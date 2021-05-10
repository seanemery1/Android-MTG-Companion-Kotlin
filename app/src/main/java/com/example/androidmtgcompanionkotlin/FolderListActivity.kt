package com.example.androidmtgcompanionkotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabItem
import com.google.android.material.tabs.TabLayout

class FolderListActivity : AppCompatActivity() {


    private lateinit var recyclerView: RecyclerView


   // private lateinit var tabLayout: TabLayout
//    private lateinit var tabAll: TabItem
//    private lateinit var tabDecks: TabItem
//    private lateinit var tabBinders: TabItem
//    private lateinit var tabFavorites: TabItem

    private lateinit var buttonAll: Button
    private lateinit var buttonDecks: Button
    private lateinit var buttonBinders: Button
    private lateinit var buttonFavorites: Button

    //private var collectionID:Int = -1
    private val cardFolderViewModel: CardFolderViewModel by viewModels {
        CardFolderViewModelFactory((application as CardFolderApplication).repository)
    }

    //private lateinit var dialogBuilder : AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folderlist)


        buttonAll = findViewById(R.id.button_all)
        buttonDecks = findViewById(R.id.button_decks)
        buttonBinders = findViewById(R.id.button_binders)
        buttonFavorites = findViewById(R.id.button_favorites)
        recyclerView = findViewById(R.id.recyclerView_folderlist)

        cardFolderViewModel.selectedFolderCategory.value = 0

        buttonAll.setOnClickListener {
            cardFolderViewModel.selectedFolderCategory.value = 0
        }
        buttonDecks.setOnClickListener {
            cardFolderViewModel.selectedFolderCategory.value = 1
        }
        buttonBinders.setOnClickListener {
            cardFolderViewModel.selectedFolderCategory.value = 2
        }
        buttonFavorites.setOnClickListener {
            cardFolderViewModel.selectedFolderCategory.value = 3
        }


        val adapter = FolderListAdapter()
        recyclerView.adapter = adapter
        //recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        cardFolderViewModel.foldersOfSelectedCategory.observe(this, Observer {
                folders -> folders?.let{
            adapter.submitList(it)
        }
        })


    }
}

//recyclerView.layoutManager = GridLayoutManager(this, 3)

//        <string name="tab_all">All</string>
//        <string name="tab_decks">Decks</string>
//        <string name="tab_binders">Binders</string>
//        <string name="tab_favorites">Favorites</string>
//

//        if (collectionID==-1) {
//            cardFolderViewModel.getLastRow().observe(this, {item->
//                collectionID = item.id
//                cardFolderViewModel.selectedFolder.value = item
//            })
//        }
