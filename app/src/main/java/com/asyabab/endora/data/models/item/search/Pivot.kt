package com.asyabab.endora.data.models.item.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Pivot : Serializable {
    @SerializedName("item_id")
    @Expose
    var itemId: Int? = null

    @SerializedName("category_id")
    @Expose
    var categoryId: Int? = null

}