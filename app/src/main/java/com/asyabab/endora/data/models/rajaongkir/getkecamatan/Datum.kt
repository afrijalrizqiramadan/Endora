package com.asyabab.endora.data.models.rajaongkir.getkecamatan

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Datum : Serializable {
    @SerializedName("subdistrict_id")
    @Expose
    var subdistrictId: String? = null

    @SerializedName("province_id")
    @Expose
    var provinceId: String? = null

    @SerializedName("province")
    @Expose
    var province: String? = null

    @SerializedName("city_id")
    @Expose
    var cityId: String? = null

    @SerializedName("city")
    @Expose
    var city: String? = null

    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("subdistrict_name")
    @Expose
    var subdistrictName: String? = null

}