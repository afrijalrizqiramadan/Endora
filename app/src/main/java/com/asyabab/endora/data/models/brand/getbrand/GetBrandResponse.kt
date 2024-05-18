package com.asyabab.endora.data.models.brand.getbrand

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GetBrandResponse : Serializable {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: List<Data>? = null

    interface GetBrandResponseCallback {
        fun onSuccess(getBrandResponse: GetBrandResponse)
        fun onFailure(message: String)
    }
}