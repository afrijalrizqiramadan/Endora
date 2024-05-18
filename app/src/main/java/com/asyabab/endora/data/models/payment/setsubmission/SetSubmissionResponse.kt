package com.asyabab.endora.data.models.payment.setsubmission

import com.asyabab.endora.data.models.payment.getpembelian.GetPembelianResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SetSubmissionResponse : Serializable {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    interface SetSubmissionResponseCallback {
        fun onSuccess(SetSubmissionResponse: SetSubmissionResponse)
        fun onFailure(message: String)
    }
}