package com.asyabab.endora.data.models.offer.detailoffer

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Pivot__2 {
    @SerializedName("offer_id")
    @Expose
    var offerId: Int? = null

    @SerializedName("offerable_id")
    @Expose
    var offerableId: Int? = null

    @SerializedName("offerable_type")
    @Expose
    var offerableType: String? = null

}