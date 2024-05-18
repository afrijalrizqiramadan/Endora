package com.asyabab.endora.data.models.lokasi.getmain

import com.asyabab.endora.data.models.lokasi.getlocation.GetLocationResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GetMainResponse : Serializable {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    interface GetMainResponseCallback {
        fun onSuccess(getMainResponse: GetMainResponse)
        fun onFailure(message: String)
    }
}