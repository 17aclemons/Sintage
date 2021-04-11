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
import java.util.*

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        lateinit var auth: FirebaseAuth

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        auth = FirebaseAuth.getInstance()

        var constraintLayout = findViewById<ConstraintLayout>(R.id.container)
        var tvTimeMsg = findViewById<TextView>(R.id.SU_time_msg)
        var timeOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        if(timeOfDay >= 0 && timeOfDay < 12){
            //morning
            tvTimeMsg.text = "Good Morning"
            constraintLayout.setBackgroundResource(R.drawable.wine_bg_1_blurred)
        }else if (timeOfDay >=12 && timeOfDay < 16){
            // afternoon
            tvTimeMsg.text = "Good Afternoon"
            constraintLayout.setBackgroundResource(R.drawable.wine_bg_4_blurred)
        } else if(timeOfDay >= 16 && timeOfDay < 12){
            //evening
            tvTimeMsg.text = "Good Evening"
            constraintLayout.setBackgroundResource(R.drawable.wine_bg_3_blurred)

        } else if (timeOfDay >= 21 && timeOfDay < 24){
            //night
            tvTimeMsg.text = "Good Night"
            constraintLayout.setBackgroundResource(R.drawable.wine_bg_2_blurred)
        }

        var signin = findViewById<Button>(R.id.SU_signin)
        var signup = findViewById<Button>(R.id.SU_signup)
        var firstName = findViewById<TextInputEditText>(R.id.SU_first_name)
        var lastName = findViewById<TextInputEditText>(R.id.SU_last_name)
        var email = findViewById<TextInputEditText>(R.id.SU_email)
        var pass = findViewById<TextInputEditText>(R.id.SU_password)
        var cpass = findViewById<TextInputEditText>(R.id.SU_confirm_password)
//      TODO: Forgot password

        signin.setOnClickListener{
            startActivity(Intent(this, SigninActivity::class.java))
        }

        signup.setOnClickListener{
            var emailMatch = android.util.Patterns.EMAIL_ADDRESS.matcher(email.text).matches()
            var pwdMatch = pass.text.toString().equals(cpass.text.toString())
            var hasLower = false
            var hasUpper = false
            var hasNumber = false
            for(letter in pass.text.toString()) {
                if (letter.isLowerCase())       hasLower = true
                else if (letter.isUpperCase())  hasUpper = true
                else if (letter.isDigit())      hasNumber = true
            }
            var pwdFormat = hasLower && hasUpper && hasNumber

            if(emailMatch && pwdMatch && pwdFormat){
                //Toast.makeText(this,"test?", Toast.LENGTH_SHORT).show()
                auth.createUserWithEmailAndPassword(email.text.toString(), pass.text.toString())

                        .addOnCompleteListener{
                            if(it.isSuccessful){
                                var user = auth.getCurrentUser()
                                //TODO: save displayName with First and Last name.
                                Toast.makeText(this, "User successfully created!", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, HomeActivity::class.java))
                            }else Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show()
                        }
            }else if(!emailMatch) {
                Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show()
            }else if (!pwdFormat){
                Toast.makeText(this, "Password must have at least one uppercase, lowercase, and number", Toast.LENGTH_SHORT).show()
            }else if(!pwdMatch) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
        }
    }
}