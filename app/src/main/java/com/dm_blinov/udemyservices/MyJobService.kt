package com.dm_blinov.udemyservices

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.PersistableBundle
import android.util.Log
import kotlinx.coroutines.*

class MyJobService : JobService() {

    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
        scope.cancel()
    }

    private fun log(msg: String) {
        Log.d("SERVICE_TAG", "MyJobService $msg")
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        log("onStartCommand")
        //val page = params?.extras?.getInt(PAGE) ?: 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            scope.launch {
                var workItem = params?.dequeueWork()

                while (workItem != null) {
                    val page = workItem.intent.getIntExtra(PAGE, 0)

                    for (i in 0 until 100) {
                        delay(1000)
                        log("Timer $i")
                    }
                    params?.completeWork(workItem)
                    workItem = params?.dequeueWork()
                }
                jobFinished(params, true)
            }
        }
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return true
    }

    companion object {

        const val JOB_ID = 111
        private const val PAGE = "page"

        //        fun newBundle(page: Int): PersistableBundle{
//            val obj =  PersistableBundle()
//            obj.putInt(PAGE, page)
//            return  obj
//        }
        fun newIntent(page: Int): Intent {
//            val obj = PersistableBundle()
//            obj.putInt(PAGE, page)
            return Intent().apply {
                putExtra(PAGE, page)
            }
        }
    }
}