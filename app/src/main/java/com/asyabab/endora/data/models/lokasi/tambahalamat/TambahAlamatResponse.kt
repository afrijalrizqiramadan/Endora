package com.asyabab.endora.data.models.lokasi.tambahalamat

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TambahAlamatResponse : Serializable {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null
    interface TambahAlamatResponseCallback {
        fun onSuccess(tambahAlamatResponse: TambahAlamatResponse)
        fun onFailure(message: String)
    }
}