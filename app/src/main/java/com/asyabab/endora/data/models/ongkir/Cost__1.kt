package com.asyabab.endora.data.models.ongkir

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Cost__1 : Serializable {
    @SerializedName("value")
    @Expose
    var value: Int? = null

    @SerializedName("etd")
    @Expose
    var etd: String? = null

    @SerializedName("note")
    @Expose
    var note: String? = null
}