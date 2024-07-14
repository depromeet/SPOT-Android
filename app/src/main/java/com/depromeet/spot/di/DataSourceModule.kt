package com.depromeet.spot.di

import com.depromeet.data.datasource.ExampleDataSource
import com.depromeet.data.datasource.WebSvgDataSource
import com.depromeet.data.datasource.remote.ExampleDataSourcelmpl
import com.depromeet.data.datasource.remote.WebSvgDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindExampleDataSource(
        exampleDataSourcelmpl: ExampleDataSourcelmpl
    ): ExampleDataSource

    @Binds
    @Singleton
    abstract fun bindWebSvgDataSource(
        webSvgDataSourceImpl: WebSvgDataSourceImpl
    ): WebSvgDataSource
}
