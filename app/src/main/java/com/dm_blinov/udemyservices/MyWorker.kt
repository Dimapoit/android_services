package com.dm_blinov.udemyservices

import android.content.Context
import android.util.Log
import androidx.work.*

class MyWorker(
    context: Context,
    private val workerParameters: WorkerParameters
) : Worker(context, workerParameters) {

    override fun doWork(): Result {
        val page = workerParameters.inputData.getInt(PAGE, 0)
        log("onHandleIntent")
        for (i in 0 until 10) {
            Thread.sleep(1000)
            log("Timer $i, page - $page")
        }
        return Result.success()
    }

    private fun log(msg: String) {
        Log.d("SERVICE_TAG", "MyJobIntentService $msg")
    }

    companion object {
        private const val PAGE = "page"
        const val WORK_NAME = "workName"

        fun makeRequest(page: Int): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<MyWorker>().apply {
                setInputData(workDataOf(PAGE to page))
                setConstraints(makeConstraints())
            }.build()
        }

        private fun makeConstraints(): Constraints {
            return Constraints.Builder()
                .setRequiresCharging(true)
                .build()
        }
    }
}