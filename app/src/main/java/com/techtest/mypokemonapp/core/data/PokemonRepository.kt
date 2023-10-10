package com.techtest.mypokemonapp.core.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.techtest.mypokemonapp.core.data.local.db.PokemonDatabase
import com.techtest.mypokemonapp.core.data.local.entity.PokemonEntity
import com.techtest.mypokemonapp.core.data.remote.network.ApiService
import com.techtest.mypokemonapp.core.data.remote.response.DetailPokemonResponse
import com.techtest.mypokemonapp.feature.search.SortType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PokemonRepository @Inject constructor(private val db: PokemonDatabase, private val apiService: ApiService) {

    @OptIn(ExperimentalPagingApi::class)
    fun getAllPokemons(): Flow<PagingData<PokemonEntity>> = Pager(
        config = PagingConfig(pageSize = 30),
        remoteMediator = PokemonRemoteMediator(db, apiService),
        pagingSourceFactory = { db.pokemonDao().getAllPokemons() },
    ).flow

    fun getDetailPokemon(id: String): Flow<Result<DetailPokemonResponse>> = flow {
        try {
            val response = apiService.getDetailPokemon(id)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    fun searchPokemon(query: String, sortBy: SortType): Flow<List<PokemonEntity>> = db.pokemonDao().searchPokemon(query, sortBy)
}
