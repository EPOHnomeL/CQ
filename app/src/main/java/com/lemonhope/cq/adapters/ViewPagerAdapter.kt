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

    private lateinit var results: RealmResults<QuoteModel>
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
            val c = Color.rgb(random.nextInt(), random.nextInt(), random.nextInt())
            colors.add(c)
        }
        val grad = GradientDrawable(GradientDrawable.Orientation.BR_TL, colors.toIntArray())
        grad.cornerRadius = 50f
        holder.card.setBackgroundDrawable(grad)

        holder.textTopic.text = s

        holder.favImg.setImageResource(if (!currentItem.favourite) R.drawable.ic_favourite_empty else R.drawable.ic_favourite)

        holder.favImg.setOnClickListener {
            Database.getInstance(context.resources).writeBlocking {
                val q: QuoteModel? = this.query<QuoteModel>("_id == $0", currentItem._id).first().find()
                q?.favourite = !currentItem.favourite
                Log.i("worked", "YEAH")
            }
            currentItem =  Database.getInstance(context.resources).query<QuoteModel>("_id == $0", currentItem._id).first().find()!!
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


//    private var randInts: MutableList<Int> = mutableListOf()
//    private var once = false;
////    private var contents: MutableList<QuoteModel> = mutableListOf()
//    private lateinit var results: RealmResults<QuoteModel>
//    val random = Random(LocalDate.now().toEpochDay().toInt())
//
//    override fun getCount(): Int {
//        return contents.size;
//    }
//
//    override fun isViewFromObject(view: View, `object`: Any): Boolean {
//        return view == (`object` as ConstraintLayout)
//    }
//
    fun initDB(){
        results = Database.getInstance(context.resources).query<QuoteModel>().find()
    }
//
//    override fun instantiateItem(container: ViewGroup, position: Int): Any {
//
//        val inflater: LayoutInflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        val view: View = inflater.inflate(R.layout.row_home, container, false)
//        container.addView(view)
//
//        val currentItem = contents[position]
//
//        val textQuote: TextView = view.findViewById(R.id.row_home_text_quote)
//        val textTopic: TextView = view.findViewById(R.id.row_home_text_topic)
//        val card: CardView = view.findViewById(R.id.row_home_card_view)
//        val copyImg: ImageView = view.findViewById(R.id.row_home_img_copy)
//
//        copyImg.setOnClickListener {
//            // Set quote as clipboard
//            val clipboardManager =
//                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//            val myClip = ClipData.newPlainText("quote", currentItem.toString())
//            clipboardManager.setPrimaryClip(myClip)
//
//            // Show copies to clipboard
//            val toast =
//                Toast.makeText(context.applicationContext, "Copied to Clipboard...", Toast.LENGTH_SHORT)
//            toast.show()
//        }
//
//        Log.i("Position", position.toString())
//        Log.i("Count", count.toString())
//        if(position == count-1){
//            viewPager.post(runnable)
//        }
//
//        val colors: ArrayList<Int> = arrayListOf(Color.rgb(140, 227, 222))
//
//        textQuote.text = currentItem.toString()
//        var s: String = ""
//        for(i in 0 until currentItem.topics.size){
//            s = s + currentItem.topics[i] + if(i != currentItem.topics.size-1) ", " else ""
//            val c = Color.rgb(random.nextInt(), random.nextInt(), random.nextInt())
//            colors.add(c)
//        }
//        val grad = GradientDrawable(GradientDrawable.Orientation.BR_TL, colors.toIntArray())
//        grad.cornerRadius = context.resources.displayMetrics.density * 10f
//        card.setBackgroundDrawable(grad)
//
//        textTopic.text = s
//        return view
//    }
//
    private val runnable = Runnable{
        getNextSetQuotes()
    }

    fun getNextSetQuotes() {
        var i = kotlin.math.abs(random.nextInt() % results.size)
        for (n in 0..2) {
//            randInts.add(i)
            contents.add(results[i])
            i = kotlin.math.abs(random.nextInt() % results.size)
        }
        notifyItemRangeInserted(contents.size-3, contents.size)
    }
//
//    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//        container.removeView(`object` as  View)
//    }
}
