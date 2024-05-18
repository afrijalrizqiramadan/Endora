package com.asyabab.endora.data.models.payment.getdetailpembelian

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Review : Serializable {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("payment_id")
    @Expose
    var paymentId: String? = null

    @SerializedName("rating")
    @Expose
    var rating: Int? = null

    @SerializedName("review")
    @Expose
    var review: String? = null

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null

}