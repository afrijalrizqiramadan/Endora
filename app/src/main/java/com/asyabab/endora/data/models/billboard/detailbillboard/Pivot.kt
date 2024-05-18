package com.asyabab.endora.data.models.billboard.detailbillboard

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Pivot : Serializable {
    @SerializedName("billboard_id")
    @Expose
    var billboardId: Int? = null

    @SerializedName("contentable_id")
    @Expose
    var contentableId: Int? = null

    @SerializedName("contentable_type")
    @Expose
    var contentableType: String? = null

}