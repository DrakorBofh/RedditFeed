package com.fristano.redditfeedchallenge.view.ui

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fristano.redditfeedchallenge.databinding.FragmentFeedListBinding
import com.fristano.redditfeedchallenge.model.Post
import com.fristano.redditfeedchallenge.viewmodel.FeedViewModel
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import okhttp3.internal.notifyAll


class FragmentFeedList : Fragment() {

    private lateinit var _binding: FragmentFeedListBinding
    private var _viewModel: FeedViewModel? = null
    private var _feedListAdapter: FeedListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _viewModel = parentFragment?.let { ViewModelProvider(it).get(FeedViewModel::class.java) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentFeedListBinding.inflate(layoutInflater)

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
        _binding.postHeaderListButton.setOnClickListener {
            _binding.feedListRecycleView.itemAnimator = SlideInLeftAnimator()
            _feedListAdapter?.notifyItemRangeRemoved(0, _feedListAdapter?.itemCount ?: 0)
            _binding.feedListRecycleView.postOnAnimation {
                _viewModel?.removeAll()
            }
        }

        val layoutManager = LinearLayoutManager(
            _binding.root.context,
            LinearLayoutManager.VERTICAL,
            false
        )
        _binding.feedListRecycleView.layoutManager = layoutManager
        _binding.feedListRecycleView.setHasFixedSize(true)

        val dividerItemDecoration = DividerItemDecoration(context,layoutManager.orientation)
        _binding.feedListRecycleView.addItemDecoration(dividerItemDecoration)

        _feedListAdapter = FeedListAdapter(ArrayList(), object : FeedListAdapter.IFeedListAdapterAdapterCallback {
            override fun onClick(post: Post) {
                selectPost(post)
            }

            override fun onDismiss(post: Post) {
                val pos = _viewModel?.removePost(post)
                pos?.let {
                    _binding.feedListRecycleView.itemAnimator = SlideInLeftAnimator()
                    _feedListAdapter?.removeItem(pos)
                }
            }
        })

        _binding.feedListRecycleView.adapter = _feedListAdapter
        _binding.feedListSwipeRefresh.setOnRefreshListener {
            _feedListAdapter?.cleanContent()
            _viewModel?.fetchTop()
        }
        _binding.feedListRecycleView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount: Int = layoutManager.childCount
                val totalItemCount: Int = layoutManager.itemCount
                val firstVisibleItemPosition: Int = layoutManager.findFirstVisibleItemPosition()

                if (!_binding.feedListSwipeRefresh.isRefreshing &&
                    visibleItemCount + firstVisibleItemPosition >= totalItemCount &&
                    firstVisibleItemPosition >= 0 &&
                    totalItemCount >= PAGE_SIZE) {
                    _viewModel?.fetchNextPage()
                }

            }
        })
    }


    private fun showLoading(show: Boolean) {
        _binding.feedListSwipeRefresh.isRefreshing = show
    }


    private fun selectPost(post: Post) {
        _viewModel?.selectPost(post)
        _feedListAdapter?.notifyDataSetChanged()
    }

    // --- ViewModel Observes
    private fun observeViewModel() {
        _viewModel?.showLoading?.observe(viewLifecycleOwner, Observer {
            showLoading(it)
        })

        _viewModel?.currentTopPostList?.observe(viewLifecycleOwner, Observer {list ->
            if (list == null)
                _feedListAdapter?.cleanContent()
            else
                _feedListAdapter?.addContent(list)

            _feedListAdapter?.notifyDataSetChanged()
        })

    }

    private fun removeObserveViewModel() {
        _viewModel?.showLoading?.removeObservers(viewLifecycleOwner)
        _viewModel?.currentTopPostList?.removeObservers(viewLifecycleOwner)

    }


}