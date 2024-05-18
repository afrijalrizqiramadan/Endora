package com.asyabab.endora.data.models.rajaongkir.getprovinsi

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Datum : Serializable {
    @SerializedName("province_id")
    @Expose
    var provinceId: String? = null

    @SerializedName("province")
    @Expose
    var province: String? = null

}