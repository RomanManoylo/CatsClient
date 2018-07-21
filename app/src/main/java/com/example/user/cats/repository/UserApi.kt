package com.example.user.cats.repository

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface UserApi {
    @GET("login")
    fun signIn() : Single<Response<ResponseBody>>
}