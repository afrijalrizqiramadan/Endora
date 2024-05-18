package com.asyabab.endora.data.models.billboard.jelajah

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class JelajahResponse : Serializable {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: List<Data>? = null

    interface JelajahResponseCallback {
        fun onSuccess(jelajahResponse: JelajahResponse)
        fun onFailure(message: String)
    }
}