package com.asyabab.endora.data.models.payment.getdetailpembelian

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Data : Serializable {
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

    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("type_uid")
    @Expose
    var typeUid: String? = null

    @SerializedName("invoice_number")
    @Expose
    var invoiceNumber: String? = null

    @SerializedName("waybill_number")
    @Expose
    var waybillNumber: String? = null

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

    @SerializedName("expire_date")
    @Expose
    var expireDate: String? = null

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null

    @SerializedName("status_shipment")
    @Expose
    var statusShipment: Int? = null

    @SerializedName("shipmentable")
    @Expose
    var shipmentable: Shipmentable? =
        null

    @SerializedName("location")
    @Expose
    var location: Location? = null

    @SerializedName("order")
    @Expose
    var order: List<Order>? =
        null

    @SerializedName("histories")
    @Expose
    var histories: List<Any>? = null

    @SerializedName("review")
    @Expose
    var review: Review? = null

}