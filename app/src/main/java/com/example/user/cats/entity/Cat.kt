package com.example.user.cats.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Cat(var name: String, var info: String):Serializable {
    var id: Long = 0
    @SerializedName("image")
    var imgName: String= ""

    fun getImage(): String? {
        return imgName
    }

}