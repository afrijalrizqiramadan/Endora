package com.asyabab.endora.data.models.user.profile

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Data2 : Serializable {
    @SerializedName("profile")
    @Expose
    var profile: Profile2? = null

    @SerializedName("kota")
    @Expose
    var kota: List<String>? = null

}