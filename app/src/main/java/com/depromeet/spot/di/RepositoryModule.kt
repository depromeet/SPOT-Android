package com.depromeet.spot.di

import com.depromeet.data.repository.ExampleRepositoryImpl
import com.depromeet.data.repository.ViewfinderRepositoryImpl
import com.depromeet.data.repository.WebSvgRepositoryImpl
import com.depromeet.domain.repository.ExampleRepository
import com.depromeet.domain.repository.ViewfinderRepository
import com.depromeet.domain.repository.WebSvgRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindExampleRepository(
        exampleRepositoryImpl: ExampleRepositoryImpl
    ): ExampleRepository

    @Binds
    @Singleton
    abstract fun bindWebSvgRepository(
        webSvgRepositoryImpl: WebSvgRepositoryImpl
    ): WebSvgRepository

    @Binds
    @Singleton
    abstract fun bindViewfinderRepository(
        viewfinderRepositoryImpl: ViewfinderRepositoryImpl
    ): ViewfinderRepository
}
