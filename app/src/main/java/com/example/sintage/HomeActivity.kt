package com.example.sintage

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.ContactsContract
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
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
    lateinit var welcome : TextView
    lateinit var logo : ImageView
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
        welcome = findViewById(R.id.welcome)
        logo = findViewById(R.id.logo)

        welcome.setVisibility(View.INVISIBLE)
        logo.setVisibility(View.INVISIBLE)
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val db = FirebaseFirestore.getInstance()
        welcome = findViewById(R.id.welcome)
        auth = FirebaseAuth.getInstance()
        var user = ""

        if(auth.currentUser != null){
            db.collection("users").get()
                .addOnCompleteListener { task ->
                    for (document in task.result!!){
                        if(document.data["UID"].toString().equals(auth.uid.toString())){
                            val sharedPreferences : SharedPreferences = getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)
                            //creating an editor
                            val editor : SharedPreferences.Editor = sharedPreferences.edit()
                            editor.apply{
                                putString("userUID", document.data["UID"].toString())
                                putString("userEmail", document.data["email"].toString())
                                putString("userFirstName", document.data["firstName"].toString())
                                putString("userLastName", document.data["lastName"].toString())
                                putInt("userXP", document.data["uXP"].toString().toInt())
                            }

                            welcome.setText("Hello, ${document.data["firstName"].toString()}")
                        }
                    }
                }
        }




        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnNavigationItemSelectedListener(navListener)

    }

//    fun getUXP() : Int{
//        auth = FirebaseAuth.getInstance()
//        val sharedPreferences : SharedPreferences = this.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)
//        val savedData : String? = sharedPreferences.getString("myData", "No data found")
//        val savedInt : Int = sharedPreferences.getInt("uXP", 1)
//
//
//        return savedInt
//    }

}