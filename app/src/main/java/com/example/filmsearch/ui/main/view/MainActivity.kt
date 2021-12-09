package com.example.filmsearch.ui.main.view

import android.Manifest
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.filmsearch.R
import com.example.filmsearch.databinding.MainActivityBinding
import com.example.filmsearch.ui.main.utils.AlertDialogs

class MainActivity : AppCompatActivity() {

    private val permissionResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            when {
                result -> getContact()
                !ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_CONTACTS
                ) -> {
                    AlertDialogs(this).showAlertDialog(getString(R.string.AD_access_to_contacts_title), getString(R.string.AD_access_to_contacts_message))
                }
                else -> Toast.makeText(this, R.string.No_access_to_permission, Toast.LENGTH_LONG).show()
            }
        }

    private val binding: MainActivityBinding by lazy {
        MainActivityBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.idHistory -> {
                supportFragmentManager.apply {
                    beginTransaction()
                        .replace(R.id.container, HistoryFragment())
                        .addToBackStack("")
                        .commitAllowingStateLoss()
                }
                true
            }
            R.id.getContact -> {
                permissionResult.launch(Manifest.permission.READ_CONTACTS)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getContact() {

        val cursor: Cursor? = contentResolver.query( //обращаемся по запросу к контент провайдеру
            ContactsContract.Contacts.CONTENT_URI,  // по какому урлу
            null,  //передает параметры
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME + " ASC"
        )

        val contacts = mutableListOf<String>()
        cursor?.let {
            for (i in 0..cursor.count) {
                //переходим на позицию и курсор
                if (cursor.moveToPosition(i)) {
                    //берем из курсор столбец с именем
                    val name =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    contacts.add(name)
                }
            }
            it.close()
        }
        AlertDialog.Builder(this)
            .setItems(contacts.toTypedArray(), { d, w -> })
            .setCancelable(true)
            .show()
    }
}
