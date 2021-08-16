package com.example.a1.workManager.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import io.karn.notify.Notify
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class NotificationWorker(val context: Context, workerParams: WorkerParameters): Worker(context, workerParams) {
    override fun doWork(): Result {
        val currentTime = SimpleDateFormat("dd/M/yyyy hh:mm:ss", Locale.getDefault()).format(Date())
        Timber.tag("Result from worker").d(currentTime)
        Notify.with(context)
            .content {
                title = "Message from FavDish App"
                text = "Current time $currentTime"
            }
            .show()
        return Result.success()
    }
}