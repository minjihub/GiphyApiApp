package com.parkminji.giphyapitest.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gifs")
data class GifEntity(@PrimaryKey val id: String,
                     @ColumnInfo(name = "title") val title: String?,
                     @ColumnInfo(name = "previewUrl") val previewUrl: String?,
                     @ColumnInfo(name = "detailUrl") val detailUrl: String?)