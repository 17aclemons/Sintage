package com.example.sintage

import android.R
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth


class HomeActivity : AppCompatActivity() {
    lateinit var photo:Button
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(com.example.sintage.R.layout.activity_home)

        //val bottomNav = findViewById<BottomNavigationItemView>(R.id.bottom_navigation)
    }

//    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
//        var selectedFragment: Fragment? = null
//        when (item.itemId) {
//            R.id.nav_profile -> selectedFragment = HomeFragment()
//            R.id.nav_collection -> selectedFragment = CollectionFragment()
//            R.id.nav_scan -> selectedFragment = SearchFragment()
//        }
//        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
//                selectedFragment).commit()
//        true
//    }

    override fun onBackPressed() {
        lateinit var auth: FirebaseAuth

        var alert = AlertDialog.Builder(this)
        alert.setTitle("Loging out")
        alert.setMessage("You are about to log out. Are you sure?")
        alert.setPositiveButton("Yes", { dialogInterface: DialogInterface, i: Int ->
            auth.signOut()
//            startActivity(Intent(this, MainActivity::class.java))
            finish() // ask the user before they close the app c:
        })
        alert.setNegativeButton("No", { dialogInterface: DialogInterface, i: Int -> })
        alert.show()
    }
}