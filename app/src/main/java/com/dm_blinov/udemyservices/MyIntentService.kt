package com.dm_blinov.udemyservices

import android.app.IntentService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyIntentService: IntentService(NAME) {


    override fun onHandleIntent(intent: Intent?) {
        log("onHandleIntent")
        for (i in 0 until 10) {
            Thread.sleep(1000)
            log("Timer $i")
        }
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(1, createNotification())
        setIntentRedelivery(false)
        log("onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
    }

    private fun log(msg: String) {
        Log.d("SERVICE_TAG", "MyIntentService $msg")
    }

    private fun createNotification(): Notification {

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("TitleForegroundService")
            .setContentText("TextForegroundService")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .build()
    }

    private fun createNotificationChannel() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }


    companion object{
        private const val NAME = "MyIntentService"
        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_NAME = "channel_name"
        const val START = "start"

        fun newIntent(context: Context): Intent {
            return Intent(context, MyIntentService::class.java)
        }
    }
}