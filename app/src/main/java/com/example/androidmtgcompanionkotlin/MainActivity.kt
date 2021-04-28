package com.example.androidmtgcompanionkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private lateinit var buttonDebug: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonDebug = findViewById(R.id.button_debug)

        buttonDebug.setOnClickListener {
            val intent = Intent(this@MainActivity, DebugActivity::class.java)
            startActivity(intent)
        }
    }

}