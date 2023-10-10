package com.techtest.mypokemonapp.core.data.local.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.techtest.mypokemonapp.core.data.local.entity.PokemonEntity
import com.techtest.mypokemonapp.feature.search.SortType
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(vararg story: PokemonEntity)

    @Query("SELECT * FROM pokemon")
    fun getAllPokemons(): PagingSource<Int, PokemonEntity>

    @Query("DELETE FROM pokemon")
    fun deleteAll()

    @Query("SELECT * FROM pokemon WHERE name LIKE :query || '%' ORDER BY CASE WHEN :sortBy = 'ASC' THEN name END ASC, CASE WHEN :sortBy = 'DESC' THEN name END DESC")
    fun searchPokemon(query: String, sortBy: SortType): Flow<List<PokemonEntity>>
}
