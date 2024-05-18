package com.asyabab.endora.data.models.user.profile

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PostProfile : Serializable {
    @SerializedName("token")
    @Expose
    var token: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("nama")
    @Expose
    var nama: String? = null

    @SerializedName("nik")
    @Expose
    var nik: String? = null

    @SerializedName("telp")
    @Expose
    var telp: String? = null

    @SerializedName("kelamin")
    @Expose
    var kelamin: String? = null

    @SerializedName("tempat")
    @Expose
    var tempat: String? = null

    @SerializedName("lahir")
    @Expose
    var lahir: String? = null

    @SerializedName("kabupaten")
    @Expose
    var kabupaten: String? = null

    @SerializedName("alamat")
    @Expose
    var alamat: String? = null

    @SerializedName("str")
    @Expose
    var str: String? = null

    @SerializedName("sip")
    @Expose
    var sip: String? = null

}