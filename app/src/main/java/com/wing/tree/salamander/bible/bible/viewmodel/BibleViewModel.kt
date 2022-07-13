package com.wing.tree.salamander.bible.bible.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.wing.tree.salamander.bible.data.datasource.BibleDataSource
import com.wing.tree.salamander.bible.data.pagingsource.VersePagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BibleViewModel @Inject constructor(
    dataSource: BibleDataSource,
    application: Application
) : AndroidViewModel(application) {
    val pager = Pager(
        config = PagingConfig(pageSize = VersePagingSource.LOAD_SIZE),
        pagingSourceFactory = { VersePagingSource(dataSource) }
    )
        .flow
        .cachedIn(viewModelScope)
}