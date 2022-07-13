package com.wing.tree.salamander.bible.data.di

import com.wing.tree.salamander.bible.data.datasource.BibleDataSource
import com.wing.tree.salamander.bible.data.datasource.BibleDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal abstract class DataSourceModule {
    @Binds
    @Singleton
    abstract fun bindsBibleDataSource(dataSource: BibleDataSourceImpl): BibleDataSource
}