package com.asyabab.endora.data.models.payment.getdetailpembelian

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Order : Serializable {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("payment_id")
    @Expose
    var paymentId: String? = null

    @SerializedName("item_id")
    @Expose
    var itemId: Int? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("price")
    @Expose
    var price: Int? = null

    @SerializedName("qty")
    @Expose
    var qty: Int? = null

    @SerializedName("variant")
    @Expose
    var variant: String? = null

    @SerializedName("t_weight")
    @Expose
    private var tWeight: String? = null

    @SerializedName("discount")
    @Expose
    var discount: Any? = null

    @SerializedName("total")
    @Expose
    var total: Int? = null

    @SerializedName("note")
    @Expose
    var note: String? = null

    @SerializedName("item")
    @Expose
    var item: Item? = null

    fun gettWeight(): String? {
        return tWeight
    }

    fun settWeight(tWeight: String?) {
        this.tWeight = tWeight
    }

}