package com.asyabab.endora.data.models.user.getsearch

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GetSearchResponse : Serializable {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: List<Data>? = null

    interface GetSearchResponeCallback {
        fun onSuccess(getSearchResponse: GetSearchResponse)
        fun onFailure(message: String)
    }
}