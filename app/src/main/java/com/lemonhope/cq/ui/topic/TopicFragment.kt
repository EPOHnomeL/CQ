package com.lemonhope.cq.ui.topic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.lemonhope.cq.databinding.FragmentTopicBinding

class TopicFragment : Fragment() {

    private var _binding: FragmentTopicBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val topicViewModel =
            ViewModelProvider(this).get(TopicViewModel::class.java)

        _binding = FragmentTopicBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textTopic
        topicViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}