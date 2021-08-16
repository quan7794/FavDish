package com.example.a1.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.a1.R
import com.example.a1.databinding.ActivityMainBinding
import com.example.a1.workManager.requests.RequestManager
import com.example.a1.workManager.requests.WorkName
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = findNavController(R.id.nav_host_fragment_activity_main)
        setupActionBarWithNavController(
            navController, AppBarConfiguration(
                setOf(
                    R.id.navigation_all_dishes, R.id.navigation_favorite_dishes, R.id.navigation_random_dish
                )
            )
        )
        binding.navBottomView.setupWithNavController(navController)
        val existWorkPolicy = ExistingPeriodicWorkPolicy.REPLACE
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(WorkName.NOTIFICATION_WORK.workName, existWorkPolicy, RequestManager.getNotificationRequest())
        Timber.d("Work name: ${WorkName.NOTIFICATION_WORK.workName}")
        WorkManager.getInstance(this).getWorkInfosForUniqueWorkLiveData(WorkName.NOTIFICATION_WORK.workName).observe(this, { workInfo ->
            when (workInfo[0].state) {
                WorkInfo.State.SUCCEEDED -> Timber.tag("Work State").d("SUCCEEDED")
                WorkInfo.State.ENQUEUED -> Timber.tag("Work State").d("ENQUEUED")
                WorkInfo.State.CANCELLED -> Timber.tag("Work State").d("CANCELLED")
                WorkInfo.State.FAILED -> Timber.tag("Work State").d("FAILED")
                WorkInfo.State.BLOCKED -> Timber.tag("Work State").d("BLOCKED")
                else -> Timber.tag("Work State").d(workInfo[0].state.name)
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }

    fun hideBottomNav() {
        binding.navBottomView.apply {
            clearAnimation()
            animate().translationY(height.toFloat()).duration = 300
        }
    }

    fun showBottomNav() {
        binding.navBottomView.apply {
            clearAnimation()
            animate().translationY(0F).duration = 300
        }
    }
}