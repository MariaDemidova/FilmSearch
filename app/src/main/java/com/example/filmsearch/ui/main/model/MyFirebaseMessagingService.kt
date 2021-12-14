package com.example.filmsearch.ui.main.model

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.filmsearch.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("fff", " onMessageReceived ${remoteMessage.data}")

        val remoteMessageData = remoteMessage.data
        if(remoteMessageData.isNotEmpty()){
            handleDataMessage(remoteMessageData)

        } else{
            handleDataMessage(mapOf("title" to "My Title", "message" to "my message"))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleDataMessage(remoteMessageData: Map<String, String>) {
        val title = remoteMessageData[PUSH_KEY_TITLE]
        val message = remoteMessageData[PUSH_KEY_MESSAGE]

        if(title.isNullOrBlank() && !message.isNullOrBlank()) {
            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_baseline_message_24)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(NotificationChannel(CHANNEL_ID, "FirstChannel", NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "Description"
            })

            notificationManager.notify(NOTIFICATION_ID, notification.build())

        }

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("fff", " Token ${token}")
    }


    companion object {
        private const val PUSH_KEY_TITLE = "title"
        private const val PUSH_KEY_MESSAGE = "message"
        private const val CHANNEL_ID = "channel_id"
        private const val NOTIFICATION_ID = 37
    }

}