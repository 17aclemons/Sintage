package com.example.sintage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class MainActivity : AppCompatActivity() {
    lateinit var photo:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        photo = findViewById<Button>(R.id.photo)

        photo.setOnClickListener {
            var intent = Intent(this, PhotoRecognizer::class.java)
            startActivity(intent)

        }

    }
}