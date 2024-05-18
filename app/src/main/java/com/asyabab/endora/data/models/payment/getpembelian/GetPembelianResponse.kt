package com.asyabab.endora.data.models.payment.getpembelian

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GetPembelianResponse: Serializable {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    interface GetPembelianResponseCallback {
        fun onSuccess(getPembelianResponse: GetPembelianResponse)
        fun onFailure(message: String)
    }
}