package com.lemonhope.cq.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lemonhope.cq.R
import com.lemonhope.cq.adapters.HomeAdapter
import com.lemonhope.cq.databinding.FragmentHomeBinding
import com.lemonhope.cq.models.Quote

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: HomeAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var quoteArrayList: ArrayList<Quote>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            //textView.text = it
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        recyclerView = view.findViewById(R.id.home_recycler_view)
        adapter = HomeAdapter(quoteArrayList)
        recyclerView.adapter = adapter
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initData() {

        val gson = Gson()
        val text = resources.openRawResource(R.raw.test_quotes)
            .bufferedReader().use { it.readText() }
        val quoteType = object : TypeToken<ArrayList<Quote>>() {}.type
        quoteArrayList = gson.fromJson(text, quoteType)
    }
}