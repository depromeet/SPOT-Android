package com.depromeet.spot.di

import com.depromeet.data.remote.ExampleService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideMockService(retrofit: Retrofit): ExampleService =
        retrofit.create(ExampleService::class.java)
}
