package com.asyabab.endora.data.models.brand.detailbrand

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

    @SerializedName("price")
    @Expose
    var price: Int? = null

    @SerializedName("weight")
    @Expose
    var weight: Int? = null

    @SerializedName("is_favorite")
    @Expose
    var favorite: Boolean? = null

    @SerializedName("images")
    @Expose
    var images: List<Image>? =
        null

    @SerializedName("promo")
    @Expose
    var promo: List<Promo>? = null


}