package com.depromeet.spot.di

import com.depromeet.data.datasource.ExampleDataSource
import com.depromeet.data.datasource.HomeDataSource
import com.depromeet.data.datasource.SeatReviewDataSource
import com.depromeet.data.datasource.ViewfinderDataSource
import com.depromeet.data.datasource.WebSvgDataSource
import com.depromeet.data.datasource.remote.ExampleDataSourcelmpl
import com.depromeet.data.datasource.remote.SeatReviewDataSourceImpl
import com.depromeet.data.datasource.remote.WebSvgDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.depromeet.data.datasource.remote.ViewfinderDataSourceImpl as ViewfinderDataSourceImpl1

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
        homeDataSourceImpl: WebSvgDataSourceImpl
    ) : HomeDataSource

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
}
