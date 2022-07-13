package com.wing.tree.salamander.bible.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.wing.tree.salamander.bible.data.dao.VerseDao
import com.wing.tree.salamander.bible.data.entity.Verse

@androidx.room.Database(
    entities = [Verse::class],
    exportSchema = false,
    version = 1
)
abstract class Database: RoomDatabase() {
    abstract fun verseDao(): VerseDao

    companion object {
        private const val PACKAGE_NAME = "com.wing.tree.salamander.bible.data.database"
        private const val CLASS_NAME = "Database"
        private const val NAME = "$PACKAGE_NAME.$CLASS_NAME"
        private const val FILE_NAME = "KoreanRevised"
        private const val VERSION = "1.0.0"

        @Volatile
        private var INSTANCE: Database? = null

        fun getInstance(context: Context): Database {
            synchronized(this) {
                return INSTANCE ?: let {
                    getBuilder(context).build().also {
                        INSTANCE = it
                    }
                }
            }
        }

        private fun getBuilder(context: Context): Builder<Database> {
            return Room.databaseBuilder(
                context.applicationContext,
                Database::class.java,
                "$NAME.$FILE_NAME:$VERSION"
            ).createFromAsset("$FILE_NAME.db")
        }
    }
}