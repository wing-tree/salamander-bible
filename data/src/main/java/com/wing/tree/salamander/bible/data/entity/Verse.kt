package com.wing.tree.salamander.bible.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wing.tree.salamander.bible.domain.model.Verse

@Entity(tableName = "verse")
data class Verse(
    @PrimaryKey(autoGenerate = false)
    override val index: Int,
    override val book: Int,
    override val chapter: Int,
    override val verse: Int,
    override val word: String
) : Verse()