package com.dm_blinov.udemyservices

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.util.Log

class MyIntentServiceForJobScheduler: IntentService(NAME) {


    override fun onHandleIntent(intent: Intent?) {
        val page = intent?.getIntExtra(PAGE, 0) ?: 0
        log("onHandleIntent")
        for (i in 0 until 10) {
            Thread.sleep(1000)
            log("Timer $i, page - $page")
        }
    }

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
        setIntentRedelivery(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
    }

    private fun log(msg: String) {
        Log.d("SERVICE_TAG", "MyIntentServiceForJobScheduler $msg")
    }

    companion object{
        private const val NAME = "MyIntentServiceForJobScheduler"
        private const val PAGE = "page"

        fun newIntent(context: Context, page: Int): Intent {
            return Intent(context, MyIntentService::class.java).apply {
                putExtra(PAGE, page)
            }
        }
    }
}