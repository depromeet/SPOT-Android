package com.dpm.spot.di

import com.dpm.data.remote.ExampleService
import com.dpm.data.remote.HomeApiService
import com.dpm.data.remote.SeatReviewService
import com.dpm.data.remote.SignupService
import com.dpm.data.remote.ViewfinderService
import com.dpm.data.remote.WebSvgApiService
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

    @Provides
    @Singleton
    fun provideViewfinderService(retrofit: Retrofit): ViewfinderService =
        retrofit.create(ViewfinderService::class.java)

    @Provides
    @Singleton
    fun provideWebSvgService(@WebSvg retrofit: Retrofit): WebSvgApiService =
        retrofit.create(WebSvgApiService::class.java)

    @Provides
    @Singleton
    fun provideHomeService(retrofit: Retrofit): HomeApiService =
        retrofit.create(HomeApiService::class.java)

    @Provides
    @Singleton
    fun provideSeatReviewService(retrofit: Retrofit): SeatReviewService =
        retrofit.create(SeatReviewService::class.java)

    @Provides
    @Singleton
    fun provideSignupService(retrofit: Retrofit): SignupService =
        retrofit.create(SignupService::class.java)
}
