package com.asyabab.endora.data.models.ongkir

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Cost : Serializable {
    @SerializedName("service")
    @Expose
    var service: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("cost")
    @Expose
    var cost: List<Cost__1>? = null
}