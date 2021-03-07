package com.parkminji.giphyapitest.favorite

import android.os.AsyncTask
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
                val deleteTask = object :AsyncTask<Unit, Unit, Unit>(){
                    override fun doInBackground(vararg params: Unit?) {
                        gifDB?.gifsDao()?.deleteGif(id)
                        val gifList = gifDB?.gifsDao()?.loadAllGifs() ?: return
                        val list = convertGifEntityListToGifList(gifList)
                        adapter.changeGifList(list)
                        adapter.addFavoriteGifs(list)
                    }

                    override fun onPostExecute(result: Unit?) {
                        super.onPostExecute(result)
                        requiredPresenter.notifyList()
                    }
                }
                deleteTask.execute()
            }
        }
        adapter.setChangeGifDataStateListener(listener)
        requiredPresenter.setAdapter(adapter)
    }

    fun getFavoriteGifList(){
        val context = requiredPresenter.getCurrentContext() ?: return
        val gifsDB = GifsDB.getInstance(context)

        val getFavoriteListTask = object : AsyncTask<Unit, Unit, Unit>(){
            override fun doInBackground(vararg params: Unit?) {
                val gifList = gifsDB?.gifsDao()?.loadAllGifs() ?: return
                val list = convertGifEntityListToGifList(gifList)
                adapter.addGifs(list)
                adapter.addFavoriteGifs(list)
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                requiredPresenter.notifyList()
            }
        }
        getFavoriteListTask.execute()
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
