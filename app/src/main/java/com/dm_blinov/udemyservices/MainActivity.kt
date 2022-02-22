package com.dm_blinov.udemyservices

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.app.job.JobWorkItem
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationBuilderWithBuilderAccessor
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.dm_blinov.udemyservices.databinding.ActivityMainBinding

private lateinit var binding: ActivityMainBinding
private var page = 0

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        binding.simpleService.setOnClickListener {
            stopService(MyForegroundService.newIntent(this))
            startService(MyService.newIntent(this, 25))
        }
        binding.foregroundService.setOnClickListener {
            //showNotification()
            ContextCompat.startForegroundService(
                this,
                MyForegroundService.newIntent(this)
            )
        }
        binding.intentService.setOnClickListener {
            //showNotification()
            ContextCompat.startForegroundService(
                this,
                MyIntentService.newIntent(this)
            )
        }
        binding.jobScheduler.setOnClickListener {

            // Указываем какой сервис нужен
            val componentName = ComponentName(this, MyJobService::class.java)
            // Устанавливаем нужные ограничения
            val jobInfo = JobInfo.Builder(MyJobService.JOB_ID, componentName)
                //.setExtras(MyJobService.newBundle(page++))
                .setRequiresCharging(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .build()

            // Запускаем на выполнение
            val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
            //jobScheduler.schedule(jobInfo)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val intent = MyJobService.newIntent(page++)
                jobScheduler.enqueue(jobInfo, JobWorkItem(intent))
            } else {
                startService(MyIntentServiceForJobScheduler.newIntent(this, page++))
            }
        }
        binding.jobIntentService.setOnClickListener {
            MyJobIntentService.enqueue(this, page++)
        }
        binding.workManager.setOnClickListener {
            val workManager = WorkManager.getInstance(applicationContext)
            workManager.enqueueUniqueWork(
                MyWorker.WORK_NAME
                ,ExistingWorkPolicy.APPEND
                ,MyWorker.makeRequest(page++)
            )
        }
    }
}