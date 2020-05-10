package com.fristano.redditfeedchallenge.view.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fristano.redditfeedchallenge.databinding.PostItemBinding
import com.fristano.redditfeedchallenge.model.Post


class FeedListAdapter(private var _items : ArrayList<Post>,
                      private var _callback: IFeedListAdapterAdapterCallback) :
    RecyclerView.Adapter<FeedListAdapter.DataBindingViewHolder>() {


    interface IFeedListAdapterAdapterCallback {
        fun onClick(post: Post)
        fun onDismiss(post: Post)
    }

    override fun getItemCount(): Int {
        return _items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder {
        val binding = PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataBindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder, position: Int) {
        holder.binding.post = _items[position]
        holder.binding.callback = _callback
    }

    fun replaceContent(items : ArrayList<Post>) {
        _items = items
        notifyDataSetChanged()
    }

    fun removeItem(pos: Int) {
        _items.removeAt(pos)
        notifyItemRemoved(pos)
    }

    fun addContent(items : ArrayList<Post>) {
        _items.addAll(items)
        notifyDataSetChanged()
    }

    fun cleanContent() {
        _items.clear()
        notifyDataSetChanged()
    }

    open inner class DataBindingViewHolder(val binding: PostItemBinding) : RecyclerView.ViewHolder(binding.root)
}
