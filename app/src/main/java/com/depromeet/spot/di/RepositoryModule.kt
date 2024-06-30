package com.depromeet.spot.di

import com.depromeet.data.repository.ExampleRepositoryImpl
import com.depromeet.domain.repository.ExampleRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMockRepository(mockRepositoryImpl: ExampleRepositoryImpl): ExampleRepository =
        mockRepositoryImpl
}
