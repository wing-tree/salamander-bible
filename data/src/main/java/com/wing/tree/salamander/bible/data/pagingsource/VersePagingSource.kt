package com.wing.tree.salamander.bible.data.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wing.tree.salamander.bible.data.datasource.BibleDataSource
import com.wing.tree.salamander.bible.data.entity.Verse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VersePagingSource @Inject constructor(private val dataSource: BibleDataSource) : PagingSource<Int, Verse>() {
    override fun getRefreshKey(state: PagingState<Int, Verse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.let {
                it.prevKey?.inc() ?: it.nextKey?.dec()
            }
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Verse> {
        val key = params.key ?: INITIAL_KEY
        val data = withContext(Dispatchers.IO) {
            dataSource.load(key, params.loadSize)
        }

        return LoadResult.Page(
            data = data,
            prevKey = when(key) {
                INITIAL_KEY -> null
                else -> key.dec()
            },
            nextKey = when {
                data.isEmpty() -> null
                else -> key.plus((params.loadSize.div(LOAD_SIZE)))
            }
        )
    }

    companion object {
        private const val INITIAL_KEY = 0

        const val LOAD_SIZE = 50
    }
}