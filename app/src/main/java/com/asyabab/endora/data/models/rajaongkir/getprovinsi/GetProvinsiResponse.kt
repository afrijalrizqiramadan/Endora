package com.asyabab.endora.data.models.rajaongkir.getprovinsi

import com.asyabab.endora.data.models.register.RegisterResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GetProvinsiResponse : Serializable {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: List<Datum>? =
        null

    interface GetProvinsiResponseCallback {
        fun onSuccess(getProvinsiResponse: GetProvinsiResponse)
        fun onFailure(message: String)
    }
}