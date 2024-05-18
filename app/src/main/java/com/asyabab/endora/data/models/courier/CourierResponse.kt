package com.asyabab.endora.data.models.courier

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CourierResponse : Serializable {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: List<Datum>? = null

    interface CourierResponseCallback {
        fun onSuccess(courierResponse: CourierResponse)
        fun onFailure(message: String)
    }
}
