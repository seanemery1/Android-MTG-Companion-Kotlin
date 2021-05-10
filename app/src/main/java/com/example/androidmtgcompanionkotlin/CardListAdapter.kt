package com.example.androidmtgcompanionkotlin


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView


class CardListAdapter : ListAdapter<Card, CardListAdapter.CardViewHolder>(CardComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val currentCard = getItem(position)
        holder.bindText(currentCard.name, holder.textViewName)
        holder.bindText(currentCard.quantity.toString(), holder.textViewQuantity)
        holder.textViewName.setOnClickListener {

            val intent = Intent(holder.textViewName.context, ViewCardActivity::class.java)
            intent.putExtra("id", currentCard.id)
            holder.textViewName.context.startActivity(intent)

        }
    }

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.textView_itemCollectionName)
        val textViewQuantity: TextView = itemView.findViewById(R.id.textView_itemQuantity)

        fun bindText(text: String?, textView: TextView) {
            textView.text = text
        }

        companion object {
            fun create(parent: ViewGroup): CardViewHolder {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
                return CardViewHolder(view)
            }
        }
    }

    class CardComparator : DiffUtil.ItemCallback<Card>() {

        override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean {
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