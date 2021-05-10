package com.example.androidmtgcompanionkotlin


import android.util.Log
import androidx.lifecycle.LiveData
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.coroutines.flow.Flow
import org.json.JSONArray
import org.json.JSONObject


class CardFolderRepository(private val cardDao: CardDAO, private val folderDao: FolderDAO) {


    val allFolders: Flow<List<Folder>> = folderDao.getAllFoldersAsc()
    val allCards: Flow<List<Card>> = cardDao.getAllCardsAsc()

    suspend fun insert(card:Card) {
        cardDao.insert(card)
    }
    suspend fun delete(id:Int) {
        cardDao.delete(id)
    }
    suspend fun update(name:String,
                       quantity:Int,
                       collectionID:Int,
                       id:Int) {
        cardDao.update(name, quantity, collectionID, id)
    }

    fun select(id:Int): LiveData<Card> {
        return cardDao.select(id)
    }

    // Folder
    suspend fun insert(folder:Folder) : Long {
        Log.d("woops", "did it work?")
        return folderDao.insert(folder)
    }

    fun getLastRow(): LiveData<Folder> {
        return folderDao.getLastRow()
    }

    fun findFolder(id:Int): LiveData<Folder> {
        return folderDao.findFolder(id)
    }


}


//suspend fun update(name:String,
//                   mana:String,
//                   manaCon:Int,
//                   cardType:String,
//                   rulesText:String,
//                   quantity:Float,
//                   scryfallURI:String,
//                   imageURI:String,
//                   collectionID:Int,
//                   id:Int) {
//    cardDao.update(name, mana, manaCon, cardType, rulesText, quantity, scryfallURI, imageURI, collectionID, id)
//}

//
//    fun selectAllCardsFromCollection(id:Int):Flow<List<Card>> = cardCollectionDao.selectAllCardsFromCollection(id)
//
//
//    suspend fun insert(card:Card){
//        cardFolderDao.insert(card)
//    }
//    suspend fun delete(id:Int) {
//        cardCollectionDao.delete(id)
//    }
//    suspend fun update(title:String,
//                       content:String,
//                       reflection:String,
//                       emotion:String,
//                       date:String,
//                       id:Int) {
//        cardDao.update(title, content, reflection, emotion, date, id)
//    }

//    fun select(id:Int): LiveData<Card> {
//        return cardCollectionDao.select(id)
//    }
//
//    fun selectAllCards(collectionId:Int): LiveData<Card> {
//        return cardCollectionDao.select(collectionId)
//    }

    // Folder
//    suspend fun insert(folder:Folder) {
//        collectionDao.insert(folder)
//    }


//    suspend fun insert(folder:Folder) {
//        cardFolderDao.insert(folder)
//    }
//    fun getLastRow(): Int {
//        return collectionDao.getLastRow()
//    }
