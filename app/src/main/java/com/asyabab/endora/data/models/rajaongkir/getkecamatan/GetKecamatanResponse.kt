package com.asyabab.endora.data.models.rajaongkir.getkecamatan

import com.asyabab.endora.data.models.rajaongkir.getprovinsi.GetProvinsiResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GetKecamatanResponse : Serializable {
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

    interface GetKecamatanResponseCallback {
        fun onSuccess(getKecamatanResponse: GetKecamatanResponse)
        fun onFailure(message: String)
    }
}