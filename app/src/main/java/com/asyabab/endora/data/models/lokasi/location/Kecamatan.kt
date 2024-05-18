package com.asyabab.endora.data.models.lokasi.location

class Kecamatan {
    var id_kab: Int? = null
    var id_kec: Int? = null
    var nama: String? = null
    override fun toString(): String {
        return nama.toString()
    }
}