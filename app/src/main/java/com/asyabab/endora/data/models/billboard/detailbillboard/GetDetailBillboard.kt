package com.asyabab.endora.data.models.billboard.detailbillboard

import com.asyabab.endora.data.models.billboard.jelajah.JelajahResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GetDetailBillboard : Serializable {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    interface GetDetailBillboardResponseCallback {
        fun onSuccess(getDetailBillboard: GetDetailBillboard)
        fun onFailure(message: String)
    }
}