package com.example.user.cats.main

import com.example.user.cats.entity.Cat
import com.example.user.cats.repository.CatsRepository
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import okio.Okio
import retrofit2.Response
import java.io.File


class MainPresenter(val catsRepository: CatsRepository, val mainView: MainContract.View, val cachePath: String) : MainContract.Presenter {

    private val cats: MutableList<Cat> = ArrayList()
    override fun start() {
        getAllCats()
    }

    override fun deleteCat(cat: Cat) {
        catsRepository.deleteCat(cat.id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer {
                    if (it.isSuccessful) {
                        refresh()
                    }
                })
    }

    private fun showCats() {
        mainView.showCats(cats)
    }

    override fun refresh() {
        getAllCats()
    }

    fun getImg(imgName: String) {
        catsRepository.getCatImgURL(imgName)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer {
                    if (it.isSuccessful && it.body() != null) {
                        val header = it.headers().get("Content-Disposition")
                        val fileName = header!!.replace("attachment; filename=", "").replace("\"", "")
                        File(cachePath).mkdirs()
                        val imgPath = File(cachePath, fileName)
                        val bufferedSink = Okio.buffer(Okio.sink(imgPath))
                        bufferedSink.writeAll(it.body()!!.source())
                        bufferedSink.close()
                        refresh()
                    }
                })

    }

    private fun getAllCats() {
        catsRepository.geAllCats()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer {
                    if (it.isSuccessful && it.body() != null) {
                        cats.clear()
                        cats.addAll((it.body() as MutableList<Cat>?)!!)
                        cats.forEach {
                            if(it.getImage()!=null && it.getImage() != "") {
                                val image = File(cachePath, it.getImage())
                                if (!image.exists())
                                    getImg(it.imgName)
                            }
                        }
                        showCats()
                    }
                })

    }

    override fun searchCat(query: String) {
        catsRepository.searchCat(query.toLong())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer {
                    if(it.isSuccessful && it.body()!=null)
                        mainView.showCats(listOf(it.body()!!))
                })
        }




}