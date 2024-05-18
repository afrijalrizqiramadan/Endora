package com.asyabab.endora.data.models.user.profile

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Profile2 : Serializable {
    @SerializedName("nama")
    @Expose
    var nama: String? = null

    @SerializedName("nik")
    @Expose
    var nik: String? = null

    @SerializedName("tempat")
    @Expose
    var tempat: String? = null

    @SerializedName("lahir")
    @Expose
    var lahir: String? = null

    @SerializedName("kabupaten")
    @Expose
    var kabupaten: String? = null

    @SerializedName("kecamatan")
    @Expose
    var kecamatan: String? = null

    @SerializedName("alamat")
    @Expose
    var alamat: String? = null

    @SerializedName("telp")
    @Expose
    var telp: String? = null

    @SerializedName("kelamin")
    @Expose
    var kelamin: String? = null

    @SerializedName("pekerjaan")
    @Expose
    var pekerjaan: String? = null

    @SerializedName("nikah")
    @Expose
    var nikah: String? = null

    @SerializedName("foto")
    @Expose
    var foto: String? = null

    companion object {
        private const val serialVersionUID = 4626929911362936980L
    }
}