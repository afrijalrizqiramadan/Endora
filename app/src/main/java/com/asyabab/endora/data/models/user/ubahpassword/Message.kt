package com.asyabab.endora.data.models.user.ubahpassword

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Message : Serializable {
    @SerializedName("new_password")
    @Expose
    var newPassword: List<String>? = null

}