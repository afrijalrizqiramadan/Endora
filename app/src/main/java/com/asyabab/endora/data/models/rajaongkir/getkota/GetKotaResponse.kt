package com.asyabab.endora.data.models.rajaongkir.getkota

import com.asyabab.endora.data.models.rajaongkir.getprovinsi.GetProvinsiResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GetKotaResponse : Serializable {
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

    interface GetKotaResponseCallback {
        fun onSuccess(getKotaResponse: GetKotaResponse)
        fun onFailure(message: String)
    }
}