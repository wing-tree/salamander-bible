package com.wing.tree.salamander.bible.data.datasource

import com.wing.tree.salamander.bible.data.database.Database
import com.wing.tree.salamander.bible.data.entity.Verse
import javax.inject.Inject

class BibleDataSourceImpl @Inject constructor(database: Database): BibleDataSource {
    private val verseDao = database.verseDao()

    override suspend fun load(key: Int, loadSize: Int): List<Verse> {
        return verseDao.load(key, loadSize)
    }
}