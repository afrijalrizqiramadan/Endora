package com.asyabab.endora.data.models.user.changephoto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Data : Serializable {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("username")
    @Expose
    var username: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("email_verified_at")
    @Expose
    var emailVerifiedAt: Any? = null

    @SerializedName("remember_token")
    @Expose
    var rememberToken: Any? = null

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