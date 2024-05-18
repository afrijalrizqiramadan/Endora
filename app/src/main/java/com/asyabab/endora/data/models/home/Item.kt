package com.asyabab.endora.data.models.home

import com.asyabab.endora.data.models.Varian
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Item : Serializable {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("brand_id")
    @Expose
    var brandId: Int? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("price")
    @Expose
    var price: Int? = null

    @SerializedName("variant")
    @Expose
    var variant: List<String>? = null

    @SerializedName("inventory")
    @Expose
    var inventory: Int? = null

    @SerializedName("weight")
    @Expose
    var weight: Int? = null

    @SerializedName("is_favorite")
    @Expose
    var favorite: Boolean? = null

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null

    @SerializedName("categories")
    @Expose
    var categories: List<Category_>? = null

    @SerializedName("images")
    @Expose
    var images: List<Image>? = null

}