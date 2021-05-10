package com.example.androidmtgcompanionkotlin

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject


class CardListActivity: AppCompatActivity() {


    private lateinit var recyclerView: RecyclerView

    //private lateinit var autoComplete: AutoCompleteTextView
    private lateinit var buttonAdd: Button
    private var collectionID:Int = -1
    private val cardFolderViewModel: CardFolderViewModel by viewModels {
        CardFolderViewModelFactory((application as CardFolderApplication).repository)
    }

    private lateinit var dialogBuilder : AlertDialog.Builder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        recyclerView = findViewById(R.id.recyclerview)
        val intent = intent

        collectionID = cardFolderViewModel.insertedId.toInt()
        if (collectionID==-1) {
            cardFolderViewModel.getLastRow().observe(this, {item->
                collectionID = item.id
                cardFolderViewModel.selectedFolder.value = item
            })
        }


        // AutoComplete
        val jsonFileContent = loadJSONfromAssets("allCards.JSON")
        val jsonObject = JSONObject(jsonFileContent)
        val jsonArray = jsonObject.getJSONArray("data")
        var strArray: Array<String?> = arrayOfNulls(jsonObject.getInt("total_values"))
        for (i in 0 until jsonArray.length()) {
            strArray[i] = jsonArray.getString(i)
        }
        val arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, strArray)

        buttonAdd = findViewById(R.id.button_add)


        buttonAdd.setOnClickListener{
            var autoComplete = AutoCompleteTextView(this);
            autoComplete.hint = "Type Card Name Here"
            autoComplete.threshold = 1 //will start working from first character
            autoComplete.setAdapter(arrayAdapter) //setting the adapter data into the AutoCompleteTextView


                dialogBuilder = AlertDialog.Builder(this).setTitle(R.string.dialog_addCardMsg)
                .setSingleChoiceItems(R.array.scan, 0)
                { dialog, which ->
                    // camera
                    val intent = Intent(this@CardListActivity, CameraActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .setView(autoComplete)
                .apply {
                    setPositiveButton(
                        R.string.ok
                    ) { dialog, id ->
                        val mainHandler = Handler(Looper.getMainLooper())
                        val myRunnable = Runnable {
                            //Code that uses AsyncHttpClient in your case ConsultaCaract()
                            val name: String = (autoComplete.getText().toString())
                            if (!name.isEmpty()) {
                                val api_searchName = "https://api.scryfall.com/cards/named?exact="
                                val client = AsyncHttpClient()
                                val apiUpdated = api_searchName + name.replace(" ", "+")
                                Log.d("api input", apiUpdated)
                                client.get(apiUpdated,
                                    object : AsyncHttpResponseHandler() {
                                        override fun onSuccess(
                                            statusCode: Int,
                                            headers: Array<Header>,
                                            responseBody: ByteArray
                                        ) {

                                            val mtg = JSONObject(String(responseBody))

                                            Log.d("api response", String(responseBody))
                                            if (mtg.getString("object").equals("card")) {
                                                Log.d("api we did it", mtg.getString("name"))
                                                val card = Card(
                                                    mtg.getString("name"),
                                                    mtg.getString("mana_cost"),
                                                    mtg.getDouble("cmc").toFloat(),
                                                    mtg.getString("type_line"),
                                                    mtg.getString("oracle_text"),
                                                    1,
                                                    mtg.getString("scryfall_uri"),
                                                    mtg.getJSONObject("image_uris").getString("normal"),
                                                    collectionID
                                                )
                                                cardFolderViewModel.insert(card)
                                                //collectionID)
                                                //cardDao.insert(card)
                                            }
                                            Log.d("api response", String())
                                        }

                                        override fun onFailure(
                                            statusCode: Int,
                                            headers: Array<Header>,
                                            responseBody: ByteArray,
                                            error: Throwable
                                        ) {
                                            Log.e("api error", String(responseBody))
                                        }
                                    })

                            }
                            else {
                                toastInput("No name was entered, please try again.")
                            }
                        }
                        mainHandler.post(myRunnable)
                        dialog.dismiss()
                    }
                    setNegativeButton(
                        R.string.cancel
                    ) { dialog, id ->
                        dialog.cancel()
                    }
                }
            val alert = dialogBuilder.create()
            alert.show()
//            class Card(@ColumnInfo(name="name") var name:String,
//                       @ColumnInfo(name="mana") var mana:String,
//                       @ColumnInfo(name="manaCon") var manaCon:String,
//                       @ColumnInfo(name="type") var type:String,
//                       @ColumnInfo(name="text") var text:String,
//                       @ColumnInfo(name="flavorText") var flavorText:String,
//                       @ColumnInfo(name ="quantity") var quantity:Int,
//                       @ColumnInfo(name = "collectionID") var collectionID:Int)

        }

        val adapter = CardListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        cardFolderViewModel.cardsOfSelectedFolder.observe(this, Observer {
            // update the cached copy of tasks in the adapter to it
                cards -> cards?.let{
            adapter.submitList(it)
        }
        })

       //ardCollectionViewModel.selectAllCardsFromCollection(intent.getIntExtra)

//        var cards = cardCollectionViewModel.selectAllCardsFromCollection(intent.getIntExtra("id", -1))


//        cards.observe(this, {
//                cards -> cards?.let{
//            adapter.submitList(it)
//        }
//        })
//        val adapter = ListAdapter()
//        recyclerView.adapter = adapter
//        recyclerView.layoutManager = LinearLayoutManager(this)
//
//        CardCollectionViewModel.allCards.observe(this, Observer {
//            // update the cached copy of tasks in the adapter to it
//                cards -> cards?.let{
//            adapter.submitList(it)
//        }
//        })










        //Getting the instance of AutoCompleteTextView

//        autoComplete.threshold = 1 //will start working from first character
//        autoComplete.setAdapter(adapter) //setting the adapter data into the AutoCompleteTextView
//        autoComplete.setOnItemClickListener { parent, view, position, id ->
//            autoComplete.setSelection(position)
//        }
    }



    private fun loadJSONfromAssets(filename: String): String {
        return applicationContext.assets.open(filename).bufferedReader().use { it.readText() }
    }
    private fun toastInput(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}


        //            val villagerJSONObject = jsonArray.getJSONObject(i)
//            // extract name, birthday, phrase
//
//            val name:String = villagerJSONObject.get("name").toString()
//            val birthday:String = villagerJSONObject.get("birthday").toString()
//            val phrase:String = villagerJSONObject.get("phrase").toString()
//            val houseURL:String = villagerJSONObject.get("house").toString()
//            val villagerURL:String = villagerJSONObject.get("villager").toString()
//
//            val villagerObject:Villager = Villager(name, birthday, phrase, houseURL, villagerURL, true)
//            villagersList.add(villagerObject)
//            val item = parent.adapter.getItem(position) as String
//            isSelect = true
//            autoComplateText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.remove, 0)
//            Log.e("Item: ", item)
//            autoComplateText.setInputType(InputType.TYPE_NULL)
//            (getSystemService<Any>(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
//                autoComplateText.getWindowToken(),
//                0
//            )



//        AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                String item = (String) parent.getAdapter().getItem(position);
//                isSelect = true;
//
//                autoComplateText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.remove, 0);
//
//                Log.e("Item: ", item);
//                autoComplateText.setInputType(InputType.TYPE_NULL);
//
//                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(autoComplateText.getWindowToken(), 0);
//
//            }
//
//            AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                String item = (String) parent.getAdapter().getItem(position);
//                isSelect = true;
//
//                autoComplateText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.remove, 0);
//
//                Log.e("Item: ", item);
//                autoComplateText.setInputType(InputType.TYPE_NULL);
//
//                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(autoComplateText.getWindowToken(), 0);
//
//            }
//        };

//        autoComplete.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                int DRAWABLE_RIGHT = 2;
//
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//
//                    if (autoComplateText != null){
//                        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(autoComplateText.getWindowToken(), 0);
//
//                        if (event.getRawX() >= (autoComplateText.getRight() - autoComplateText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
//                            autoComplateText.setText("");
//                            autoComplateText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                            isSelect = false;
//                            return true;
//                        }
//                    }
//
//
//
//                }
//
//                return false;
//            }
//        });



//    }
//    internal class Validator : AutoCompleteTextView.Validator {
//        override fun isValid(text: CharSequence): Boolean {
//            Log.v("Test", "Checking if valid: $text")
//            Arrays.sort(validWords)
//            return if (Arrays.binarySearch(validWords, text.toString()) > 0) {
//                true
//            } else false
//        }
//
//        override fun fixText(invalidText: CharSequence): CharSequence {
//            Log.v("Test", "Returning fixed text")
//
//            /* I'm just returning an empty string here, so the field will be blanked,
//             * but you could put any kind of action here, like popping up a dialog?
//             *
//             * Whatever value you return here must be in the list of valid words.
//             */return ""
//        }
//    }

