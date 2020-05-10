package com.fristano.redditfeedchallenge.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.fristano.redditfeedchallenge.databinding.FragmentFeedBinding
import com.fristano.redditfeedchallenge.model.Post
import com.fristano.redditfeedchallenge.viewmodel.FeedViewModel


class FragmentFeed : Fragment() {

    private lateinit var _binding: FragmentFeedBinding
    private lateinit var _viewModel: FeedViewModel
    private var _feedListAdapter: FeedListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val vm: FeedViewModel by viewModels()
        _viewModel = vm
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentFeedBinding.inflate(layoutInflater)

        _binding.lifecycleOwner = this
        bind()
        observeViewModel()
        _viewModel.fetchTop()
        return _binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        removeObserveViewModel()
    }

    private fun bind() {

    }


    private fun showLoading(show: Boolean) {

    }


    private fun selectPost(post: Post) {
        _viewModel.selectPost(post)
        _binding.frgFeedSlidingPane.closePane()
        _feedListAdapter?.notifyDataSetChanged()
    }

    // --- ViewModel Observes
    private fun observeViewModel() {
        _viewModel.showLoading.observe(viewLifecycleOwner, Observer {
            showLoading(it)
        })
        _viewModel.listItemSelected.observe(viewLifecycleOwner, Observer {
            _binding.frgFeedSlidingPane.closePane()
        })

    }

    private fun removeObserveViewModel() {
        _viewModel.showLoading.removeObservers(viewLifecycleOwner)
        _viewModel.listItemSelected.removeObservers(viewLifecycleOwner)
    }

}