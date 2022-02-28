package com.example.cogtrainingdemo.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "Episodes")
@Parcelize
data class EpisodesItem(
    @PrimaryKey
    val episode_id: Int,
    val title: String? = null,
    val season: String? = null,
    val air_date: String? = null,
    val characters: ArrayList<String>? = null,
    val episode: String? = null,
    val name: String? = null,
    val series: String? = null
) : Parcelable
