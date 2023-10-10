package com.techtest.mypokemonapp.core.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.techtest.mypokemonapp.core.data.local.db.PokemonDatabase
import com.techtest.mypokemonapp.core.data.local.entity.PokemonEntity
import com.techtest.mypokemonapp.core.data.local.entity.RemoteKeys
import com.techtest.mypokemonapp.core.data.remote.network.ApiService
import com.techtest.mypokemonapp.core.utils.Helpers

@OptIn(ExperimentalPagingApi::class)
class PokemonRemoteMediator(
    private val db: PokemonDatabase,
    private val apiService: ApiService,
) : RemoteMediator<Int, PokemonEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokemonEntity>,
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(NEXT_PAGE_INDEX) ?: INITIAL_PAGE_INDEX
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null,
                )
                prevKey
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeysForLastItem(state)
                val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null,
                )
                nextKey
            }
        }

        try {
            val responseData = apiService.getAllPokemons(page, state.config.pageSize)
            val endOfPaginationReached = responseData.results.isEmpty()

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.remoteKeysDao().deleteRemoteKeys()
                    db.pokemonDao().deleteAll()
                }

                val prevKey = if (page == INITIAL_PAGE_INDEX) null else page - NEXT_PAGE_INDEX
                val nextKey = if (endOfPaginationReached) null else page + NEXT_PAGE_INDEX
                val keys = responseData.results.map {
                    val id = Helpers.convertURLtoId(it.url)

                    RemoteKeys(id = id, prevKey = prevKey, nextKey = nextKey)
                }
                db.remoteKeysDao().insertAll(keys)

                responseData.results.forEach { result ->
                    val id = Helpers.convertURLtoId(result.url)
                    val pokemon = PokemonEntity(
                        id = id,
                        name = result.name,
                    )

                    db.pokemonDao().insertPokemon(pokemon)
                }
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    private suspend fun getRemoteKeysForLastItem(state: PagingState<Int, PokemonEntity>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            db.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, PokemonEntity>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            db.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, PokemonEntity>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                db.remoteKeysDao().getRemoteKeysId(id)
            }
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 0
        const val NEXT_PAGE_INDEX = 30
    }
}
