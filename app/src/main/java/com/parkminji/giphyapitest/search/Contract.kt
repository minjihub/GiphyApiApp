package com.parkminji.giphyapitest.search

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
        fun getGifList()
    }

    interface RequiredPresenter {
        fun setAdapter(adapter: GifListAdapter)
        fun notifyList()
        fun getCurrentContext(): Context?
        fun getViewHandler(): Handler
    }
}