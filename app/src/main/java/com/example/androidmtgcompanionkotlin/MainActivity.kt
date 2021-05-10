package com.example.androidmtgcompanionkotlin

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log

import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var buttonDebug: Button
    private lateinit var buttonNew: Button

    private val cardFolderViewModel:CardFolderViewModel by viewModels{
        CardFolderViewModelFactory((application as CardFolderApplication).repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        buttonDebug = findViewById(R.id.button_debug)

        buttonDebug.setOnClickListener {
            val intent = Intent(this@MainActivity, DebugActivity::class.java)
            startActivity(intent)
        }
        buttonNew = findViewById(R.id.button_mainNew)

        buttonNew.setOnClickListener {
            var isDeck = true
            var editText = EditText(this);
            editText.hint = "Enter name here"
            val dialogBuilder = AlertDialog.Builder(this)
                .setTitle(R.string.dialog_message)
                .setSingleChoiceItems(R.array.choice, 0)
                { dialog, which ->
                    isDeck = which == 0
                }
                .setView(editText)
                .apply {
                setPositiveButton(
                    R.string.ok
                ) { dialog, id ->

                    val name: String = (editText.getText().toString())
                    if (!name.isEmpty()) {
                        val intent = Intent(this@MainActivity, CardListActivity::class.java)
                        intent.putExtra("isDeck", isDeck)
                        intent.putExtra("name", name)

                        val collection = Folder(name, isDeck)
                        cardFolderViewModel.insert(collection)
//                        var id: Int = cardFolderViewModel.insertedId.toInt()
//                        while (id==-1) {
//                            id = cardFolderViewModel.insertedId.toInt()
//                        }
                        //intent.putExtra("collectionID", id.toString())
                        startActivity(intent)
                        dialog.dismiss()
                    } else {
                        toastInput("No name was entered, please try again.")
                    }
                }
                    setNegativeButton(
                        R.string.cancel
                    ) { dialog, id ->
                        dialog.cancel()
                    }
                }
            val alert = dialogBuilder.create()
            alert.show()
        }
    }
    private fun toastInput(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

}