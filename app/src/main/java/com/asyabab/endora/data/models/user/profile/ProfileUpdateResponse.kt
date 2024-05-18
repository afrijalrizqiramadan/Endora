package com.asyabab.endora.data.models.user.profile

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ProfileUpdateResponse : Serializable {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("url")
    @Expose
    var url: String? = null

    interface ProfileUpdateResponseCallback {
        fun onSuccess(profileUpdateResponse: ProfileUpdateResponse)
        fun onFailure(message: String)
    }
}