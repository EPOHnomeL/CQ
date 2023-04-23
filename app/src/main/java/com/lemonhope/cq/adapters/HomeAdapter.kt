package com.lemonhope.cq.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lemonhope.cq.R
import com.lemonhope.cq.models.Quote

class HomeAdapter(private val quoteList: ArrayList<Quote>) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_home, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val currentItem = quoteList[position]
        print(currentItem)
        viewHolder.textQuote.text = currentItem.toString()
        viewHolder.textTopic.text = currentItem.topic.toString()
        viewHolder.imgCopy.setImageResource(R.drawable.ic_copy)
        viewHolder.imgFavourite.setImageResource(R.drawable.ic_favourite_empty)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = quoteList.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textQuote: TextView = view.findViewById(R.id.row_home_text_quote)
        val textTopic: TextView = view.findViewById(R.id.row_home_text_topic)
        val imgCopy: ImageView = view.findViewById(R.id.row_home_img_copy)
        val imgFavourite: ImageView = view.findViewById(R.id.row_home_img_fav)
    }

}
