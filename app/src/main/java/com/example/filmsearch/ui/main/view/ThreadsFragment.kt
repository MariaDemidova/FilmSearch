package com.example.filmsearch.ui.main.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.filmsearch.R
import com.example.filmsearch.databinding.FragmentThreadsBinding
import com.example.filmsearch.ui.main.model.MainService
import com.example.filmsearch.ui.main.services.USUAL_SERVICE_STRING_EXTRA
import com.example.filmsearch.ui.main.services.UsualService
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

const val TEST_BROADCAST_INTENT_FILTER = "TEST BROADCAST INTENT FILTER"
const val THREADS_FRAGMENT_BROADCAST_EXTRA = "THREADS_FRAGMENT_EXTRA"

class ThreadsFragment : Fragment(), CoroutineScope by MainScope() {
    private var _binding: FragmentThreadsBinding? = null
    private val binding get() = _binding!!

    private val testReceiver: BroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {

//            intent.getStringExtra(THREADS_FRAGMENT_BROADCAST_EXTRA)?.let {
//                addView(context, it)
//            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            LocalBroadcastManager.getInstance(it)
                .registerReceiver(testReceiver, IntentFilter(TEST_BROADCAST_INTENT_FILTER))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThreadsBinding.inflate(inflater, container, false)
        return binding.root
    }

    // @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        button.setOnClickListener {
            launch {
                val resultJob = async(Dispatchers.Default) {
                    startCalculations(editText.text.toString().toInt())
                }
                textView.text = resultJob.await()
                mainContainer.addView(AppCompatTextView(it.context).apply {
                    text = getString(R.string.in_main_thread)
                    textSize = resources.getDimension(R.dimen.main_container_text_size)
                })
            }
        }
      //  initServiceButton()

//        val handlerThread = HandlerThread("some thread")
//        handlerThread.start()
//
//        val handler = Handler(handlerThread.looper)

        initServiceWithBroadcastButton()

    }

    private fun initServiceWithBroadcastButton() {
        binding.serviceButton.setOnClickListener {
            context?.let {
                it.startService(Intent(it, UsualService::class.java).apply {
                    putExtra(
                        USUAL_SERVICE_STRING_EXTRA,
                        binding.editText.text.toString().toInt()
                    )
                })
            }
        }
    }

    override fun onDestroy() {
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(testReceiver)
        }
        cancel()
        super.onDestroy()
    }

    private fun startCalculations(seconds: Int): String {
        val date = Date()
        var diffInSec: Long

        do {
            val currentDate = Date()
            val diffInMs: Long = (currentDate.time - date.time)
            diffInSec = (TimeUnit.MILLISECONDS).toSeconds(diffInMs)
        } while (diffInSec < seconds)
        return diffInSec.toString()
    }

    companion object {
        fun newInstance() = ThreadsFragment()
    }

}