package com.asyabab.endora.data.models.register

import com.asyabab.endora.data.models.login.LoginResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RegisterResponse2 {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: Message? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    interface RegisterResponse2Callback {
        fun onSuccess(registerResponse2: RegisterResponse2)
        fun onFailure(message: String)
    }
}