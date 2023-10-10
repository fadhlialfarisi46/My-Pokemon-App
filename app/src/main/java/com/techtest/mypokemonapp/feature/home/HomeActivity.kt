package com.techtest.mypokemonapp.feature.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.techtest.mypokemonapp.core.adapter.LoadingStateAdapter
import com.techtest.mypokemonapp.core.adapter.PokemonPagingAdapter
import com.techtest.mypokemonapp.core.data.local.entity.PokemonEntity
import com.techtest.mypokemonapp.databinding.ActivityHomeBinding
import com.techtest.mypokemonapp.feature.search.SearchActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var listAdapter: PokemonPagingAdapter

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSwipeRefreshLayout()
        setupListAdapter()
        observePokemons()
        listenClickBtn()
    }

    private fun setSwipeRefreshLayout() {
        binding.swipeRefresh.setOnRefreshListener {
            observePokemons()
        }
    }

    private fun setupListAdapter() {
        val linearLayoutManager = LinearLayoutManager(this)

        listAdapter = PokemonPagingAdapter()

        listAdapter.addLoadStateListener { loadState ->
            if ((loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && listAdapter.itemCount < 1) || loadState.source.refresh is LoadState.Error) {
                binding.apply {
                    viewError.root.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    rvPokemons.isVisible = false
                }
            } else {
                binding.apply {
                    viewError.root.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    rvPokemons.isVisible = true
                }
            }

            binding.swipeRefresh.isRefreshing = loadState.source.refresh is LoadState.Loading
        }

        try {
            binding.rvPokemons.apply {
                adapter = listAdapter.withLoadStateFooter(
                    footer = LoadingStateAdapter {
                        listAdapter.retry()
                    },
                )
                layoutManager = linearLayoutManager
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    private fun listenClickBtn() {
        binding.apply {
            fabSearch.setOnClickListener {
                val intent = Intent(this@HomeActivity, SearchActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun observePokemons() {
        viewModel.getAllPokemons().observe(this@HomeActivity) {
            updateRecyclerViewData(it)
        }
    }

    private fun updateRecyclerViewData(pokemons: PagingData<PokemonEntity>) {
        val recyclerViewState = binding.rvPokemons.layoutManager?.onSaveInstanceState()

        listAdapter.submitData(lifecycle, pokemons)

        binding.rvPokemons.layoutManager?.onRestoreInstanceState(recyclerViewState)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}
