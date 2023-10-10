package com.techtest.mypokemonapp.core.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.techtest.mypokemonapp.core.data.local.entity.PokemonEntity
import com.techtest.mypokemonapp.core.utils.Constant.EXTRA_ID
import com.techtest.mypokemonapp.databinding.ListItemPokemonBinding
import com.techtest.mypokemonapp.feature.detail.DetailActivity

class PokemonPagingAdapter : PagingDataAdapter<PokemonEntity, PokemonPagingAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(private val binding: ListItemPokemonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(pokemon: PokemonEntity) {
            binding.apply {
                tvName.text = pokemon.name

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java).apply {
                        putExtra(EXTRA_ID, pokemon.id)
                    }
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(ListItemPokemonBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<PokemonEntity>() {
            override fun areItemsTheSame(oldItem: PokemonEntity, newItem: PokemonEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: PokemonEntity, newItem: PokemonEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}
