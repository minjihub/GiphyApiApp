package com.parkminji.giphyapitest.favorite

import com.parkminji.giphyapitest.adapter.ChangeGifDataStateListener
import com.parkminji.giphyapitest.adapter.GifListAdapter
import com.parkminji.giphyapitest.db.GifEntity
import com.parkminji.giphyapitest.db.GifsDB
import com.parkminji.giphyapitest.model.Gif
import com.parkminji.giphyapitest.model.Image
import com.parkminji.giphyapitest.model.PreviewGif

class Model(private val requiredPresenter: Contract.RequiredPresenter)  {
    private lateinit var adapter: GifListAdapter

    fun createListAdapter(){
        adapter = GifListAdapter()
        val listener = object : ChangeGifDataStateListener{
            override fun insertGif(entity: GifEntity) {
                return
            }

            override fun deleteGif(id: String) {
                val context = requiredPresenter.getCurrentContext() ?: return
                val gifDB = GifsDB.getInstance(context)
                Thread(Runnable {
                    gifDB?.gifsDao()?.deleteGif(id)
                    val gifList = gifDB?.gifsDao()?.loadAllGifs() ?: return@Runnable
                    val list = convertGifEntityListToGifList(gifList)
                    adapter.changeGifList(list)
                    adapter.addFavoriteGifs(list)
                    requiredPresenter.getViewHandler().sendEmptyMessage(0)
                }).start()
            }
        }
        adapter.setChangeGifDataStateListener(listener)
        requiredPresenter.setAdapter(adapter)
    }

    fun getFavoriteGifList(){
        val context = requiredPresenter.getCurrentContext() ?: return
        val gifsDB = GifsDB.getInstance(context)
        Thread(Runnable {
            val gifList = gifsDB?.gifsDao()?.loadAllGifs() ?: return@Runnable
            val list = convertGifEntityListToGifList(gifList)
            adapter.addGifs(list)
            adapter.addFavoriteGifs(list)
            requiredPresenter.getViewHandler().sendEmptyMessage(0)
        }).start()
    }

    private fun convertGifEntityListToGifList(entityList: List<GifEntity>): List<Gif>{
        val list: MutableList<Gif> = mutableListOf()
        for(entity in entityList){
            val previewGif = PreviewGif(entity.previewUrl)
            val detailGif = PreviewGif(entity.detailUrl)
            val image = Image(previewGif, detailGif)
            val gif = Gif(entity.id, entity.title, image)
            list.add(gif)
        }

        return list
    }

    fun destroyDbInstance(){
        GifsDB.destroyInstance()
    }
}
