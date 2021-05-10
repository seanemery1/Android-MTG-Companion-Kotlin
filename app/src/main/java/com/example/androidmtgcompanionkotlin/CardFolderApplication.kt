package com.example.androidmtgcompanionkotlin

import android.app.Application

class CardFolderApplication : Application() {
    val database by lazy { CardFolderRoomDatabase.getDatabase(this)}
    val repository by lazy { CardFolderRepository(database.cardDAO(), database.folderDAO())}
}