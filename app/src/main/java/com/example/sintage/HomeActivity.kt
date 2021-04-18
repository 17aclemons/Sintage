package com.example.sintage

import android.os.Bundle
import android.provider.ContactsContract
import android.view.MenuItem
import android.widget.Button
import androidx.annotation.NonNull
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
    lateinit var photo:Button
    lateinit var auth: FirebaseAuth

    val navListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var selectedFragment: Fragment = CollectionFragment()
        //was null? idk
        when (item.itemId) {
            R.id.nav_collection -> selectedFragment = CollectionFragment()
            R.id.nav_scan -> selectedFragment = ScanFragment()
            R.id.nav_profile -> selectedFragment = ProfileFragment()
        }
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment).commit()
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnNavigationItemSelectedListener(navListener)

    }

}