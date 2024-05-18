package com.asyabab.endora.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.get
import com.asyabab.endora.R
import com.asyabab.endora.base.BaseActivity
import com.asyabab.endora.data.models.lokasi.getlocation.Datum
import com.asyabab.endora.data.models.lokasi.location.Kabupaten
import com.asyabab.endora.data.models.lokasi.location.Kecamatan
import com.asyabab.endora.data.models.lokasi.location.Provinsi
import com.asyabab.endora.data.models.lokasi.tambahalamat.Data
import com.asyabab.endora.data.models.lokasi.tambahalamat.TambahAlamatResponse
import com.asyabab.endora.data.models.rajaongkir.getkecamatan.GetKecamatanResponse
import com.asyabab.endora.data.models.rajaongkir.getkota.GetKotaResponse
import com.asyabab.endora.data.models.rajaongkir.getprovinsi.GetProvinsiResponse
import com.asyabab.endora.utils.launchActivity
import com.asyabab.endora.utils.launchActivityWithNewTask
import com.asyabab.endora.utils.onClick
import kotlinx.android.synthetic.main.activity_tambahalamat.*


class TambahAlamatActivity : BaseActivity() {
    var item = Datum()
    var listProvinsi = ArrayList<Provinsi>()
    var listKota = ArrayList<Kabupaten>()
    var listKecamatan = ArrayList<Kecamatan>()
    var lat = ""
    var long=""
    var kecamatan=""
    var provinsi = ""
    var idkecamatan = ""
    var kabupaten = ""
    var idn=""

    companion object{
        var lat=""
        var long=""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambahalamat)
        if (intent.getSerializableExtra("data") != null) {
            item = intent.getSerializableExtra("data") as Datum
            inputLabel.setText(item.category)
            inputNamaPenerima.setText(item.receiverName)
            inputTelepon.setText(item.phone)
            inputAlamat.setText(item.address)
            inputKodePos.setText(item.postalCode.toString())
            lat = item.latitude.toString()
            long = item.longitude.toString()
            idn = item.id.toString()
        }


        btSimpan.onClick {
            if (inputLabel.text.toString() == ""){
                Toast.makeText(this@TambahAlamatActivity, "Isi Label", Toast.LENGTH_SHORT)
                    .show()
            }else if(inputNamaPenerima.text.toString() == ""){
                Toast.makeText(this@TambahAlamatActivity, "Isi Nama Penerima", Toast.LENGTH_SHORT)
                    .show()
            }else if(inputTelepon.text.toString() == ""){
                Toast.makeText(this@TambahAlamatActivity, "Isi No Telepon", Toast.LENGTH_SHORT)
                    .show()
            }
            else if(inputAlamat.text.toString() == "") {
                Toast.makeText(this@TambahAlamatActivity, "Isi Alamat", Toast.LENGTH_SHORT)
                    .show()
            }else if(inputKodePos.text.toString() == ""){
                Toast.makeText(this@TambahAlamatActivity, "Isi Kode Pos", Toast.LENGTH_SHORT)
                    .show()
            }else if (provinsi==""){
                Toast.makeText(this@TambahAlamatActivity, "Pilih Provinsi Terlebih Dahulu", Toast.LENGTH_SHORT)
                    .show()
            }else if (kabupaten==""){
                Toast.makeText(this@TambahAlamatActivity, "Pilih Kabupaten/Kota Terlebih Dahulu", Toast.LENGTH_SHORT)
                    .show()
            }else if (kecamatan==""){
                Toast.makeText(this@TambahAlamatActivity, "Pilih Kecamatan Terlebih Dahulu", Toast.LENGTH_SHORT)
                    .show()
            }else if (lat==""){
                Toast.makeText(this@TambahAlamatActivity, "Pilih Maps Terlebih Dahulu", Toast.LENGTH_SHORT)
                    .show()
            }else{
                loading?.show()

                if (intent.getSerializableExtra("data") != null) {
                    repository?.getToken()?.let {
                        setUpdateAlamat(
                            idn,
                            inputLabel.text.toString(),
                            inputNamaPenerima.text.toString(),
                            inputTelepon.text.toString(),
                            inputAlamat.text.toString(),
                            kecamatan,
                            idkecamatan,
                            kabupaten,
                            provinsi,
                            inputKodePos.text.toString(),
                            lat,
                            long,
                            it
                        )
                    }
                }else{
                    repository?.getToken()?.let {
                        setTambahAlamat(
                            inputLabel.text.toString(),
                            inputNamaPenerima.text.toString(),
                            inputTelepon.text.toString(),
                            inputAlamat.text.toString(),
                            kecamatan,
                            idkecamatan,
                            kabupaten,
                            provinsi,
                            inputKodePos.text.toString(),
                            lat,
                            long,
                            it
                        )
                    }
                }

            }
        }
        btBack.onClick {
            finish()
        }
        btMaps.onClick {

                if (intent.getSerializableExtra("data") != null) {
                    launchActivity<MapsActivity>(1) {
                        item = intent.getSerializableExtra("data") as Datum
                        putExtra("latitude", item.latitude.toString())
                        putExtra("longitude", item.longitude.toString())

                    }
                } else {
                    launchActivity<MapsActivity>(1)
                }
        }
        loading?.show()
//        listProvinsi = repository?.getProvinsi()!!
        getProvinsi()


        val dataAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            listProvinsi
        )
        spProvinsi.adapter = dataAdapter
             
        spProvinsi?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                loading?.show()

                getKota(listProvinsi[position].id_prov.toString())
//                listKota = repository?.getKota(listProvinsi[position].id_prov?.toInt()!!)!!
                val dataAdapter = ArrayAdapter(
                    this@TambahAlamatActivity,
                    android.R.layout.simple_spinner_item,
                    listKota

                )
                provinsi = listProvinsi[position].nama.toString()

                spKota.adapter = dataAdapter

            }

        }

        spKota?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                loading?.show()

                getKecamatan(listKota[position].id_kab.toString())

//                listKecamatan = repository?.getKecamatan(listKota[position].id_kab!!)!!
                val dataAdapter = ArrayAdapter(
                    this@TambahAlamatActivity,
                    android.R.layout.simple_spinner_item,
                    listKecamatan
                )
                kabupaten = listKota[position].nama.toString()

                spKecamatan.adapter = dataAdapter

            }
        }

        spKecamatan?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                kecamatan = listKecamatan[position].nama.toString()
                idkecamatan = listKecamatan[position].id_kec.toString()

            }
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                lat = data?.getStringExtra("lat").toString()
                long = data?.getStringExtra("long").toString()
            }
        }
    }

    fun setTambahAlamat(
        kategori: String,
        receiver_name: String,
        phone: String,
        address: String,
        district: String,
        districtid: String,
        city: String,
        province: String,
        postal_code: String,
        latitude: String,
        longitude: String,
        auth: String
    ) {
        repository?.setTambahAlamat(
            kategori,
            receiver_name,
            phone,
            address,
            district,
            districtid,
            city,
            province,
            postal_code,
            latitude,
            longitude,
            auth,
            object : TambahAlamatResponse.TambahAlamatResponseCallback {
                override fun onSuccess(tambahAlamatResponse: TambahAlamatResponse) {
                    Log.d("Ubah", "signInsuccess")
                    loading?.dismiss()
                    if (tambahAlamatResponse.status == true) {
                        launchActivity<AturAlamatActivity>()
                        finish()
                    } else {
                        Toast.makeText(
                            this@TambahAlamatActivity,
                            "Gagal Menambahkan",
                            Toast.LENGTH_SHORT
                        )
                            .show()

                    }
                }

                override fun onFailure(message: String) {
                    Log.d("Login", message)
                    loading?.dismiss()
                    Toast.makeText(applicationContext, "Server Sedang Error", Toast.LENGTH_LONG)
                        .show()
                }

            })
    }

    fun setUpdateAlamat(
        id: String,
        kategori: String,
        receiver_name: String,
        phone: String,
        address: String,
        district: String,
        districtid: String,
        city: String,
        province: String,
        postal_code: String,
        latitude: String,
        longitude: String,
        auth: String
    ) {
        repository?.setUpdateAlamat(
            id,
            kategori,
            receiver_name,
            phone,
            address,
            district,
            districtid,
            city,
            province,
            postal_code,
            latitude,
            longitude,
            auth,
            object : TambahAlamatResponse.TambahAlamatResponseCallback {
                override fun onSuccess(tambahAlamatResponse: TambahAlamatResponse) {
                    Log.d("Ubah", "signInsuccess")
                    loading?.dismiss()
                    if (tambahAlamatResponse.status == true) {
                        launchActivity<AturAlamatActivity> { }
                        finish()
                    } else {
                        Toast.makeText(this@TambahAlamatActivity, "Gagal Memperbarui", Toast.LENGTH_SHORT)
                            .show()

                    }
                }

                override fun onFailure(message: String) {
                    Log.d("Login", message)
                    loading?.dismiss()
                    Toast.makeText(applicationContext, "Server Sedang Error", Toast.LENGTH_LONG)
                        .show()
                }

            })
    }

    fun getProvinsi() {
        repository!!.getProvinsi(
            repository?.getToken()!!,
            object : GetProvinsiResponse.GetProvinsiResponseCallback {
                override fun onSuccess(getProvinsiResponse: GetProvinsiResponse) {
                    listProvinsi.clear()
                    loading?.dismiss()
                    if (getProvinsiResponse.status == true) {
                        for (n in getProvinsiResponse.data?.indices!!) {
                            val id: String? = getProvinsiResponse.data!![n].provinceId
                            val prov: String? = getProvinsiResponse.data!![n].province

                            var provinsi = Provinsi()
                            provinsi.id_prov = id?.toInt()
                            provinsi.nama = prov?.toString().toString()
                            listProvinsi.add(
                                provinsi
                            )
                        }
                    }
                }

                override fun onFailure(message: String) {
                    Toast.makeText(applicationContext, "Gagal Memuat", Toast.LENGTH_LONG)
                        .show()

                }

            })
    }

    fun getKecamatan(id: String) {
        repository!!.getKecamatan(
            id,
            repository?.getToken()!!,
            object : GetKecamatanResponse.GetKecamatanResponseCallback {
                override fun onSuccess(getKecamatanResponse: GetKecamatanResponse) {
                    listKecamatan.clear()
                    loading?.dismiss()

                    if (getKecamatanResponse.status == true) {
                        for (n in getKecamatanResponse.data?.indices!!) {
                            val id: String? = getKecamatanResponse.data!![n].subdistrictId
                            val prov: String? = getKecamatanResponse.data!![n].subdistrictName

                            var provinsi = Kecamatan()
                            provinsi.id_kec = id?.toInt()
                            provinsi.nama = prov?.toString().toString()

                            listKecamatan.add(
                                provinsi
                            )
                        }
                    }
                }

                override fun onFailure(message: String) {
                    Toast.makeText(applicationContext, "Gagal Memuat", Toast.LENGTH_LONG)
                        .show()

                }

            })
    }

    fun getKota(id: String) {
        repository!!.getKota(
            id,
            repository?.getToken()!!,
            object : GetKotaResponse.GetKotaResponseCallback {
                override fun onSuccess(getKotaResponse: GetKotaResponse) {
                    listKota.clear()
                    loading?.dismiss()

                    if (getKotaResponse.status == true) {
                        for (n in getKotaResponse.data?.indices!!) {
                            val id: String? = getKotaResponse.data!![n].cityId
                            val prov: String? = getKotaResponse.data!![n].cityName

                            var kota = Kabupaten()
                            kota.id_kab = id?.toInt()
                            kota.nama = prov?.toString().toString()

                            listKota.add(
                                kota
                            )
                        }
                    }
                }

                override fun onFailure(message: String) {
                    Toast.makeText(applicationContext, "Gagal Memuat", Toast.LENGTH_LONG)
                        .show()

                }

            })
    }

}