package com.asyabab.endora.data.models.user.changephoto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ChangePhotoResponse : Serializable {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    interface ChangePhotoResponseCallback {
        fun onSuccess(changePhotoResponse: ChangePhotoResponse)
        fun onFailure(message: String)
    }
}