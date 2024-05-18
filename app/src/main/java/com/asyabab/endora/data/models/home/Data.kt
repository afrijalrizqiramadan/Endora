package com.asyabab.endora.data.models.home

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Data : Serializable {
    @SerializedName("billboard")
    @Expose
    var billboard: List<Billboard>? =
        null

    @SerializedName("offer_med")
    @Expose
    var offerMed: List<Any>? = null

    @SerializedName("offer_high")
    @Expose
    var offerHigh: List<OfferHigh>? =
        null

    @SerializedName("category")
    @Expose
    var category: List<Category>? =
        null

    @SerializedName("item")
    @Expose
    var item: List<Item>? = null

}