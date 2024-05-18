package com.asyabab.endora.data.models.courier

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Datum : Serializable {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("code")
    @Expose
    var code: String? = null

    @SerializedName("service")
    @Expose
    var service: String? = null

    @SerializedName("is_active")
    @Expose
    var is_active: String? = null

}