package com.techtest.mypokemonapp.feature.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.techtest.mypokemonapp.core.data.PokemonRepository
import com.techtest.mypokemonapp.core.data.local.entity.PokemonEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository,
) : ViewModel() {

    fun getAllPokemons(): LiveData<PagingData<PokemonEntity>> =
        pokemonRepository.getAllPokemons().cachedIn(viewModelScope).asLiveData()
}
