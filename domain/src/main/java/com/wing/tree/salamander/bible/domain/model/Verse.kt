package com.wing.tree.salamander.bible.domain.model

abstract class Verse {
    abstract val index: Int
    abstract val book: Int
    abstract val chapter: Int
    abstract val verse: Int
    abstract val word: String
}