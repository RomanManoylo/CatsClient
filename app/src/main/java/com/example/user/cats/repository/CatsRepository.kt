package com.example.user.cats.repository

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import com.example.user.cats.entity.Cat
import io.reactivex.Single
import okhttp3.ResponseBody
import okhttp3.MultipartBody
import retrofit2.Response
import java.util.concurrent.TimeUnit


const val URL = "http://192.168.0.102:8080/"

class CatsRepository(var login: String, var password: String){
    private lateinit var catsService: CatsApi
    private lateinit var userService: UserApi

    init {

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)
                .authenticator { route, response ->
                    val credential = Credentials.basic(login, password)
                    response.request().newBuilder().header("Authorization", credential).build()
                }.connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)


        val mRetrofit = Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient.build())
                .build()

        catsService = mRetrofit.create(CatsApi::class.java)
        userService = mRetrofit.create(UserApi::class.java)
    }

    fun geAllCats(): Single<retrofit2.Response<List<Cat>>> {
        return catsService.getAllCats()
    }

    fun addCat(cat: Cat, image: MultipartBody.Part?): Single<retrofit2.Response<ResponseBody>> {
        return catsService.addCat(cat, image)
    }

    fun addCatImg(image: MultipartBody.Part?): Single<retrofit2.Response<ResponseBody>> {
        return catsService.addCatImg(image)
    }

    fun deleteCat(id: Long): Single<retrofit2.Response<ResponseBody>> {
        return catsService.deleteCat(id)
    }

    fun getCatImgURL(filename: String): Single<Response<ResponseBody>> {
        return catsService.getCatImgURL(filename)
    }

    fun searchCat(id: Long): Single<Response<Cat>> {
        return catsService.searchCat(id)
    }

    fun signIn(): Single<Response<ResponseBody>> {
        return userService.signIn()
    }
}