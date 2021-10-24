package com.example.filmsearch.ui.main.services

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.filmsearch.ui.main.reciver.MainBroadcastReceiver
import com.example.filmsearch.ui.main.view.TEST_BROADCAST_INTENT_FILTER
import com.example.filmsearch.ui.main.view.THREADS_FRAGMENT_BROADCAST_EXTRA

private const val TAG = "UsualServiceTAG"
const val USUAL_SERVICE_STRING_EXTRA = "MainServiceExtra"


class UsualService (name: String? = "UsualService") : IntentService(name) {


    override fun onHandleIntent(intent: Intent?) {
        intent?.let {
            sendBack(it.getIntExtra(USUAL_SERVICE_STRING_EXTRA, 0).toString())
        }

    }

    private fun sendBack(result: String) {
        val broadcastIntent = Intent(TEST_BROADCAST_INTENT_FILTER)
        broadcastIntent.putExtra(THREADS_FRAGMENT_BROADCAST_EXTRA, result)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }


    private fun createLogMessage(message: String) {
        Log.d(TAG, message)
    }

    override fun onCreate() {
        createLogMessage("onCreate")
        super.onCreate()
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createLogMessage("OnStartCommand")
        return super.onStartCommand(intent, flags, startId)

    }

    override fun onDestroy() {
        createLogMessage("onDestroy")
        super.onDestroy()
    }

    companion object{
        fun start(context: Context) {
            val usualServiceIntent = Intent(context, UsualService::class.java)
            context.startService(usualServiceIntent)
        }

        fun stop(context: Context) {
            val usualServiceIntent = Intent(context, UsualService::class.java)
            context.stopService(usualServiceIntent)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

}