package com.depromeet.spot.di

import com.depromeet.data.remote.ExampleService
import com.depromeet.data.remote.ViewfinderService
import com.depromeet.data.remote.WebSvgApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideMockService(retrofit: Retrofit): ExampleService =
        retrofit.create(ExampleService::class.java)

    @Provides
    @Singleton
    fun provideViewfinderService(retrofit: Retrofit): ViewfinderService =
        retrofit.create(ViewfinderService::class.java)

    @Provides
    @Singleton
    fun provideWebSvgService(@WebSvg retrofit: Retrofit): WebSvgApiService =
        retrofit.create(WebSvgApiService::class.java)
}
