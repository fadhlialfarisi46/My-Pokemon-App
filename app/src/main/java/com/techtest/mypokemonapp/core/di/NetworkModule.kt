package com.techtest.mypokemonapp.core.di

import com.techtest.mypokemonapp.core.data.remote.network.ApiConfig
import com.techtest.mypokemonapp.core.data.remote.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideApiService(): ApiService = ApiConfig.getApiService()
}
