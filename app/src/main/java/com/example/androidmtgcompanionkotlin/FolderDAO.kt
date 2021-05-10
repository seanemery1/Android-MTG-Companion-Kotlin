package com.example.androidmtgcompanionkotlin

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface FolderDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(collection:Folder) : Long

    @Query("SELECT * FROM folder_table ORDER BY id ASC")
    fun getAllFoldersAsc() : Flow<List<Folder>>

    @Query("SELECT * FROM folder_table ORDER BY id DESC LIMIT 1")
    fun getLastRow(): LiveData<Folder>
}