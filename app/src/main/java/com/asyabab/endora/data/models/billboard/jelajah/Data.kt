package com.asyabab.endora.data.models.billboard.jelajah

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

    @SerializedName("cover_small")
    @Expose
    var coverSmall: String? = null

    @SerializedName("cover_big")
    @Expose
    var coverBig: String? = null

    @SerializedName("is_important")
    @Expose
    var isImportant: String? = null

    @SerializedName("is_active")
    @Expose
    var isActive: String? = null

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null

}