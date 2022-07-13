package com.wing.tree.salamander.bible.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.wing.tree.salamander.bible.data.entity.Verse

@Dao
interface VerseDao {
    @Query("SELECT * FROM verse ORDER BY `index` ASC LIMIT :loadSize OFFSET :key * :loadSize")
    fun load(key: Int, loadSize: Int): List<Verse>
}