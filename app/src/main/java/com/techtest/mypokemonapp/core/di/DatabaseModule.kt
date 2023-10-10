package com.techtest.mypokemonapp.core.di

import android.content.Context
import androidx.room.Room
import com.techtest.mypokemonapp.core.data.local.db.PokemonDao
import com.techtest.mypokemonapp.core.data.local.db.PokemonDatabase
import com.techtest.mypokemonapp.core.data.local.db.RemoteKeysDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun providePokemonDao(storyDatabase: PokemonDatabase): PokemonDao = storyDatabase.pokemonDao()

    @Provides
    fun provideRemoteKeysDao(storyDatabase: PokemonDatabase): RemoteKeysDao = storyDatabase.remoteKeysDao()

    @Provides
    @Singleton
    fun providePokemonDatabase(@ApplicationContext context: Context): PokemonDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            PokemonDatabase::class.java,
            "pokemon_db",
        ).build()
    }
}
