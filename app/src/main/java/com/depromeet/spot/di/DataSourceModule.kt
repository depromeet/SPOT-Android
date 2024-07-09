package com.depromeet.spot.di

import com.depromeet.data.datasource.ExampleDataSource
import com.depromeet.data.datasource.remote.ExampleDataSourcelmpl
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
}
