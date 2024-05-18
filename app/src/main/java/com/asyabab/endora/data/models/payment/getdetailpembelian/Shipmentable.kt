package com.asyabab.endora.data.models.payment.getdetailpembelian

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Shipmentable : Serializable {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("fullname")
    @Expose
    var fullname: String? = null

    @SerializedName("gender")
    @Expose
    var gender: String? = null

    @SerializedName("number_plate")
    @Expose
    var numberPlate: String? = null

    @SerializedName("date_birth")
    @Expose
    var dateBirth: String? = null

    @SerializedName("image")
    @Expose
    var image: String? = null

    @SerializedName("token_fcm")
    @Expose
    var tokenFcm: String? = null

    @SerializedName("device_id")
    @Expose
    var deviceId: String? = null

    @SerializedName("is_active")
    @Expose
    var isActive: String? = null

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null

}