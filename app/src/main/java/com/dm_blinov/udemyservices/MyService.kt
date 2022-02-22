package com.dm_blinov.udemyservices

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*

class MyService : Service() {

    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("onStartCommand")
        val start = intent?.getIntExtra(START, 0) ?: 0
        scope.launch {
            for (i in start until start + 100) {
                delay(1000)
                log("Timer $i")
            }
        }
        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
        scope.cancel()
    }

    private fun log(msg: String) {
        Log.d("SERVICE_TAG", "MyService $msg")
    }

    companion object {
        const val START = "start"

        fun newIntent(context: Context, start: Int): Intent {
            return Intent(context, MyService::class.java).apply {
                putExtra("START", start)
            }
        }
    }
}