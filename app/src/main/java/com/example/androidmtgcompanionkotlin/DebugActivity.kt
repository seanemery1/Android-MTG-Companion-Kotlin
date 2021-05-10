package com.example.androidmtgcompanionkotlin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class DebugActivity: AppCompatActivity() {
    private lateinit var buttonCamera: Button
    private lateinit var buttonList: Button

    private val cardFolderViewModel:CardFolderViewModel by viewModels{
        CardFolderViewModelFactory((application as CardFolderApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug)

        buttonCamera = findViewById(R.id.button_camera)

        buttonCamera.setOnClickListener {
            val intent = Intent(this@DebugActivity, CameraActivity::class.java)
            startActivity(intent)
        }

        buttonList= findViewById(R.id.button_list)

        buttonList.setOnClickListener {
            val card = Card("New", "New",4f,"New","New",1,"test","test", -1)
            cardFolderViewModel.insert(card)
            finish()
        }
    }
}
