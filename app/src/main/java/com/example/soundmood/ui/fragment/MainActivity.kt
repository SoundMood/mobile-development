package com.example.soundmood.ui.fragment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.soundmood.R
import com.example.soundmood.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private var _binding : ActivityMainBinding?=null
    private val binding get(): ActivityMainBinding = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val navView : BottomNavigationView = binding.navigationView
        val navController = findNavController(R.id.navigation_main_activity_host)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homepagefragment,R.id.captureImagePageFragment,R.id.historyPageFragment
            )
        )

//        setupActionBarWithNavController(navController,appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}