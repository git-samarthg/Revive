package com.example.revive

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find BottomNavigationView from the layout
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)

        // Set the default fragment to HomeFragment or any fragment you'd like
        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }

        // Handle BottomNavigationView item clicks
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment())  // Load Home Fragment
                    true
                }
                R.id.nav_messages -> {
                    loadFragment(ChatFragment())  // Load Messages Fragment
                    true
                }
                R.id.nav_sell -> {
                    loadFragment(SellFragment())  // Load Sell Fragment
                    true
                }
                R.id.nav_cart -> {
                    loadFragment(CartFragment())  // Load Cart Fragment
                    true
                }
                R.id.nav_profile -> {
                    loadFragment(ProfileFragment())  // Load Profile Fragment
                    true
                }
                else -> false
            }
        }

        // Handle navigation drawer item clicks (for logout)
        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_logout -> {
                    Log.d("MainActivity", "Logout clicked")  // Debugging log
                    drawerLayout.closeDrawers()  // Close the drawer

                    logOutUser()  // Call the logOutUser function to handle logout
                    true  // Indicate the item was handled
                }
                else -> {
                    drawerLayout.closeDrawers()  // Close drawer for other items
                    false  // Indicate the item was not handled
                }
            }
        }
    }

    // Function to load fragments dynamically
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    // Function to log out the user and redirect to SignInActivity
    private fun logOutUser() {
        // Sign out the user using FirebaseAuth
        FirebaseAuth.getInstance().signOut()

        // Clear the back stack and redirect to SignInActivity
        val intent = Intent(this, SignInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

        finish()  // Close MainActivity to prevent back navigation
    }
}
