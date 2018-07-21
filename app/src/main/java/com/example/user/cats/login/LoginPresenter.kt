package com.example.user.cats.login

import com.example.user.cats.repository.CatsRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class LoginPresenter(var loginView: LoginContract.View) : LoginContract.Presenter {
    override fun signIn(login: String, password: String) {
        val catsRepository = CatsRepository(login, password)
        catsRepository.signIn()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.code() == 401)
                        loginView.signInFailed()
                    else
                        loginView.signInSuccess()
                }, {
                    loginView.signInFailed()
                })


    }

    override fun start() {

    }
}