package com.lemonhope.cq.ui.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lemonhope.cq.Database
import com.lemonhope.cq.R
import com.lemonhope.cq.adapters.FavouriteAdapter
import com.lemonhope.cq.databinding.FragmentFavouriteBinding
import com.lemonhope.cq.models.QuoteModel
import io.realm.kotlin.ext.query

class FavouriteFragment : Fragment() {

    private var _binding: FragmentFavouriteBinding? = null
    private lateinit var adapter: FavouriteAdapter
    private lateinit var recyclerView: RecyclerView

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val favouriteViewModel =
            ViewModelProvider(this)[FavouriteViewModel::class.java]

        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.fav_rv)
        adapter = FavouriteAdapter(
            Database.getInstance().query<QuoteModel>("favourite == true").find(),
            requireContext()
        )
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext());
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}