package com.example.androidmtgcompanionkotlin


import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.squareup.picasso.Picasso
import cz.msebera.android.httpclient.Header
import org.json.JSONObject


class FolderListAdapter: ListAdapter<Folder,FolderListAdapter.FolderViewHolder>(FolderComparator()) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        return FolderViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        val currentFolder = getItem(position)
        holder.bindText(currentFolder.name, holder.textViewName)
        Picasso.get().load("file:///android_asset/folderIcon.png").into(holder.imageViewImage)


        holder.checkBoxFavorite.isChecked = currentFolder.favorite

        //holder.bindImage(currentFolder.quantity.toString(), holder.textViewQuantity)

//        holder.imageViewImage.setOnClickListener {
//            val intent = Intent(holder.imageViewImage.context, CardListActivity::class.java)
//            intent.putExtra("id", currentFolder.id)
//            holder.textViewName.context.startActivity(intent)
//        }
        holder.textViewName.setOnClickListener {

            val intent = Intent(holder.textViewName.context, CardListActivity::class.java)
            intent.putExtra("collectionID", currentFolder.id)
            intent.putExtra("fromFolderList", true)
            intent.putExtra("name", currentFolder.name)
            holder.textViewName.context.startActivity(intent)

        }

        holder.checkBoxFavorite.setOnClickListener { view: View? ->
            currentFolder.favorite = holder.checkBoxFavorite.isChecked
            this.notifyItemChanged(position)
        }
    }



    class FolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.textView_itemFolderName)
        val imageViewImage: ImageView = itemView.findViewById(R.id.imageView_itemFolderImage)
        val checkBoxFavorite: CheckBox = itemView.findViewById(R.id.checkBox_itemFavorite)
        //val textViewQuantity: TextView = itemView.findViewById(R.id.textView_itemQuantity)


        fun bindText(text: String?, textView: TextView) {
            textView.text = text
        }



        //        searchView_filter.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                // your text view here
//                filteredBrews.getFiltered();
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                textView.setText(query);
//                return true;
//            }
//        });


        companion object {
            fun create(parent: ViewGroup): FolderViewHolder {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.item_folder, parent, false)
                return FolderViewHolder(view)
            }
        }
    }

    class FolderComparator : DiffUtil.ItemCallback<Folder>() {
        override fun areContentsTheSame(oldItem: Folder, newItem: Folder): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areItemsTheSame(oldItem: Folder, newItem: Folder): Boolean {
            return oldItem === newItem
        }
    }
}

//class CardListAdapter : ListAdapter<Card, CardListAdapter.CardViewHolder>(CardComparator()) {
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
//        return CardViewHolder.create(parent)
//    }
//
//    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
//        val currentCard = getItem(position)
//        holder.bindText(currentCard.name, holder.textViewName)
//        holder.bindText(currentCard.quantity.toString(), holder.textViewQuantity)
//        holder.textViewName.setOnClickListener{
//            val intent = Intent(holder.textViewName.context, CardListActivity::class.java)
//            intent.putExtra("id", currentCard.id)
//            holder.textViewName.context.startActivity(intent)
//
//        }
//    }
//
//    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val textViewName: TextView = itemView.findViewById(R.id.textView_itemName)
//        val textViewQuantity: TextView = itemView.findViewById(R.id.textView_itemQuantity)
//
//        fun bindText(text:String?, textView:TextView) {
//            textView.text = text
//        }
//
//        companion object{
//            fun create (parent: ViewGroup) : CardViewHolder {
//                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
//                return CardViewHolder(view)
//            }
//        }
//    }
//
//    class CardComparator : DiffUtil.ItemCallback<Card>(){
//
//        override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean {
//            return oldItem.name == newItem.name
//        }
//        override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean {
//            return oldItem === newItem
//        }
//    }
//}