package com.dpm.spot.di

import com.dpm.data.datasource.AuthDataSource
import com.dpm.data.datasource.ExampleDataSource
import com.dpm.data.datasource.HomeDataSource
import com.dpm.data.datasource.SeatReviewDataSource
import com.dpm.data.datasource.SignupRemoteDataSource
import com.dpm.data.datasource.ViewfinderDataSource
import com.dpm.data.datasource.WebSvgDataSource
import com.dpm.data.datasource.local.AuthDataSourceImpl
import com.dpm.data.datasource.remote.ExampleDataSourcelmpl
import com.dpm.data.datasource.remote.HomeDataSourceImpl
import com.dpm.data.datasource.remote.SeatReviewDataSourceImpl
import com.dpm.data.datasource.remote.SignupRemoteDataSourceImpl
import com.dpm.data.datasource.remote.WebSvgDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.dpm.data.datasource.remote.ViewfinderDataSourceImpl as ViewfinderDataSourceImpl1

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindExampleDataSource(
        exampleDataSourcelmpl: ExampleDataSourcelmpl,
    ): ExampleDataSource

    @Binds
    @Singleton
    abstract fun bindWebSvgDataSource(
        webSvgDataSourceImpl: WebSvgDataSourceImpl,
    ): WebSvgDataSource

    @Binds
    @Singleton
    abstract fun bindHomeDataSource(
        homeDataSourceImpl: HomeDataSourceImpl,
    ): HomeDataSource

    @Binds
    @Singleton
    abstract fun seatReviewDataSource(
        seatReviewDataSourceImpl: SeatReviewDataSourceImpl,
    ): SeatReviewDataSource

    @Binds
    @Singleton
    abstract fun bindViewfinderDataSource(
        viewfinderDataSourceImpl: ViewfinderDataSourceImpl1,
    ): ViewfinderDataSource

    @Binds
    @Singleton
    abstract fun bindSignupDataSource(
        signupDataSourceImpl: SignupRemoteDataSourceImpl,
    ): SignupRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindAuthDataSource(
        authDataSourceImpl: AuthDataSourceImpl
    ): AuthDataSource
}
