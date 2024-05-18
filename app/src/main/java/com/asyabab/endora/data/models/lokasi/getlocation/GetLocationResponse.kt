package com.asyabab.endora.data.models.lokasi.getlocation

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GetLocationResponse : Serializable {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: List<Datum>? =
        null
    interface GetLocationResponseCallback {
        fun onSuccess(getLocationResponse: GetLocationResponse)
        fun onFailure(message: String)
    }
}