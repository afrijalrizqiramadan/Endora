package com.asyabab.endora.data.models.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class LoginResponse2 : Serializable {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: Any? = null

    interface LoginResponse2Callback {
        fun onSuccess(loginResponse2: LoginResponse2)
        fun onFailure(message: String)
    }
}