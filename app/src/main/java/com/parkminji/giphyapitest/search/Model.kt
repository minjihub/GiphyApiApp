package com.parkminji.giphyapitest.search

import android.util.Log
import com.parkminji.giphyapitest.adapter.ChangeGifDataStateListener
import com.parkminji.giphyapitest.adapter.GifListAdapter
import com.parkminji.giphyapitest.db.GifEntity
import com.parkminji.giphyapitest.db.GifsDB
import com.parkminji.giphyapitest.model.Gif
import com.parkminji.giphyapitest.model.GifData
import com.parkminji.giphyapitest.model.Image
import com.parkminji.giphyapitest.model.PreviewGif
import com.parkminji.giphyapitest.utils.RetrofitConnect
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Model(private val requiredPresenter: Contract.RequiredPresenter) {
    private var offset = 0
    private lateinit var adapter: GifListAdapter

    fun createListAdapter(){
        adapter = GifListAdapter()
        val listener = object : ChangeGifDataStateListener {
            override fun insertGif(entity: GifEntity) {
                val context = requiredPresenter.getCurrentContext() ?: return
                val gifDB = GifsDB.getInstance(context)
                Thread(Runnable {
                    gifDB?.gifsDao()?.insertGif(entity)
                }).start()
            }

            override fun deleteGif(id: String) {
                val context = requiredPresenter.getCurrentContext() ?: return
                val gifDB = GifsDB.getInstance(context)
                Thread(Runnable {
                    gifDB?.gifsDao()?.deleteGif(id)
                }).start()
            }
        }
        adapter.setChangeGifDataStateListener(listener)
        requiredPresenter.setAdapter(adapter)
    }

    fun getGifList(){
        // 불러올 수 있는 position 최대값은 4999
        if(offset >= 4999){
            return
        }
        val API_KEY = "발급받은 API Key 입력"
        val limit = 30
        val service = RetrofitConnect().connectGiphyService
        val call = service?.getTrendGifList(API_KEY, limit, offset)
        call?.enqueue(object : Callback<GifData>{
            override fun onFailure(call: Call<GifData>, t: Throwable) {
                Log.e("Retrofit_fail", t.toString())
                requiredPresenter.failSearch()
            }

            override fun onResponse(call: Call<GifData>, response: Response<GifData>) {
                offset += limit
                Log.e("Retrofit_success", response.body().toString())
                val list = response.body()?.data
                list?.let {
                    adapter.addGifs(list)
                    getFavoriteGifList()
                } ?: requiredPresenter.setNullList()
            }
        })
    }

    private fun getFavoriteGifList(){
        val context = requiredPresenter.getCurrentContext() ?: return
        val gifsDB = GifsDB.getInstance(context)
        Thread(Runnable {
            val gifList = gifsDB?.gifsDao()?.loadAllGifs() ?: return@Runnable
            val list: MutableList<Gif> = mutableListOf()
            for(entity in gifList){
                val previewGif = PreviewGif(entity.previewUrl)
                val detailGif = PreviewGif(entity.detailUrl)
                val image = Image(previewGif, detailGif)
                val gif = Gif(entity.id, entity.title, image)
                list.add(gif)
            }
            adapter.addFavoriteGifs(list)
            requiredPresenter.getViewHandler().sendEmptyMessage(0)
        }).start()
    }

    fun destroyDbInstance(){
        GifsDB.destroyInstance()
    }
}