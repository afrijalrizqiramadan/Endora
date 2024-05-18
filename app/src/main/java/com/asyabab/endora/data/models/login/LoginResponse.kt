package com.asyabab.endora.data.models.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class LoginResponse : Serializable {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

//
//    @SerializedName("message")
//    @Expose
//    var message2: Message? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    interface LoginResponseCallback {
        fun onSuccess(loginResponse: LoginResponse)
        fun onFailure(message: String)
    }
}