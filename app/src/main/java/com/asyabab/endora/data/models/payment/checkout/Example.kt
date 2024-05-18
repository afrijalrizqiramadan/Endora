package com.asyabab.endora.data.models.payment.checkout

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Example : Serializable {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: CheckoutResponse? = null

    interface CheckoutResponseCallback {
        fun onSuccess(checkoutResponse: CheckoutResponse)
        fun onFailure(message: String)
    }
}