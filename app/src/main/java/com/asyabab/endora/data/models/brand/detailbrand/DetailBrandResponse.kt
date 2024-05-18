package com.asyabab.endora.data.models.brand.detailbrand

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class DetailBrandResponse : Serializable {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    interface DetailBrandResponseCallback {
        fun onSuccess(detailBrandResponse: DetailBrandResponse)
        fun onFailure(message: String)
    }
}