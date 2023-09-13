package com.vineet.shelfapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import com.vineet.shelfapp.R
import com.vineet.shelfapp.databinding.ActivityMainBinding
import com.vineet.shelfapp.utils.AppConstants
import com.vineet.shelfapp.utils.ShelfPreference
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navGraph: NavGraph

    private lateinit var navController: NavController
    /**
     * lifecycle methods
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigation()
    }

    /**
     * navGraph setup
     */
    private fun setupNavigation() {
        val navHostFragment =
            (supportFragmentManager.findFragmentById(R.id.shelfNavHost) as NavHostFragment)
        val graphInflater = navHostFragment.navController.navInflater
        navGraph = graphInflater.inflate(R.navigation.navigation_graph)
        navController = navHostFragment.navController
        val destination: Int
        if (ShelfPreference.getPreference(AppConstants.PreferenceConstants.GET_TOKEN)?.isNotEmpty() == true)
        {
            destination = R.id.bookListFragment
            navGraph.setStartDestination(destination)
            navController.setGraph(navGraph,null)
        }
        else
        {
            destination = R.id.loginFragment
            navGraph.setStartDestination(destination)
            navController.setGraph(navGraph,null)
        }
    }

}