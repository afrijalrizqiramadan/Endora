package com.asyabab.endora.data.models.payment.getpembelian

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Data : Serializable {
    @SerializedName("unpaid")
    @Expose
    var unpaid: List<Unpaid>? =
        null

    @SerializedName("packed")
    @Expose
    var packed: List<Packed>? =
        null

    @SerializedName("sent")
    @Expose
    var sent: List<Sent>? =
        null

    @SerializedName("finished")
    @Expose
    var finished: List<Finished>? =
        null

    @SerializedName("submission")
    @Expose
    var submission: List<Submission>? = null

    @SerializedName("canceled")
    @Expose
    var canceled: List<Canceled>? =
        null

}