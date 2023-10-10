package com.techtest.mypokemonapp.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class PokemonsResponse(

    @field:SerializedName("results")
    val results: List<PokemonsItem>,
)

data class PokemonsItem(

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("url")
    val url: String,
)
