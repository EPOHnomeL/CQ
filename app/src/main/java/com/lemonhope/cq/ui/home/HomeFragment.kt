package com.lemonhope.cq.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lemonhope.cq.R
import com.lemonhope.cq.adapters.ViewPagerAdapter
import com.lemonhope.cq.databinding.FragmentHomeBinding
import com.lemonhope.cq.models.Quote
import com.lemonhope.cq.models.QuoteModel
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var mAdapter: ViewPagerAdapter
    private lateinit var mViewPager: ViewPager
    private lateinit var quoteArrayList: ArrayList<Quote>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val config =
//            RealmConfiguration.Builder(schema = setOf(QuoteModel::class)).compactOnLaunch().initialData(QuoteModel()).build()
//        val realm: Realm = Realm.open(config)
        val quotes = ArrayList<QuoteModel>()
//        val res: RealmResults<QuoteModel> = realm.query<QuoteModel>().find()
//        quotes.addAll(realm.copyFromRealm(res))
        //initData()
        mViewPager = view.findViewById(R.id.viewpager)
        mAdapter = ViewPagerAdapter(quotes, context)
        mViewPager.setPageTransformer(true, ViewPagerStack())
        mViewPager.adapter = mAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initData() {
        val gson = Gson()
        val text = resources.openRawResource(R.raw.quotes2).bufferedReader().use { it.readText() }
        val quoteType = object : TypeToken<ArrayList<Quote>>() {}.type
        quoteArrayList = gson.fromJson(text, quoteType)


    }

    class ViewPagerStack : ViewPager.PageTransformer {
        override fun transformPage(page: View, position: Float) {
            if (position >= 0) {
                page.scaleY = (0.88f - 0.05f * position)
                page.scaleX = 1f
                page.translationX = -page.width * position
                page.translationY = position - 120
            }
        }
    }
}