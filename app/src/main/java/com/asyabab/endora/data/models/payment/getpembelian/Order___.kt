package com.asyabab.endora.data.models.payment.getpembelian

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Order___: Serializable {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("payment_id")
    @Expose
    var paymentId: String? = null

    @SerializedName("item_id")
    @Expose
    var itemId: Int? = null

    @SerializedName("item")
    @Expose
    var item: Item___? = null

}