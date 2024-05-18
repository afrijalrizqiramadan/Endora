package com.asyabab.endora.data.models.lokasi.tambahalamat

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Data : Serializable {
    @SerializedName("category")
    @Expose
    var category: String? = null

    @SerializedName("receiver_name")
    @Expose
    var receiverName: String? = null

    @SerializedName("phone")
    @Expose
    var phone: String? = null

    @SerializedName("address")
    @Expose
    var address: String? = null

    @SerializedName("district")
    @Expose
    var district: String? = null

    @SerializedName("city")
    @Expose
    var city: String? = null

    @SerializedName("province")
    @Expose
    var province: String? = null

    @SerializedName("postal_code")
    @Expose
    var postalCode: String? = null

    @SerializedName("latitude")
    @Expose
    var latitude: String? = null

    @SerializedName("longitude")
    @Expose
    var longitude: String? = null

    @SerializedName("user_id")
    @Expose
    var userId: Int? = null

    @SerializedName("is_main")
    @Expose
    var isMain: String? = null

}