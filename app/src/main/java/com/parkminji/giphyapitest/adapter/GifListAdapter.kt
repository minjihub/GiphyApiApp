package com.parkminji.giphyapitest.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AlertDialog
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.parkminji.giphyapitest.R
import com.parkminji.giphyapitest.db.GifEntity
import com.parkminji.giphyapitest.model.Gif
import kotlinx.android.synthetic.main.detail_gif_dialog.view.*
import kotlinx.android.synthetic.main.gif_item_layout.view.*

class GifListAdapter : RecyclerView.Adapter<GifListAdapter.GifListHolder>() {
    private var gifList: MutableList<Gif> = mutableListOf()
    private var favoriteList: List<Gif> = listOf()
    private var changeGifDataStateListener: ChangeGifDataStateListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.gif_item_layout, parent, false)
        return GifListHolder(view)
    }

    override fun getItemCount(): Int {
        return gifList.size
    }

    override fun onBindViewHolder(holder: GifListHolder, position: Int) {
        holder.bind(gifList[position])
    }

    fun setChangeGifDataStateListener(listener: ChangeGifDataStateListener){
        changeGifDataStateListener = listener
    }

    fun addGifs(list: List<Gif>){
        gifList.addAll(list)
    }

    fun addFavoriteGifs(list: List<Gif>){
        favoriteList = list
    }

    fun changeGifList(list: List<Gif>){
        gifList.clear()
        gifList.addAll(list)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class GifListHolder(private val view: View): RecyclerView.ViewHolder(view){
        fun bind(gif: Gif){
            for(favoriteGif in favoriteList){
                if(gif.id == favoriteGif.id){
                    view.like_button.isChecked = true
                }
            }

            val previewGifUrl = gif.images?.preview_gif?.url
            val detailUrl = gif.images?.downsized?.url

            Glide.with(view)
                    .load(previewGifUrl)
                    .into(view.image_view)

            view.image_view.setOnClickListener{
                showDialog(detailUrl, previewGifUrl)
            }

            view.like_button.setOnCheckedChangeListener(object: CompoundButton.OnCheckedChangeListener{
                override fun onCheckedChanged(button: CompoundButton?, isChecked: Boolean) {
                    if(isChecked){
                        val gifEntity = GifEntity(gif.id, gif.title, previewGifUrl, detailUrl)
                        changeGifDataStateListener?.insertGif(gifEntity)
                    }else{
                        changeGifDataStateListener?.deleteGif(gif.id)
                    }
                }
            })
        }

        private fun showDialog(detailUrl: String?, previewUrl: String?){
            val inflater = LayoutInflater.from(view.context)
            val dialogLayout = inflater.inflate(R.layout.detail_gif_dialog, null)
            val detailView = dialogLayout.gif_detail_view
            val progressBar = dialogLayout.detail_progress_bar

            val requestListener = object : RequestListener<Drawable>{
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    progressBar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    progressBar.visibility = View.GONE
                    detailView.setPadding(10)
                    return false
                }
            }

            Glide.with(view.context)
                    .load(detailUrl)
                    .listener(requestListener)
                    .error(
                            Glide.with(detailView)
                                    .load(previewUrl)
                                    .listener(requestListener)
                                    .error(R.drawable.error_load_fail_gif)
                    )
                    .into(detailView)

            AlertDialog.Builder(view.context).run {
                setView(dialogLayout)
                show()
            }.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        }
    }
}