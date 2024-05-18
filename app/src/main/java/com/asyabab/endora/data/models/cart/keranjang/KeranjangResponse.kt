package com.asyabab.endora.data.models.cart.keranjang

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class KeranjangResponse : Serializable {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: List<Data>? = null

    interface KeranjangResponseCallback {
        fun onSuccess(keranjangResponse: KeranjangResponse)
        fun onFailure(message: String)
    }
}