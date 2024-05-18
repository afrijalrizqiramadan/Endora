package com.asyabab.endora.data.models.favorit

import com.asyabab.endora.data.models.login.LoginResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Favoritresponse : Serializable {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: List<Data>? = null

    interface FavoritResponseCallback {
        fun onSuccess(favoritresponse: Favoritresponse)
        fun onFailure(message: String)
    }
}