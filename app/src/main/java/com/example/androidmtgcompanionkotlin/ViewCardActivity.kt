package com.example.androidmtgcompanionkotlin

import android.content.Intent
import android.os.Bundle
import android.app.AlertDialog
import android.text.method.LinkMovementMethod
import android.widget.*
import androidx.activity.viewModels

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.squareup.picasso.Picasso

class ViewCardActivity : AppCompatActivity() {

    private lateinit var textViewName: TextView
    private lateinit var textViewMana: TextView
    private lateinit var textViewManaCon: TextView
    private lateinit var textViewType: TextView
    private lateinit var textViewRulesText: TextView
    private lateinit var textViewExternal: TextView
    private lateinit var imageViewCardArt: ImageView

    //private lateinit var buttonDelete:Button
    private lateinit var card: Card
    private var index: Int = 0

    private val cardFolderViewModel: CardFolderViewModel by viewModels {
        CardFolderViewModelFactory((application as CardFolderApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card)

        textViewName = findViewById(R.id.textView_cardName)
        textViewMana = findViewById(R.id.textView_cardMana)
        textViewManaCon = findViewById(R.id.textView_cardManaCon)
        textViewType = findViewById(R.id.textView_cardType)
        textViewRulesText = findViewById(R.id.textView_cardRulesText)
        textViewExternal = findViewById(R.id.textView_cardLink)
        imageViewCardArt = findViewById(R.id.imageView_cardArt)


        index = intent.getIntExtra("id", -1)

        if (index != -1) {
            cardFolderViewModel.select(index).observe(this, Observer {
                if (it != null) {
                    this.card = it
                    textViewName.text = card.name
                    textViewMana.text = card.mana
                    textViewManaCon.text = card.manaCon.toString()
                    textViewType.text = card.type
                    textViewRulesText.text = card.rulesText
                    textViewExternal.text = card.scryfallURI
                    textViewExternal.movementMethod = LinkMovementMethod.getInstance();
                    Picasso.get().load(card.imageURI).into(imageViewCardArt)
                }
            })
        }
    }
}
        // buttonUpdate = findViewById(R.id.button_update)
        // buttonDelete = findViewById(R.id.button_delete)

//        buttonUpdate.setOnClickListener{
//            val intent = Intent(this@ViewCardActivity, AddActivity::class.java)
//            intent.putExtra("content", card.content)
//            intent.putExtra("id", card.id)
//            intent.putExtra("reflection", card.reflection)
//            intent.putExtra("title", card.title)
//            intent.putExtra("date", card.date)
//            intent.putExtra("emotion", card.emotion)
//            startActivity(intent)
//        }

//        buttonDelete.setOnClickListener{
//            // Alert Box: https://www.tutorialkart.com/kotlin-android/android-alert-dialog-example/
//            val dialogBuilder = AlertDialog.Builder(this)
//            dialogBuilder.setMessage("Do you really want to delete this dream entry?")
//                .setCancelable(false)
//                .setPositiveButton("Proceed") { dialog, id ->
//                    val tempIndex:Int = index
//                    index = -1
//                    dreamViewModel.delete(tempIndex)
//                    finish()
//                }
//                .setNegativeButton("Cancel") { dialog, id ->
//                    dialog.cancel()
//                }
//            val alert = dialogBuilder.create()
//            alert.setTitle("Alert!")
//            alert.show()
//        }

