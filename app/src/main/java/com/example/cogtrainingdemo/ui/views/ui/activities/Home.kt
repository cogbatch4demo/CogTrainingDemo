package com.example.cogtrainingdemo.ui.views.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.cogtrainingdemo.R
import com.example.cogtrainingdemo.databinding.ActivityHomeBinding
import com.example.cogtrainingdemo.ui.listener.CallbackListener
import com.example.cogtrainingdemo.ui.views.ViewBase
import com.example.cogtrainingdemo.ui.views.ui.fragments.ARTIST_DATA
import com.example.cogtrainingdemo.ui.views.ui.fragments.DashboardFragment
import com.example.cogtrainingdemo.ui.views.ui.fragments.EPISODE_DATA
import com.example.cogtrainingdemo.ui.views.ui.fragments.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class Home : AppCompatActivity() , ViewBase, CallbackListener {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_home)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


    }

    override fun onBackPress() {
        TODO("Not yet implemented")
    }

    override fun clickOnItem(item: Any?) {
        TODO("Not yet implemented")
    }

    override fun observeViewModelStates() {
        TODO("Not yet implemented")
    }
}