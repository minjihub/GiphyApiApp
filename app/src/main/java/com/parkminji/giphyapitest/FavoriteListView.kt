package com.parkminji.giphyapitest

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.parkminji.giphyapitest.adapter.GifListAdapter
import com.parkminji.giphyapitest.favorite.Contract
import com.parkminji.giphyapitest.favorite.Presenter
import kotlinx.android.synthetic.main.title_list_view.view.*

class FavoriteListView : Fragment(), Contract.View {
    private var presenter: Contract.Presenter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorite_list, container, false)
        view.title_name.text = "Favorites"
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter = Presenter(this)
        presenter?.createListAdapter()
        presenter?.getFavoriteGifList()
    }

    override fun setAdapter(adapter: GifListAdapter) {
        view?.list_view?.let{
            val layoutManager = GridLayoutManager(context, 2)
            it.layoutManager = layoutManager
            it.adapter = adapter
        }
    }

    override fun notifyList() {
        view?.list_view?.adapter?.notifyDataSetChanged()
    }

    override fun getCurrentContext(): Context? {
        return this.context
    }

    override fun getViewHandler(): Handler {
        return viewHandler
    }

    private val viewHandler = object : Handler(){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if(msg.what == 0){
                view?.list_view?.adapter?.notifyDataSetChanged()
            }
        }
    }
}