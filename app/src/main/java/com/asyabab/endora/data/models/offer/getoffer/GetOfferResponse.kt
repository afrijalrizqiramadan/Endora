package com.asyabab.endora.data.models.offer.getoffer

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GetOfferResponse : Serializable {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: List<Data>? = null

    interface GetOfferCallback {
        fun onSuccess(getOfferResponse: GetOfferResponse)
        fun onFailure(message: String)
    }
}