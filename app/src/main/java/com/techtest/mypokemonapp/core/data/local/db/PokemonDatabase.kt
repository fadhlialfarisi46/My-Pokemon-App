package com.techtest.mypokemonapp.core.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.techtest.mypokemonapp.core.data.local.entity.PokemonEntity
import com.techtest.mypokemonapp.core.data.local.entity.RemoteKeys

@Database(entities = [PokemonEntity::class, RemoteKeys::class], version = 1, exportSchema = false)
abstract class PokemonDatabase : RoomDatabase() {

    abstract fun pokemonDao(): PokemonDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}
