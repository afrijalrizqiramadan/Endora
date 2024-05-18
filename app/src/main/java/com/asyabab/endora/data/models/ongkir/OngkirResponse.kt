package com.asyabab.endora.data.models.ongkir

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class OngkirResponse : Serializable {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: Datum? = null

    interface OngkirResponseCallback {
        fun onSuccess(ongkirResponse: OngkirResponse)
        fun onFailure(message: String)
    }
}