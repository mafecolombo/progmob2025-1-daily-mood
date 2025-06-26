package com.android.dailymood.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.android.dailymood.R
import com.android.dailymood.utils.SessionManager

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root_layout)) { view, insets ->
            val topInset = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            view.setPadding(0, topInset, 0,0)
            insets
        }


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)

        when {
            SessionManager.isFirstTime(this) -> {
                navGraph.setStartDestination(R.id.welcomeFragment)
                SessionManager.setFirstTime(this, false)
            }

            SessionManager.getLoggedInUser(this) != null -> {
                navGraph.setStartDestination(R.id.homeFragment)
            }

            else -> {
                navGraph.setStartDestination(R.id.loginFragment)
            }
        }

        navController.graph=navGraph
    }
}