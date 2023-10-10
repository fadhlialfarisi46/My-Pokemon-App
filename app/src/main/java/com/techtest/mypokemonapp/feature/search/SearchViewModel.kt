package com.techtest.mypokemonapp.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.techtest.mypokemonapp.core.data.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository,
) : ViewModel() {

    val search = MutableStateFlow("")
    val sortType = MutableStateFlow(SortType.ASC)

    private val searchPokemonFlow = combine(search, sortType) { searchQuery, sortingType ->
        pokemonRepository.searchPokemon(searchQuery, sortingType).first()
    }

    val searchPokemon = searchPokemonFlow.asLiveData()
}

enum class SortType {
    ASC, DESC
}
