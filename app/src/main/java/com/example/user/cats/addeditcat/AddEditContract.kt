package com.example.user.cats.login

import com.example.user.cats.base.BasePresenter
import com.example.user.cats.base.BaseView
import com.example.user.cats.entity.Cat

interface AddEditContract {
    interface View: BaseView<Presenter>{
        fun savingCompleated()

    }
    interface Presenter: BasePresenter{
        fun addCat(cat: Cat, image: String)
    }
}