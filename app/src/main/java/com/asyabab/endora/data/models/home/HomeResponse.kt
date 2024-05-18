package com.asyabab.endora.data.models.home

import com.asyabab.endora.data.models.favorit.Favoritresponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class HomeResponse : Serializable {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    interface HomeResponseCallback {
        fun onSuccess(homeResponse: HomeResponse)
        fun onFailure(message: String)
    }
}