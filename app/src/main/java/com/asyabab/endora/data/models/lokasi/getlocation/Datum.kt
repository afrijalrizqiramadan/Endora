package com.asyabab.endora.data.models.lokasi.getlocation

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Datum : Serializable {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("user_id")
    @Expose
    var userId: Int? = null

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

    @SerializedName("subdistrict_id")
    @Expose
    var subdistrictId: Int? = null

    @SerializedName("postal_code")
    @Expose
    var postalCode: Int? = null

    @SerializedName("description")
    @Expose
    var description: Any? = null

    @SerializedName("latitude")
    @Expose
    var latitude: String? = null

    @SerializedName("longitude")
    @Expose
    var longitude: String? = null

    @SerializedName("is_main")
    @Expose
    var isMain: String? = null

    @SerializedName("deleted_at")
    @Expose
    var deletedAt: Any? = null

    @SerializedName("detail")
    @Expose
    var detail: Detail? = null

}