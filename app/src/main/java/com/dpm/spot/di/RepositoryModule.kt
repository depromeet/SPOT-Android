package com.dpm.spot.di

import com.dpm.data.repository.ExampleRepositoryImpl
import com.dpm.data.repository.HomeRepositoryImpl
import com.dpm.data.repository.SeatReviewRepositoryImpl
import com.dpm.data.repository.SignupRepositoryImpl
import com.dpm.data.repository.ViewfinderRepositoryImpl
import com.dpm.data.repository.WebSvgRepositoryImpl
import com.dpm.domain.repository.ExampleRepository
import com.dpm.domain.repository.HomeRepository
import com.dpm.domain.repository.SeatReviewRepository
import com.dpm.domain.repository.SignupRepository
import com.dpm.domain.repository.ViewfinderRepository
import com.dpm.domain.repository.WebSvgRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindExampleRepository(
        exampleRepositoryImpl: ExampleRepositoryImpl,
    ): ExampleRepository

    @Binds
    @Singleton
    abstract fun bindWebSvgRepository(
        webSvgRepositoryImpl: WebSvgRepositoryImpl,
    ): WebSvgRepository

    @Binds
    @Singleton
    abstract fun bindHomeRepository(
        homeRepositoryImpl: HomeRepositoryImpl,
    ): HomeRepository

    @Binds
    @Singleton
    abstract fun seatReviewRepository(
        seatReviewRepositoryImpl: SeatReviewRepositoryImpl,
    ): SeatReviewRepository

    @Binds
    @Singleton
    abstract fun bindViewfinderRepository(
        viewfinderRepositoryImpl: ViewfinderRepositoryImpl,
    ): ViewfinderRepository

    @Binds
    @Singleton
    abstract fun bindSignupRepository(
        signupRepositoryImpl: SignupRepositoryImpl,
    ): SignupRepository
}
