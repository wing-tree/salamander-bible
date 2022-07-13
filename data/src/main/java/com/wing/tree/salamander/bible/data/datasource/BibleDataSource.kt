package com.wing.tree.salamander.bible.data.datasource

import com.wing.tree.salamander.bible.data.entity.Verse

interface BibleDataSource {
    suspend fun load(key: Int, loadSize: Int): List<Verse>
}