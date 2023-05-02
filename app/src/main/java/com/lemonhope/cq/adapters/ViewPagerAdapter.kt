package com.lemonhope.cq.adapters

import android.content.Context
import android.content.res.Resources.Theme
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.icu.util.Calendar
import android.provider.CalendarContract.Colors
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.lemonhope.cq.Database
import com.lemonhope.cq.R
import com.lemonhope.cq.models.Quote
import com.lemonhope.cq.models.QuoteModel
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.random.Random

class ViewPagerAdapter(private val context: Context?, private val viewPager: ViewPager) : PagerAdapter() {
    private var randInts: MutableList<Int> = mutableListOf()
    private var once = false;
    private var contents: MutableList<QuoteModel> = mutableListOf()
    private val results: RealmResults<QuoteModel> = Database.getInstance(context!!.resources).query<QuoteModel>().find()
    val random = Random(LocalDate.now().toEpochDay().toInt())

    override fun getCount(): Int {
        return contents.size;
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == (`object` as ConstraintLayout)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val inflater: LayoutInflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.row_home, container, false)
        container.addView(view)

        val textQuote: TextView = view.findViewById(R.id.row_home_text_quote)
        val textTopic: TextView = view.findViewById(R.id.row_home_text_topic)
        val card: CardView = view.findViewById(R.id.row_home_card_view)
        Log.i("Position", position.toString())
        Log.i("Count", count.toString())
        if(position == count-1){
            viewPager.post(runnable)
        }


        val colors: ArrayList<Int> = arrayListOf(Color.rgb(140, 227, 222))
        val currentItem = contents[position]
        textQuote.text = currentItem.toString()
        var s: String = ""
        for(i in 0 until currentItem.topics.size){
            s = s + currentItem.topics[i] + if(i != currentItem.topics.size-1) ", " else ""
            val c = Color.rgb(random.nextInt(), random.nextInt(), random.nextInt())
            colors.add(c)
        }
        val grad = GradientDrawable(GradientDrawable.Orientation.BR_TL, colors.toIntArray())
        grad.cornerRadius = context.resources.displayMetrics.density * 10f
        card.setBackgroundDrawable(grad)

        textTopic.text = s
        return view
    }

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
        notifyDataSetChanged()
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as  View)
    }
}
