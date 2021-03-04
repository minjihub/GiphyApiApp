package com.parkminji.giphyapitest.utils

import com.parkminji.giphyapitest.model.GifData
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

class RetrofitConnect {
    private var retrofit: Retrofit? = null
    var connectGiphyService: ConnectGiphyService? = null

    init {
        retrofit = Retrofit.Builder()
            .baseUrl("https://api.giphy.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        connectGiphyService = retrofit?.create(ConnectGiphyService::class.java)
    }

    interface ConnectGiphyService{
        @GET("v1/gifs/trending")
        fun getTrendGifList(
            @Query("api_key") key: String,
            @Query("limit") limit: Int,
            @Query("offset") offset: Int
        ): Call<GifData>
    }
}