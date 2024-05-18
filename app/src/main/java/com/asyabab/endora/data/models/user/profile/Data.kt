package com.asyabab.endora.data.models.user.profile

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Data : Serializable {
    @SerializedName("nama")
    @Expose
    var nama: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("str")
    @Expose
    var str: String? = null

    @SerializedName("foto")
    @Expose
    var foto = ""
}