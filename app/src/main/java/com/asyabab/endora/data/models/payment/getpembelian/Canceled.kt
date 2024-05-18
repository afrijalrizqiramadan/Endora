package com.asyabab.endora.data.models.payment.getpembelian

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Canceled: Serializable {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("user_id")
    @Expose
    var userId: Int? = null

    @SerializedName("location_id")
    @Expose
    var locationId: Int? = null

    @SerializedName("shipmentable_id")
    @Expose
    var shipmentableId: Int? = null

    @SerializedName("shipmentable_type")
    @Expose
    var shipmentableType: String? = null

    @SerializedName("invoice_number")
    @Expose
    var invoiceNumber: String? = null

    @SerializedName("waybill_number")
    @Expose
    var waybillNumber: Any? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("total_shipment")
    @Expose
    var totalShipment: Int? = null

    @SerializedName("total")
    @Expose
    var total: Int? = null

    @SerializedName("status_payment")
    @Expose
    var statusPayment: String? = null

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null

    @SerializedName("cancel")
    @Expose
    var cancel: Cancel? = null

    @SerializedName("order")
    @Expose
    var order: List<Order____>? = null

}