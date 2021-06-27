package com.example.a1.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.a1.R
import com.example.a1.databinding.ActivityMainBinding

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
                    R.id.navigation_all_dishes,
                    R.id.navigation_favorite_dishes,
                    R.id.navigation_random_dish
                )
            )
        )
        binding.navBottomView.setupWithNavController(navController)
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