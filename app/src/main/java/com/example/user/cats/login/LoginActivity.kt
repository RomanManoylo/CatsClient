package com.example.user.cats.login

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.example.user.cats.R
import com.example.user.cats.utils.LoginUtils
import kotlinx.android.synthetic.main.activity_login.*


/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity(), LoginContract.View {

    private lateinit var loginUtils: LoginUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })

        sign_in_button.setOnClickListener {
            attemptLogin()
        }
        loginUtils = LoginUtils(application.getSharedPreferences("",Context.MODE_PRIVATE) ?: return)
    }

    private fun attemptLogin() {

        login.error = null
        password.error = null

        val loginStr = login.text.toString()
        val passwordStr = password.text.toString()

        var cancel = false
        var focusView: View? = null

        if (!TextUtils.isEmpty(passwordStr) && !isPasswordValid(passwordStr)) {
            password.error = getString(R.string.error_invalid_password)
            focusView = password
            cancel = true
        }

        if (TextUtils.isEmpty(loginStr)) {
            login.error = getString(R.string.error_field_required)
            focusView = login
            cancel = true
        }

        if (cancel) {
            focusView?.requestFocus()
        } else {
            presenter.signIn(loginStr, passwordStr)
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 4
    }

    override fun signInFailed() {
        password.error = getString(R.string.error_incorrect_password)
        password.requestFocus()
    }

    override fun signInSuccess() {
        loginUtils.setLogin(login.text.toString())
        loginUtils.setPassword(password.text.toString())
        finish()
    }

    override var presenter: LoginContract.Presenter
        get() = LoginPresenter(this)
        set(value) {}


}
