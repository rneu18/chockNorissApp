package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.joke_list.view.*

class MyAdapter(private val items : MutableList<String>, private val context: Context) : RecyclerView.Adapter<ViewHolder>() {

    // Gets the number of jokes in the list
    override fun getItemCount(): Int {
        //println("111111111111111111111111111111 ${items.size}")
        return items.size
    }

    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.joke_list, parent, false))
    }

    // Binds each joke in the List to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        println("1111111111111111111111111111111111 ${items[position]}")

        holder.joke.text = items[position]
    }
}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each joke to
    val joke: TextView = view.findViewById(R.id.my_jokes_list)
    //val cardView: CardView = view.findViewById(R.id.myCard)
}