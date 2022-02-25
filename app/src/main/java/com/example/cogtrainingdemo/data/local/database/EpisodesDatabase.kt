package com.example.cogtrainingdemo.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cogtrainingdemo.data.model.EpisodesItem

@Database(entities = arrayOf(EpisodesItem::class), version = 2)
@TypeConverters(Converters::class)
abstract class EpisodesDatabase : RoomDatabase() {
    abstract fun episodesDao(): EpisodesDao

    companion object {
        private var INSTANCE: EpisodesDatabase? = null
        private const val DB_NAME = "episodes.db"
        fun getInstance(context: Context): EpisodesDatabase {
            return INSTANCE ?: let {
                synchronized(EpisodesDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        EpisodesDatabase::class.java, DB_NAME
                    ).build()
                    INSTANCE as EpisodesDatabase
                }
            }
        }
    }
}