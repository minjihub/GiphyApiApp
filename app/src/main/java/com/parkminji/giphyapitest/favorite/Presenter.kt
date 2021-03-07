package com.parkminji.giphyapitest.favorite

import android.content.Context
import com.parkminji.giphyapitest.adapter.GifListAdapter

class Presenter(private val view: Contract.View) : Contract.Presenter, Contract.RequiredPresenter {
    private var model: Model? = null

    init {
        model = Model(this)
    }

    override fun createListAdapter() {
        model?.createListAdapter()
    }

    override fun getFavoriteGifList() {
        model?.getFavoriteGifList()
    }

    override fun destroyDbInstance() {
        model?.destroyDbInstance()
    }

    override fun setAdapter(adapter: GifListAdapter) {
        view.setAdapter(adapter)
    }

    override fun notifyList() {
        view.notifyList()
    }

    override fun getCurrentContext(): Context? {
        return view.getCurrentContext()
    }

}