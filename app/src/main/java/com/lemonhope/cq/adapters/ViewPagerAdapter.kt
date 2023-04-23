package com.lemonhope.cq.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.lemonhope.cq.R
import kotlin.collections.List
import com.lemonhope.cq.models.Quote
import com.lemonhope.cq.models.QuoteModel

class ViewPagerAdapter(private val contents: ArrayList<QuoteModel>, private val context: Context?) : PagerAdapter() {


    override fun getCount(): Int {
        return contents.size;
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == (`object` as LinearLayout)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val inflater: LayoutInflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var view: View = inflater.inflate(R.layout.row_home, container, false)
        container.addView(view)

        val textQuote: TextView = view.findViewById(R.id.row_home_text_quote)
        val textTopic: TextView = view.findViewById(R.id.row_home_text_topic)
        val imgCopy: ImageView = view.findViewById(R.id.row_home_img_copy)
        val imgFavourite: ImageView = view.findViewById(R.id.row_home_img_fav)

        val currentItem = contents[position]
        textQuote.text = currentItem.toString()
        textTopic.text = "Holy"
        imgCopy.setImageResource(R.drawable.ic_copy)
        imgFavourite.setImageResource(R.drawable.ic_favourite_empty)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as  View)
    }
}
