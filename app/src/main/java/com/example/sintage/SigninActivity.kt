package com.example.sintage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class SigninActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        lateinit var auth: FirebaseAuth
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        auth = FirebaseAuth.getInstance()
        var db = FirebaseFirestore.getInstance()

        var constraintLayout = findViewById<ConstraintLayout>(R.id.container)
        var tvTimeMsg = findViewById<TextView>(R.id.SI_time_msg)
        var timeOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        if(timeOfDay >= 0 && timeOfDay < 12){
            //morning
            tvTimeMsg.text = "Good Morning"
            constraintLayout.setBackgroundResource(R.drawable.wine_bg_1)
        }else if (timeOfDay >=12 && timeOfDay < 16){
            // afternoon
            tvTimeMsg.text = "Good Afternoon"
            constraintLayout.setBackgroundResource(R.drawable.wine_bg_4)
        } else if(timeOfDay >= 16 && timeOfDay < 21){
            //evening
            tvTimeMsg.text = "Good Evening"
            constraintLayout.setBackgroundResource(R.drawable.wine_bg_3)

        } else if (timeOfDay >= 21 && timeOfDay < 24){
            //night
            tvTimeMsg.text = "Good Night"
            constraintLayout.setBackgroundResource(R.drawable.wine_bg_2)
        }

        var signin = findViewById<Button>(R.id.SI_signin)
        var signup = findViewById<Button>(R.id.SI_signup)
        var email = findViewById<TextInputEditText>(R.id.SI_email)
        var pass = findViewById<TextInputEditText>(R.id.SI_password)
        //TODO: Forgot password

        signup.setOnClickListener{
            startActivity(Intent(this, SignupActivity::class.java))
            //Toast.makeText(this, "button pressed", Toast.LENGTH_SHORT).show()
        }

        signin.setOnClickListener{
            auth.signInWithEmailAndPassword(email.text.toString(), pass.text.toString())
                .addOnCompleteListener {
                    if(it.isSuccessful){

                        startActivity(Intent(this, HomeActivity::class.java))
                    } else {
                        Toast.makeText(this, "Invalid email/password", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}