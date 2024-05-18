package com.asyabab.endora.data.models.payment.checkout

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CheckoutResponse : Serializable {
    @SerializedName("location_id")
    @Expose
    var locationId: String? = null

    @SerializedName("shipmentable_type")
    @Expose
    var shipmentableType: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("total_shipment")
    @Expose
    var totalShipment: String? = null

    @SerializedName("total")
    @Expose
    var total: String? = null

    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("user_id")
    @Expose
    var userId: Int? = null

    @SerializedName("type_uid")
    @Expose
    var typeUid: String? = null

    @SerializedName("invoice_number")
    @Expose
    var invoiceNumber: String? = null

    @SerializedName("expire_date")
    @Expose
    var expireDate: String? = null

    @SerializedName("shipmentable_id")
    @Expose
    var shipmentableId: String? = null

    @SerializedName("transaction")
    @Expose
    var transaction: Transaction? = null

}