package com.asyabab.endora.data.models.payment.getdetailpembelian

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GetDetailPembelianResponse : Serializable {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null
    interface GetDetailPembelianResponseCallback {
        fun onSuccess(getDetailPembelianResponse: GetDetailPembelianResponse)
        fun onFailure(message: String)
    }
}