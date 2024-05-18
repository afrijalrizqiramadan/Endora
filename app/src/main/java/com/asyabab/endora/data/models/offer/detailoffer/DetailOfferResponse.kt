package com.asyabab.endora.data.models.offer.detailoffer

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DetailOfferResponse {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    interface DetailOfferResponseCallback {
        fun onSuccess(detailOfferResponse: DetailOfferResponse)
        fun onFailure(message: String)
    }
}