package com.techtest.mypokemonapp.core.data.remote.network

import com.techtest.mypokemonapp.core.data.remote.response.DetailPokemonResponse
import com.techtest.mypokemonapp.core.data.remote.response.PokemonsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("pokemon")
    suspend fun getAllPokemons(
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 20,
    ): PokemonsResponse

    @GET("pokemon/{id}")
    suspend fun getDetailPokemon(
        @Path("id") id: String,
    ): DetailPokemonResponse
}
