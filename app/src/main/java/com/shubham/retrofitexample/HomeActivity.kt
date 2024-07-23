package com.shubham.retrofitexample

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.shubham.retrofitexample.Fragment.DashboardFragment
import com.shubham.retrofitexample.Fragment.ProfileFragment
import com.shubham.retrofitexample.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomeBinding
    lateinit var sharedPrefManager: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            loadFragment(DashboardFragment())
        }

        sharedPrefManager = SharedPrefManager(this)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.dashboard -> {
                    loadFragment(DashboardFragment())
                    Toast.makeText(this, "Dashboard", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.profile -> {
                    loadFragment(ProfileFragment())
                    Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
                    true
                }

                else -> false
            }
        }

    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.relative_layout, fragment)
            .commit()
    }
}
