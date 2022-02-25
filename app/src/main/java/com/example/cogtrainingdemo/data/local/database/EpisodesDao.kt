package com.example.cogtrainingdemo.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cogtrainingdemo.data.model.EpisodesItem

@Dao
interface EpisodesDao {
    @Query("SELECT * FROM Episodes")
    fun getEpisodesList(): List<EpisodesItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisodesItem(episodesItem: EpisodesItem)

//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    suspend fun insertListEpisodesItem(episodesItemList: List<EpisodesItem>)

    @Query("SELECT * FROM Episodes WHERE upper(characters) LIKE '%' || :characterName || '%'")
    suspend fun findEpisodesCharacterName(characterName: String): List<EpisodesItem>
}