package com.example.androidmtgcompanionkotlin

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface CardDAO {

    @Query("SELECT * FROM card_table ORDER BY id ASC")
    fun getAllCardsAsc() : Flow<List<Card>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(card:Card)

    @Query("UPDATE card_table SET name=:name, quantity=:quantity, collectionID=:collectionID WHERE id=:id")
    suspend fun update(name:String,
                       quantity:Int,
                       collectionID:Int,
                       id:Int)

//    @Query("UPDATE card_table SET name=:name, mana=:mana, manaCon=:manaCon, cardType=:cardType, rulesText=:rulesText, quantity=:quantity, scryfallURI=:scryfallURI, imageURI=:imageURI, collectionID=:collectionID WHERE id=:id")
//    suspend fun update(name:String,
//                       mana:String,
//                       manaCon:Int,
//                       cardType:String,
//                       rulesText:String,
//                       quantity:Float,
//                       scryfallURI:String,
//                       imageURI:String,
//                       collectionID:Int,
//                       id:Int)

    @Query("DELETE FROM card_table WHERE id=:id")
    suspend fun delete(id:Int)

    @Query("SELECT * FROM card_table WHERE id=:id")
    fun select(id:Int): LiveData<Card>
}

//    @Query("SELECT * FROM card_table WHERE id =:id ORDER BY name ASC")
//    fun selectAllCardsFromFolder(id:Int) : Flow<List<Card>>
//
//@Insert(onConflict = OnConflictStrategy.IGNORE)
//suspend fun insert(card:Card)
//
////    @Query("UPDATE card_table SET title=:title, content=:content, reflection=:reflection, emotion=:emotion, date=:date WHERE id=:id")
////    suspend fun update(title:String,
////                       content:String,
////                       reflection:String,
////                       emotion:String,
////                       date:String,
////                       id:Int)
////
////    @Query("DELETE FROM card_table WHERE id=:id")
////    suspend fun delete(id:Int)
////
////    @Query("SELECT * FROM card_table WHERE id=:id")
////    fun select(id:Int): LiveData<Card>
//
//// Colelction
//@Insert(onConflict = OnConflictStrategy.IGNORE)
//fun insert(folder: Folder): Long
//}

