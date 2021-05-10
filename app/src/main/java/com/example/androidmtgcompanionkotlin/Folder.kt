package com.example.androidmtgcompanionkotlin


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "folder_table")
class Folder(@ColumnInfo(name="name") var title:String,
             @ColumnInfo(name="isDeck") var isDeck:Boolean,
             @ColumnInfo(name="favorite") var favorite:Boolean = false){
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id:Int = 0
}