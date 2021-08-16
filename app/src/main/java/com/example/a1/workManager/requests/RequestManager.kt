package com.example.a1.workManager.requests

import androidx.work.Constraints
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import com.example.a1.workManager.workers.NotificationWorker
import java.util.concurrent.TimeUnit

object RequestManager {

    fun getNotificationRequest(constraints: Constraints? = null) =
        PeriodicWorkRequestBuilder<NotificationWorker>(PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS)
            .setConstraints(constraints ?: Constraints.Builder().build())
            .build()
}

enum class WorkName(val workName: String) {
    NOTIFICATION_WORK("Notification Work")
}