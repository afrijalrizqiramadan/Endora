package com.asyabab.endora.data.database

import android.content.Context
import android.database.sqlite.SQLiteQueryBuilder
import android.util.Log
import com.asyabab.endora.data.models.lokasi.location.Kabupaten
import com.asyabab.endora.data.models.lokasi.location.Kecamatan
import com.asyabab.endora.data.models.lokasi.location.Provinsi
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper

/**
 * Created by SILVA on 2019/05/19.
 */
class Database(context: Context?) : SQLiteAssetHelper(context, DB_NAME, null, DB_VER) {

    fun getProvinsi(): ArrayList<Provinsi> {
        val db = readableDatabase
        val qb = SQLiteQueryBuilder()
        val sqlSelect = arrayOf("id_prov", "nama")
        val sqlTable = "tbl_provinsi"
        qb.tables = sqlTable
        val c = qb.query(db, sqlSelect, null, null, null, null, "nama ASC")
        val result: ArrayList<Provinsi> = ArrayList()
        if (c.moveToFirst()) {
            do {
                var provinsi = Provinsi()
                provinsi.id_prov = c.getInt(c.getColumnIndex(sqlSelect[0]))
                provinsi.nama = c.getString(c.getColumnIndex(sqlSelect[1]))
                Log.d("hasil", c.getString(c.getColumnIndex(sqlSelect[0])).toString())
                Log.d("hasil", c.getString(c.getColumnIndex(sqlSelect[1])).toString())
                result.add(
                    provinsi
                )
            } while (c.moveToNext())
        }
        return result
    }

    fun getKota(idProv: Int): ArrayList<Kabupaten> {
        val db = readableDatabase
        val qb = SQLiteQueryBuilder()
        val sqlSelect = arrayOf("id_kab", "id_prov", "nama")
        val sqlTable = "tbl_kabupaten"
        val args = arrayOf(idProv.toString() + "")
        qb.tables = sqlTable
        val c = qb.query(db, sqlSelect, "id_prov=?", args, null, null, "nama ASC")
        val result: ArrayList<Kabupaten> = ArrayList()
        if (c.moveToFirst()) {
            do {
                var kabupaten = Kabupaten()
                kabupaten.id_kab = c.getInt(c.getColumnIndex(sqlSelect[0]))
                kabupaten.id_prov = c.getInt(c.getColumnIndex(sqlSelect[1]))
                kabupaten.nama = c.getString(c.getColumnIndex(sqlSelect[2]))
                result.add(
                    kabupaten
                )
            } while (c.moveToNext())
        }
        return result
    }

    fun getKecamatan(idKota: Int): ArrayList<Kecamatan> {
        val db = readableDatabase
        val qb = SQLiteQueryBuilder()
        val sqlSelect = arrayOf("id_kec", "id_kab", "nama")
        val sqlTable = "tbl_kecamatan"
        val args = arrayOf(idKota.toString() + "")
        qb.tables = sqlTable
        val c = qb.query(db, sqlSelect, "id_kab=?", args, null, null, "nama ASC")
        val result: ArrayList<Kecamatan> = ArrayList()
        if (c.moveToFirst()) {
            do {
                var kecamatan = Kecamatan()
                kecamatan.id_kec = c.getInt(c.getColumnIndex(sqlSelect[0]))
                kecamatan.id_kab = c.getInt(c.getColumnIndex(sqlSelect[1]))
                kecamatan.nama = c.getString(c.getColumnIndex(sqlSelect[2]))
                result.add(
                    kecamatan
                )
            } while (c.moveToNext())
        }
        return result
    }

    fun removeFromCart(order: String) {
        val db = readableDatabase
        val query = String.format("DELETE FROM OrderDetail WHERE ProductId='$order'")
        db.execSQL(query)
    }

    fun cleanCart() {
        val db = readableDatabase
        val query = String.format("DELETE FROM OrderDetail")
        db.execSQL(query)
    }

    companion object {
        private const val DB_NAME = "location"
        private const val DB_VER = 1
    }
}