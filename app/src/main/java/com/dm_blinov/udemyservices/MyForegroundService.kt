package com.dm_blinov.udemyservices

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*

class MyForegroundService : Service() {
    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(1, createNotification())
        log("onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("onStartCommand")
        scope.launch {
            for (i in 0 until 10) {
                delay(1000)
                log("Timer $i")
            }
            //stopSelf()
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
        scope.cancel()
    }

    private fun log(msg: String) {
        Log.d("SERVICE_TAG", "MyForegroundService $msg")
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
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    companion object {

        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_NAME = "channel_name"
        const val START = "start"

        fun newIntent(context: Context): Intent {
            return Intent(context, MyForegroundService::class.java)
        }
    }
}