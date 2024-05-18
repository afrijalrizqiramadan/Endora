package com.asyabab.endora.data.models.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Message : Serializable {
    @SerializedName("username")
    @Expose
    var username: List<String>? = null

    @SerializedName("password")
    @Expose
    var password: List<String>? = null

}