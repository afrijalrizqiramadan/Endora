package com.asyabab.endora.data.models.user.ubahpassword

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UbahPasswordResponse : Serializable {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: Message? = null

    @SerializedName("data")
    @Expose
    var data: Any? = null

    interface UbahPasswordResponseCallback {
        fun onSuccess(ubahPasswordResponse: UbahPasswordResponse)
        fun onFailure(message: String)
    }
}