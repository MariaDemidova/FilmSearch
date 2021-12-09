package com.example.filmsearch.ui.main.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.*
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.filmsearch.R
import com.example.filmsearch.databinding.FragmentMapsBinding
import com.example.filmsearch.ui.main.model.Coordinations
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.fragment_maps.*
import java.io.IOException

private const val REFRESH_PERIOD =
    60000L // будем ходить за локацией раз в 60000 миллисекунд - раз в минуту
private const val MINIMAL_DISTANCE =
    100f //минимальная дистанция. если не изминилась локация на 100 м, то новую не надо передавать

class MapsFragment : Fragment() {
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    var initialPlace = Coordinations(52.52000659999999, 13.404953999999975)

    private lateinit var map: GoogleMap
    private val markers: ArrayList<Marker> = arrayListOf()
    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap

        googleMap.moveCamera(
            CameraUpdateFactory.newLatLng(
                LatLng(
                    initialPlace.lat,
                    initialPlace.lng
                )
            )
        )
        googleMap.setOnMapLongClickListener { latLng ->
            getAddressAsync(latLng)
            addMarkerToArray(latLng)
            drawLine()
        }
        activateMyLocation(googleMap)
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        val locationManager =
            context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager //возвращает обжект. приводим к типу лок манедж

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) { //проверяем, включен ли, если вкл, то получаем его
            locationManager.getProvider(LocationManager.GPS_PROVIDER)?.let {

                locationManager.requestLocationUpdates(//метод, который будет возвращать локацию с какой-то частотой
                    LocationManager.GPS_PROVIDER, //говорим, какие нужны координаты
                    REFRESH_PERIOD,
                    MINIMAL_DISTANCE,
                    object : LocationListener {
                        override fun onLocationChanged(location: Location) { //вызывается, когда меняется геолокац
                            initialPlace.lat = location.latitude
                            initialPlace.lng = location.longitude

                            map.addMarker(
                                MarkerOptions().position(LatLng(initialPlace.lat, initialPlace.lng))
                                    .title(getString(R.string.marker_start))
                            )
                            getAddressByLocation(location)
                        } //будет слушать гео локацию

                        override fun onStatusChanged( // когда вкл или выкл геолокац
                            provider: String?,
                            status: Int,
                            extras: Bundle?
                        ) {
                            //super.onStatusChanged(provider, status, extras) //комментим, чтоб не падало
                        }

                        override fun onProviderEnabled(provider: String) {
                        }

                        override fun onProviderDisabled(provider: String) {
                        }
                    }
                )
            }
        } else {
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) //последняя известная локация
                ?.let { //вызывается, когда меняется геолокац
                    getAddressByLocation(it)
                }
        } ?: AlertDialog.Builder(requireContext())
            .setMessage(R.string.AD_Access_to_geo_mess_map)
            .create()
            .show()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getLocation()
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        initSearchByAddress()
    }

    private fun initSearchByAddress() {
        binding.buttonSearch.setOnClickListener {
            val geoCoder = Geocoder(it.context)
            val searchText = searchAddress.text.toString()
            Thread {
                try {
                    val addresses = geoCoder.getFromLocationName(searchText, 1)
                    if (addresses.size > 0) {
                        goToAddress(addresses, it, searchText)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }.start()
        }
    }

    private fun goToAddress(
        addresses: MutableList<Address>,
        view: View,
        searchText: String

    ) {
        val location = LatLng(
            addresses[0].latitude,
            addresses[0].longitude
        )
        view.post {
            setMarker(location, searchText, R.drawable.ic_map_marker32)
            map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    location,
                    15f
                )
            )
        }
    }

    private fun getAddressAsync(location: LatLng) {
        context?.let {
            val geoCoder = Geocoder(it)
            Thread {
                try {
                    val addresses =
                        geoCoder.getFromLocation(location.latitude, location.longitude, 1)
                    textAddress.post { textAddress.text = addresses[0].getAddressLine(0) }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }.start()
        }
    }

    private fun addMarkerToArray(location: LatLng) {
        val marker =
            setMarker(location, markers.size.toString(), R.drawable.ic_map_marker32)
        markers.add(marker)
    }

    private fun setMarker(
        location: LatLng,
        searchText: String,
        resourceId: Int
    ): Marker {
        return map.addMarker(
            MarkerOptions()
                .position(location)
                .title(searchText)
                .icon(BitmapDescriptorFactory.fromResource(resourceId))
        )
    }

    private fun drawLine() {
        val last: Int = markers.size - 1
        if (last >= 1) {
            val previous: LatLng = markers[last - 1].position
            val current: LatLng = markers[last].position
            map.addPolyline(
                PolylineOptions()
                    .add(previous, current)
                    .color(Color.RED)
                    .width(5f)
            )
        }
    }

    private fun activateMyLocation(googleMap: GoogleMap) {
        context?.let {
            val isPermissionGranted =
                ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED
            googleMap.isMyLocationEnabled = isPermissionGranted
            googleMap.uiSettings.isMyLocationButtonEnabled = isPermissionGranted
        }

    }

    companion object {
        fun newInstance() = MapsFragment()
    }


    private fun getAddressByLocation(location: Location) {
        val geocoder = Geocoder(requireContext())
        Thread {
            try {
                val address = geocoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1
                )

                requireActivity().runOnUiThread {
                    binding.textAddress.text = address[0].getAddressLine(0)
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }
}
