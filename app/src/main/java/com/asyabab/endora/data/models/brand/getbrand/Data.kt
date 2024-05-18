package com.asyabab.endora.data.models.brand.getbrand

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Data : Serializable {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("logo")
    @Expose
    var logo: String? = null

    @SerializedName("image")
    @Expose
    var image: Any? = null

    @SerializedName("is_validate")
    @Expose
    var isValidate: String? = null

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null

}