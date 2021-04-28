package com.example.androidmtgcompanionkotlin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class DebugActivity: AppCompatActivity() {
    private lateinit var buttonCamera: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug)

        buttonCamera = findViewById(R.id.button_camera)

        buttonCamera.setOnClickListener {
            val intent = Intent(this@DebugActivity, CameraActivity::class.java)
            startActivity(intent)
        }
    }
}