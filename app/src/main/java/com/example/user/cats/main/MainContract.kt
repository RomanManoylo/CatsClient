package com.example.user.cats.main

import android.net.Uri
import com.example.user.cats.base.BasePresenter
import com.example.user.cats.base.BaseView
import com.example.user.cats.entity.Cat
import okhttp3.MultipartBody

interface MainContract {
    interface View : BaseView<Presenter> {
        fun showCats(body: List<Cat>?)

    }

    interface Presenter : BasePresenter {
        fun deleteCat(cat: Cat)
        fun refresh()
        fun searchCat(query: String)
        //fun getImg(imgName:String)
    }
}