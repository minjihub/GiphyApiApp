package com.parkminji.giphyapitest.adapter

import com.parkminji.giphyapitest.db.GifEntity

interface ChangeGifDataStateListener {
    fun insertGif(entity: GifEntity)
    fun deleteGif(id: String)
}