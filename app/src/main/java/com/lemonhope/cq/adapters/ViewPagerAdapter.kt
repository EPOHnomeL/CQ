package com.lemonhope.cq.adapters

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.RadialGradient
import android.graphics.Shader
import android.graphics.drawable.GradientDrawable
import android.provider.ContactsContract.Data
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.getSystemService
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.lemonhope.cq.Database
import com.lemonhope.cq.R
import com.lemonhope.cq.models.QuoteModel
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import java.time.LocalDate
import kotlin.random.Random

class ViewPagerAdapter(
    private val contents: MutableList<QuoteModel>,
    private val context: Context,
    private val viewPager: ViewPager2
) : RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {

    private val random = Random(LocalDate.now().toEpochDay().toInt())

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textQuote: TextView
        val textTopic: TextView
        val card: CardView
        val copyImg: ImageView
        val favImg: ImageView

        init {
            // Define click listener for the ViewHolder's View
            textQuote = view.findViewById(R.id.row_home_text_quote)
            textTopic = view.findViewById(R.id.row_home_text_topic)
            card = view.findViewById(R.id.row_home_card_view)
            copyImg = view.findViewById(R.id.row_home_img_copy)
            favImg = view.findViewById(R.id.row_home_img_fav)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_home, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(position == itemCount-1){
            viewPager.post(runnable)
        }
        var currentItem = contents[position]


        val colors: ArrayList<Int> = arrayListOf(Color.rgb(117, 189, 185))

        holder.textQuote.text = currentItem.toString()
        var s: String = ""
        for (i in 0 until currentItem.topics.size) {
            s = s + currentItem.topics[i] + if (i != currentItem.topics.size - 1) ", " else ""
            colors.add(1, Database.topicToColor(currentItem.topics[i]))
        }
        val grad = GradientDrawable(GradientDrawable.Orientation.BR_TL, colors.toIntArray())
        grad.cornerRadius = 50f
        holder.card.setBackgroundDrawable(grad)

        holder.textTopic.text = s

        holder.favImg.setImageResource(if (!currentItem.favourite) R.drawable.ic_favourite_empty else R.drawable.ic_favourite)

        holder.favImg.setOnClickListener {
            Database.getInstance().writeBlocking {
                val q: QuoteModel? = this.query<QuoteModel>("_id == $0", currentItem._id).first().find()
                q?.favourite = !currentItem.favourite
            }
            currentItem =  Database.getInstance().query<QuoteModel>("_id == $0", currentItem._id).first().find()!!
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
        return contents.size;
    }


    private val runnable = Runnable{
        getNextSetQuotes()
    }

    fun getNextSetQuotes() {
        var i = kotlin.math.abs(random.nextInt()% 11018)
        for (n in 0..2) {
//            randInts.add(i)
            contents.add(Database.getInstance().query<QuoteModel>("index == $0", i).first().find()!!)
            i = kotlin.math.abs(random.nextInt() % 11018)
        }
        notifyItemRangeInserted(contents.size-3, contents.size)
    }
}
