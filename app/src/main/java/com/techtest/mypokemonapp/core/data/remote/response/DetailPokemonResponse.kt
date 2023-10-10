package com.techtest.mypokemonapp.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class DetailPokemonResponse(

    @field:SerializedName("abilities")
    val abilities: List<AbilitiesItem>,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("sprites")
    val sprites: Sprites,
)

data class Sprites(

    @field:SerializedName("front_default")
    val frontDefault: String,
)

data class AbilitiesItem(

    @field:SerializedName("ability")
    val ability: Ability,
)

data class Ability(

    @field:SerializedName("name")
    val name: String,
)
