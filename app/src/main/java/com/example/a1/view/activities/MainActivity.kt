package com.example.a1.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.a1.R
import com.example.a1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        setupActionBarWithNavController(navController, AppBarConfiguration(
            setOf(
                R.id.navigation_all_dishes, R.id.navigation_favorite_dishes, R.id.navigation_random_dish
            )
        ))
        navView.setupWithNavController(navController)
    }
}