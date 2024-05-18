package com.asyabab.endora.data.models.user.profile

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class EditProfileResponse : Serializable {
    @SerializedName("message")
    @Expose
    var message: String? = ""

    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("data")
    @Expose
    var data: Data2? = null

    interface EditProfileResponseCallback {
        fun onSuccess(editProfileResponse: EditProfileResponse)
        fun onFailure(message: String)
    }
}