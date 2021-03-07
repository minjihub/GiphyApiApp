package com.parkminji.giphyapitest.favorite

import android.content.Context
import com.parkminji.giphyapitest.adapter.GifListAdapter

interface Contract {
    interface View {
        fun setAdapter(adapter: GifListAdapter)
        fun notifyList()
        fun getCurrentContext(): Context?
    }

    interface Presenter {
        fun createListAdapter()
        fun getFavoriteGifList()
        fun destroyDbInstance()
    }

    interface RequiredPresenter {
        fun setAdapter(adapter: GifListAdapter)
        fun notifyList()
        fun getCurrentContext(): Context?
    }
}