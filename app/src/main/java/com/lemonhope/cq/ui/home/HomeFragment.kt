package com.lemonhope.cq.ui.home


import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.lemonhope.cq.R
import com.lemonhope.cq.adapters.ViewPagerAdapter
import com.lemonhope.cq.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var mAdapter: ViewPagerAdapter
    private lateinit var mViewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewPager = view.findViewById(R.id.viewpager)
        mAdapter = ViewPagerAdapter(
            mutableListOf(),
            requireContext(),
            mViewPager
        )
        mAdapter.initDB()
        mAdapter.getNextSetQuotes()
        mViewPager.offscreenPageLimit = 3
        mViewPager.setPageTransformer(SliderTransformer(3))
        mViewPager.adapter = mAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class SliderTransformer(private val offscreenPageLimit: Int) : ViewPager2.PageTransformer {

        companion object {

            private const val DEFAULT_TRANSLATION_X = .0f
            private const val DEFAULT_TRANSLATION_FACTOR = 1.2f

            private const val SCALE_FACTOR = .14f
            private const val DEFAULT_SCALE = 1f

            private const val ALPHA_FACTOR = .3f
            private const val DEFAULT_ALPHA = 1f

        }

        override fun transformPage(page: View, position: Float) {

            page.apply {

                ViewCompat.setElevation(page, -kotlin.math.abs(position))

                val scaleFactor = -SCALE_FACTOR * position + DEFAULT_SCALE
                val alphaFactor = -ALPHA_FACTOR * position + DEFAULT_ALPHA

                when {
                    position <= 0f -> {
                        translationX = DEFAULT_TRANSLATION_X
                        scaleX = DEFAULT_SCALE
                        scaleY = DEFAULT_SCALE
                        alpha = DEFAULT_ALPHA + position
                    }
                    position <= offscreenPageLimit - 1 -> {
                        scaleX = scaleFactor
                        scaleY = scaleFactor
                        translationX = -(width / DEFAULT_TRANSLATION_FACTOR) * position
                        alpha = alphaFactor
                    }
                    else -> {
                        translationX = DEFAULT_TRANSLATION_X
                        scaleX = DEFAULT_SCALE
                        scaleY = DEFAULT_SCALE
                        alpha = DEFAULT_ALPHA
                    }
                }
            }
        }
    }
}