package com.asyabab.endora.data.models.lokasi.location

class Kabupaten {
    var id_kab: Int? = null
    var id_prov: Int? = null
    var nama: String? = null
    override fun toString(): String {
        return nama.toString()
    }
}