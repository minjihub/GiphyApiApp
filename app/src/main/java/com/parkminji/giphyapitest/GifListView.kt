package com.parkminji.giphyapitest

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.*
import com.parkminji.giphyapitest.adapter.GifListAdapter
import com.parkminji.giphyapitest.search.Contract
import com.parkminji.giphyapitest.search.Presenter
import kotlinx.android.synthetic.main.fragment_grid_list.*
import kotlinx.android.synthetic.main.fragment_grid_list.view.*

class GifListView : Fragment(), Contract.View{
    private var presenter: Contract.Presenter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_grid_list, container, false)
        view.title_name?.text =  "Trending GIFs"
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter = Presenter(this)
        presenter?.createListAdapter()
        presenter?.getGifList()
    }

    override fun setAdapter(adapter: GifListAdapter) {
        list_view?.let {
            val layoutManager = GridLayoutManager(context, 2)
            it.layoutManager = layoutManager
            it.adapter = adapter
            it.addOnScrollListener(object: RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = it.layoutManager as GridLayoutManager
                    val lastPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                    val totalCount = adapter.itemCount - 1

                    if(lastPosition == totalCount){
                        progress_bar?.visibility = View.VISIBLE
                        presenter?.getGifList()
                    }
                }
            })
        }
    }

    override fun notifyList() {
        progress_bar?.visibility = View.GONE
        list_view?.adapter?.notifyDataSetChanged()
    }

    override fun failSearch() {
        Toast.makeText(this.context, "네트워크를 확인해주세요.", Toast.LENGTH_SHORT).show()
    }

    override fun setNullList() {
        Toast.makeText(this.context, "문제가 발생했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
    }

    override fun getCurrentContext(): Context? {
        return this.context
    }

    override fun onDestroy() {
        presenter?.destroyDbInstance()
        super.onDestroy()
    }
}