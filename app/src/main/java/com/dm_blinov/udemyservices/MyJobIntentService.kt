package com.dm_blinov.udemyservices

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService

class MyJobIntentService : JobIntentService() {

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
    }

    override fun onHandleWork(intent: Intent) {
        val page = intent.getIntExtra(PAGE, 0)
        log("onHandleIntent")
        for (i in 0 until 10) {
            Thread.sleep(1000)
            log("Timer $i, page - $page")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
    }

    private fun log(msg: String) {
        Log.d("SERVICE_TAG", "MyJobIntentService $msg")
    }

    companion object {
        private const val PAGE = "page"
        private const val JOB_ID = 555

        fun enqueue(context: Context, page: Int) {
            JobIntentService.enqueueWork(
                context,
                MyIntentService::class.java,
                JOB_ID,
                newIntent(context, page)
                )
        }

        fun newIntent(context: Context, page: Int): Intent {
            return Intent(context, MyIntentService::class.java).apply {
                putExtra(PAGE, page)
            }
        }
    }
}