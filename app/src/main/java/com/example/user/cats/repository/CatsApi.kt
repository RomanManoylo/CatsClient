package com.example.user.cats.repository

import okhttp3.RequestBody
import com.example.user.cats.entity.Cat
import io.reactivex.Single
import okhttp3.ResponseBody
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*


interface CatsApi {

    @GET("cats")
    fun getAllCats(): Single<Response<List<Cat>>>

    @Multipart
    @POST("cats/create")
    fun addCat(@Part("cat") cat: Cat, @Part image: MultipartBody.Part?): Single<Response<ResponseBody>>

    @Multipart
    @POST("cats/createImg")
    fun addCatImg(@Part image: MultipartBody.Part?): Single<Response<ResponseBody>>

    @DELETE("cats/delete/{id}")
    fun deleteCat(@Path("id") id: Long): Single<Response<ResponseBody>>

    @Streaming
    @GET("cats/files/{filename}")
    fun getCatImgURL(@Path("filename") filename: String): Single<Response<ResponseBody>>

    @GET("cats/search/{id}")
    fun searchCat(@Path("id")id: Long): Single<Response<Cat>>

}