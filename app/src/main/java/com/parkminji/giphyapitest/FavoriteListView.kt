package com.parkminji.giphyapitest

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.parkminji.giphyapitest.adapter.GifListAdapter
import com.parkminji.giphyapitest.favorite.Contract
import com.parkminji.giphyapitest.favorite.Presenter
import kotlinx.android.synthetic.main.fragment_grid_list.*
import kotlinx.android.synthetic.main.fragment_grid_list.view.*

class FavoriteListView : Fragment(), Contract.View {
    private var presenter: Contract.Presenter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_grid_list, container, false)
        view.title_name?.text = "Favorites"
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter = Presenter(this)
        presenter?.createListAdapter()
        presenter?.getFavoriteGifList()
    }

    override fun setAdapter(adapter: GifListAdapter) {
        list_view?.let{
            val layoutManager = GridLayoutManager(context, 2)
            it.layoutManager = layoutManager
            it.adapter = adapter
        }
    }

    override fun notifyList() {
        list_view?.adapter?.notifyDataSetChanged()
    }

    override fun getCurrentContext(): Context? {
        return this.context
    }

    override fun onDestroy() {
        presenter?.destroyDbInstance()
        super.onDestroy()
    }
}