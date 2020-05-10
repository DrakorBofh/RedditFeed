package com.fristano.redditfeedchallenge.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fristano.redditfeedchallenge.databinding.FragmentFeedDetailBinding
import com.fristano.redditfeedchallenge.util.FileHelper
import com.fristano.redditfeedchallenge.viewmodel.FeedViewModel


class FragmentFeedDetail : Fragment() {

    private lateinit var _binding: FragmentFeedDetailBinding
    private var _viewModel: FeedViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _viewModel = parentFragment?.let { ViewModelProvider(it).get(FeedViewModel::class.java) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentFeedDetailBinding.inflate(layoutInflater)

        _binding.lifecycleOwner = this
        bind()
        observeViewModel()
        return _binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        removeObserveViewModel()
    }

    private fun bind() {
        _binding.postDetailImage.setOnClickListener {view ->
            context?.let { FileHelper.imageDownload(it, _viewModel?.currentSelectedPost?.value?.thumbnail)  }
        }
    }

    private fun showLoading(show: Boolean) {}

    // --- ViewModel Observes
    private fun observeViewModel() {
        _viewModel?.showLoading?.observe(viewLifecycleOwner, Observer {
            showLoading(it)
        })

        _viewModel?.currentSelectedPost?.observe(viewLifecycleOwner, Observer {post ->
            _binding.post = post
        })

    }

    private fun removeObserveViewModel() {
        _viewModel?.showLoading?.removeObservers(viewLifecycleOwner)
        _viewModel?.currentSelectedPost?.removeObservers(viewLifecycleOwner)
    }


}