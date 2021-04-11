package com.example.sintage

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth


class HomeActivity : AppCompatActivity() {
    lateinit var photo:Button
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        var test = findViewById<TextView>(R.id.testTV)
        test.text = "Hello, " + auth.currentUser.displayName
    }

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