package com.wing.tree.salamander.bible.data.di

import com.wing.tree.salamander.bible.data.datasource.BibleDataSource
import com.wing.tree.salamander.bible.data.pagingsource.VersePagingSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal object PagingSourceModule {
    @Provides
    @Singleton
    fun providesVersePagingSource(dataSource: BibleDataSource): VersePagingSource {
        return VersePagingSource(dataSource)
    }
}