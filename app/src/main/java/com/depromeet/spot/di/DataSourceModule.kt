package com.depromeet.spot.di

import com.depromeet.data.datasource.ExampleDataSource
import com.depromeet.data.datasource.remote.ExampleDataSourcelmpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideMockDataSource(mockDataSourceImpl: ExampleDataSourcelmpl): ExampleDataSource =
        mockDataSourceImpl
}
