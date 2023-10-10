package com.techtest.mypokemonapp.core.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.techtest.mypokemonapp.databinding.ItemLoadingStateBinding

class LoadingStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<LoadingStateAdapter.ViewHolder>() {
    class ViewHolder(private val binding: ItemLoadingStateBinding, retry: () -> Unit) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.retryButton.setOnClickListener { retry.invoke() }
        }
        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.errorMsg.text = loadState.error.localizedMessage
            }
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.retryButton.isVisible = loadState is LoadState.Error
            binding.errorMsg.isVisible = loadState is LoadState.Error
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemLoadingStateBinding.inflate(layoutInflater, parent, false), retry)
    }
}
