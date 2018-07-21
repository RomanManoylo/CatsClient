package com.example.user.cats.login

import com.example.user.cats.entity.Cat
import com.example.user.cats.repository.CatsRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class AddEditPresenter(val catsRepository: CatsRepository, val addEditView: AddEditContract.View) : AddEditContract.Presenter {
    override fun start() {

    }

    override fun addCat(cat: Cat, image: String) {
        var bodyImg: MultipartBody.Part? = null

        val f = File(image)
        val rbImg = RequestBody.create(MediaType.parse("multipart/form-data"), f)
        bodyImg = MultipartBody.Part.createFormData("image", f.name, rbImg)



        catsRepository.addCat(cat, bodyImg)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer {
                    if (it.isSuccessful) {
                        addEditView.savingCompleated()
                    }
                })
    }


}
