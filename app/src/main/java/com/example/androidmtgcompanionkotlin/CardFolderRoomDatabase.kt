package com.example.androidmtgcompanionkotlin

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Card::class, Folder::class), version = 1, exportSchema = false)
abstract class CardFolderRoomDatabase : RoomDatabase() {
    abstract fun cardDAO():CardDAO // getter
    abstract fun folderDAO():FolderDAO // getter

    companion object{

        @Volatile
        private var INSTANCE:CardFolderRoomDatabase? = null

        fun getDatabase(context:Context) : CardFolderRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CardFolderRoomDatabase::class.java,
                    "card_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}