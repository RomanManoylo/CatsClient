package com.example.user.cats.login

import com.example.user.cats.base.BasePresenter
import com.example.user.cats.base.BaseView

interface LoginContract {
    interface View : BaseView<Presenter> {
        fun signInFailed()
        fun signInSuccess()

    }

    interface Presenter : BasePresenter {
        fun signIn(login: String, password: String)
    }
}