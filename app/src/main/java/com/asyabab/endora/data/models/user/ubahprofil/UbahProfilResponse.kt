package com.asyabab.endora.data.models.user.ubahprofil

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UbahProfilResponse : Serializable {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    interface UbahProfilResponseCallback {
        fun onSuccess(ubahProfilResponse: UbahProfilResponse)
        fun onFailure(message: String)
    }
}