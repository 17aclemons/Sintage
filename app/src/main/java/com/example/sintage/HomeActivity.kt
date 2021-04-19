package com.example.sintage

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.annotation.NonNull
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth

    val navListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var selectedFragment: Fragment = CollectionFragment()
        when (item.itemId) {
            R.id.nav_collection -> selectedFragment = CollectionFragment()
            R.id.nav_scan -> {
                //selectedFragment = ScanFragment()
                startActivity(Intent(this, UploadActivity::class.java))
            }
            R.id.nav_profile -> selectedFragment = ProfileFragment()
        }
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment).commit()
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val db = FirebaseFirestore.getInstance()
        var welcome = findViewById<TextView>(R.id.welcome)
        auth = FirebaseAuth.getInstance()
        var user = ""

        if(auth.currentUser != null){
            db.collection("users").get()
                .addOnCompleteListener { task ->
                    for (document in task.result!!){
                        if(document.data["UID"].toString().equals(auth.uid.toString())){
                            welcome.setText("Welcome, ${document.data["firstName"].toString()} ${document.data["lastName"].toString()}")
                        }
                    }
                }
        }




        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnNavigationItemSelectedListener(navListener)

    }

}