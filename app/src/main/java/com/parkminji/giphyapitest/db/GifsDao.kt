package com.parkminji.giphyapitest.db

import androidx.room.*

@Dao
interface GifsDao {
    @Query("SELECT * FROM gifs")
    fun loadAllGifs() : List<GifEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGif(gif: GifEntity)

    @Query("DELETE FROM gifs WHERE id = :id")
    fun deleteGif(id: String)
}