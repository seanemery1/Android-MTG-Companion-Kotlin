package com.example.androidmtgcompanionkotlin

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "card_table")
class Card(@ColumnInfo(name="name") var name:String,
           @ColumnInfo(name="mana") var mana:String,
           @ColumnInfo(name="manaCon") var manaCon:Float,
           @ColumnInfo(name="cardType") var type:String,
           @ColumnInfo(name="rulesText") var rulesText:String,
           @ColumnInfo(name ="quantity") var quantity:Int,
           @ColumnInfo(name ="scryfallURI") var scryfallURI:String,
           @ColumnInfo(name ="imageURI") var imageURI:String,
           @ColumnInfo(name = "collectionID") var collectionID:Int){
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id:Int = 0
}