package com.lemonhope.cq.adapters

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.lemonhope.cq.Database
import com.lemonhope.cq.R
import com.lemonhope.cq.models.Quote
import com.lemonhope.cq.models.QuoteModel
import io.realm.kotlin.ext.query

class FavouriteAdapter(
    private val contents: List<QuoteModel>,
    private val context: Context
) :
    RecyclerView.Adapter<FavouriteAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val textQuote: TextView
        val textAuthor: TextView
        val card: CardView
        val copyImg: ImageView
        val favImg: ImageView

        init {
            textQuote = view.findViewById(R.id.row_fav_quote)
            textAuthor = view.findViewById(R.id.row_fav_author)
            card = view.findViewById(R.id.row_fav_card)
            copyImg = view.findViewById(R.id.row_fav_copy)
            favImg = view.findViewById(R.id.row_fav_fav)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_fav, parent, false)

        return FavouriteAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var currentItem = Database.getInstance().copyFromRealm(contents[position])

        val colors: ArrayList<Int> = arrayListOf()

        holder.textQuote.text = currentItem.quote
        holder.textAuthor.text = currentItem.author

        for (i in 0 until currentItem.topics.size) {
            colors.add(Database.topicToColor(currentItem.topics[i]))
        }

        if(colors.size >= 2) {
            val grad = GradientDrawable(GradientDrawable.Orientation.BR_TL, colors.toIntArray())
            grad.cornerRadius = 20f
            holder.card.setBackgroundDrawable(grad)
        } else {
           holder.card.setCardBackgroundColor(colors[0])
        }

        holder.favImg.setImageResource(if (!currentItem.favourite) R.drawable.ic_favourite_empty else R.drawable.ic_favourite)

        holder.favImg.setOnClickListener {
            Database.getInstance().writeBlocking {
                val q: QuoteModel? =
                    this.query<QuoteModel>("_id == $0", currentItem._id).first().find()
                q?.favourite = !currentItem.favourite
            }
            currentItem =
                Database.getInstance().query<QuoteModel>("_id == $0", currentItem._id).first()
                    .find()!!
            holder.favImg.setImageResource(if (!currentItem.favourite) R.drawable.ic_favourite_empty else R.drawable.ic_favourite)
            val toast =
                Toast.makeText(
                    context.applicationContext,
                    if (currentItem.favourite) "Added to favourites" else "Removed from favourites",
                    Toast.LENGTH_SHORT
                )
            toast.show()
        }

        holder.copyImg.setOnClickListener {
            // Set quote as clipboard
            val clipboardManager =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val myClip = ClipData.newPlainText("quote", currentItem.toString())
            clipboardManager.setPrimaryClip(myClip)

            // Show copies to clipboard
            val toast =
                Toast.makeText(
                    context.applicationContext,
                    "Copied to Clipboard...",
                    Toast.LENGTH_SHORT
                )
            toast.show()
        }
    }

    override fun getItemCount(): Int {
        return contents.size

    }
}