package com.parkminji.giphyapitest.search

import android.content.Context
import android.os.Handler
import com.parkminji.giphyapitest.adapter.GifListAdapter

class Presenter(private val view: Contract.View) : Contract.Presenter, Contract.RequiredPresenter {
    private var model: Model? = null

    init {
        model = Model(this)
    }

    override fun createListAdapter() {
        model?.createListAdapter()
    }

    override fun getGifList() {
        model?.getGifList()
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

    override fun failSearch() {
        view.failSearch()
    }

    override fun setNullList() {
        view.setNullList()
    }

    override fun getCurrentContext(): Context? {
        return view.getCurrentContext()
    }

    override fun getViewHandler(): Handler {
        return view.getViewHandler()
    }
}