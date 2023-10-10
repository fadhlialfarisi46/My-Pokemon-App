package com.techtest.mypokemonapp.feature.search

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.techtest.mypokemonapp.R
import com.techtest.mypokemonapp.core.adapter.PokemonAdapter
import com.techtest.mypokemonapp.databinding.ActivitySearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var listAdapter: PokemonAdapter

    private val viewModel: SearchViewModel by viewModels()

    private var isAsc = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleSearchAndSort()
        observePokemons()
        setupListAdapter()
    }

    private fun observePokemons() {
        viewModel.searchPokemon.observe(this) {
            listAdapter.submitList(it) {
                binding.rvPokemons.post { binding.rvPokemons.scrollToPosition(0) }
            }
        }
    }

    private fun setupListAdapter() {
        val linearLayoutManager = LinearLayoutManager(this)

        listAdapter = PokemonAdapter()
        try {
            binding.rvPokemons.apply {
                adapter = listAdapter
                layoutManager = linearLayoutManager
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    private fun handleSearchAndSort() {
        binding.apply {
            etSearch.doAfterTextChanged {
                Log.e("search ", binding.etSearch.text.toString().trim())
                lifecycleScope.launch {
                    viewModel.search.emit(binding.etSearch.text.toString().trim())
                }
            }
            btnSort.setOnClickListener {
                if (isAsc) {
                    btnSort.setIconResource(R.drawable.ic_up)
                    isAsc = false
                    lifecycleScope.launch {
                        viewModel.sortType.emit(SortType.DESC)
                    }
                } else {
                    btnSort.setIconResource(R.drawable.ic_down)
                    isAsc = true
                    lifecycleScope.launch {
                        viewModel.sortType.emit(SortType.ASC)
                    }
                }
            }
        }
    }
}
