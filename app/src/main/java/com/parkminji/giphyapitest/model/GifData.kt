package com.parkminji.giphyapitest.model

data class GifData(val data: List<Gif>)

data class Gif(val id: String,
               val title: String?,
               val images: Image?)

data class Image(val preview_gif: PreviewGif?, // 미리보기 url > 저화질
                 val downsized: PreviewGif?) // 상세보기 url > 고화질

data class PreviewGif(val url: String?)