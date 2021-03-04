package com.parkminji.giphyapitest.favorite

import android.content.Context
import android.os.Handler
import com.parkminji.giphyapitest.adapter.GifListAdapter

interface Contract {
    interface View {
        fun setAdapter(adapter: GifListAdapter)
        fun notifyList()
        fun getCurrentContext(): Context?
        fun getViewHandler(): Handler
    }

    interface Presenter {
        fun createListAdapter()
        fun getFavoriteGifList()
    }

    interface RequiredPresenter {
        fun setAdapter(adapter: GifListAdapter)
        fun notifyList()
        fun getCurrentContext(): Context?
        fun getViewHandler(): Handler
    }
}