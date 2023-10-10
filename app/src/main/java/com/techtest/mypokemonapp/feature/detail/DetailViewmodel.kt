package com.techtest.mypokemonapp.feature.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.techtest.mypokemonapp.core.data.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewmodel @Inject constructor(
    private val pokemonRepository: PokemonRepository,
) : ViewModel() {

    fun getDetailPokemon(id: String) = pokemonRepository.getDetailPokemon(id).asLiveData()
}
