package com.example.androidmtgcompanionkotlin

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class CardFolderViewModel (private val repository: CardFolderRepository) : ViewModel() {


    // Filtering for cards in a deck/collection (folder)
    val selectedFolder = MutableLiveData<Folder>()

    val cardsOfSelectedFolder: LiveData<List<Card>>
        get() = Transformations.switchMap(selectedFolder) { folder ->
            val allCards = repository.allCards.asLiveData()
            val cards = when {
                folder == null -> allCards
                else -> {
                    Transformations.switchMap(allCards) { playerList ->
                        val filteredCards = MutableLiveData<List<Card>>()
                        val filteredList = playerList.filter { card -> card.collectionID == folder.id }
                        filteredCards.value = filteredList
                        filteredCards
                    }
                }
            }
            cards
        }

    // Filtering for decks/collections based on all, decks, collection (generic), or favorites

    //val selectedFolderCategory = MutableLiveData<Folder>()
    val selectedFolderCategory = MutableLiveData<Int>()



    val foldersOfSelectedCategory: LiveData<List<Folder>>
        get() = Transformations.switchMap(selectedFolderCategory) { category ->
            val allFolders = repository.allFolders.asLiveData()
            val folders = when {
                category == null -> allFolders
                else -> {
                    Transformations.switchMap(allFolders) { folderList ->
                        val filteredFolders = MutableLiveData<List<Folder>>()
                        val filteredList = folderList.filter { folder ->
                            when(category) {
                                1 -> folder.isDeck
                                2 -> !folder.isDeck
                                3 -> folder.favorite
                                else -> {
                                    folder.id != -1
                                }
                            }}
                        filteredFolders.value = filteredList
                        filteredFolders
                    }
                }
            }
            folders
        }


    val allFolders: LiveData<List<Folder>>
        get() = repository.allFolders.asLiveData()

    val allCards: LiveData<List<Card>> = repository.allCards.asLiveData()
    fun insert(card:Card) = viewModelScope.launch{
        repository.insert(card)
    }
    fun delete(id:Int) = viewModelScope.launch{
        repository.delete(id)
    }
    fun update(name:String,
               quantity:Int,
               collectionID:Int,
               id:Int) = viewModelScope.launch{
        repository.update(name, quantity, collectionID, id)
    }
    fun select(id:Int): LiveData<Card> = repository.select(id)


    // Folder
    var insertedId = -1L
    fun insert(folder:Folder) = viewModelScope.launch{
        insertedId = repository.insert(folder)
    }

    fun getLastRow(): LiveData<Folder> {
        return repository.getLastRow()
    }

    fun findFolder(id:Int): LiveData<Folder> {
        return repository.findFolder(id)
    }
}

class CardFolderViewModelFactory(private val repository: CardFolderRepository) : ViewModelProvider.Factory{

    override fun <D : ViewModel?> create(modelClass: Class<D>): D {
        if (modelClass.isAssignableFrom(CardFolderViewModel::class.java)) {
            return CardFolderViewModel(repository) as D
        }
        throw IllegalArgumentException("Unknown view model class")
    }
}


//fun update(name:String,
//           mana:String,
//           manaCon:Int,
//           cardType:String,
//           rulesText:String,
//           flavorText:String,
//           quantity:Int,
//           collectionID:Int,
//           id:Int) = viewModelScope.launch{
//    repository.update(name, mana, manaCon, cardType, rulesText,  flavorText, quantity, collectionID, id)
//}
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.viewModelScope
//import kotlinx.coroutines.launch
//import java.lang.IllegalArgumentException
//
//class CardFolderViewModel (private val repository: CardFolderRepository) : ViewModel() {
//
////    fun selectAllCardsFromCollection(id:Int): LiveData<List<Card>> = repository.selectAllCardsFromCollection(id).asLiveData()
////
////    var insertedId = -1L
//    fun insert(card:Card) = viewModelScope.launch {
//        repository.insert(card)
//
//    }
//
////        fun delete(id:Int) = viewModelScope.launch{
////            repository.delete(id)
////        }
////
////        fun select(id:Int): LiveData<Card> = repository.select(id)
//

//        //fun getLastRow(): Int = repository.getLastRow()
//
//}
//
//class CardFolderViewModelFactory(private val repository: CardFolderRepository) : ViewModelProvider.Factory{
//    // override the create method to return the TaskViewModel
//    override fun <D : ViewModel?> create(modelClass: Class<D>): D {
//        if (modelClass.isAssignableFrom(CardFolderViewModel::class.java)) {
//            return CardFolderViewModel(repository) as D
//        }
//        throw IllegalArgumentException("Unknown view model class")
//    }
//}