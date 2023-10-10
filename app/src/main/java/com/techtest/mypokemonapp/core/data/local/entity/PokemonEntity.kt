package com.techtest.mypokemonapp.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon")
data class PokemonEntity(
    @PrimaryKey
    val id: String,

    val name: String,
)
