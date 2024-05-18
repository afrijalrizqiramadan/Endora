package com.asyabab.endora.ui.activity

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import com.asyabab.endora.R
import com.asyabab.endora.base.BaseActivity
import com.asyabab.endora.data.models.general.GeneralResponse
import com.asyabab.endora.data.models.lokasi.getlocation.Datum
import com.asyabab.endora.data.models.lokasi.getlocation.GetLocationResponse
import com.asyabab.endora.data.models.lokasi.getmain.GetMainResponse
import kotlinx.android.synthetic.main.rv_location.view.*
import com.asyabab.endora.utils.RecyclerViewAdapter
import com.asyabab.endora.utils.launchActivity
import com.asyabab.endora.utils.onClick
import com.asyabab.endora.utils.setVerticalLayout
import kotlinx.android.synthetic.main.activity_aturalamat.*
import kotlinx.android.synthetic.main.activity_aturalamat.btBack
import kotlinx.android.synthetic.main.rv_location.view.btRadio

class AturAlamatActivity : BaseActivity() {
    lateinit var handler: Handler

    private var getLocationAdapter: RecyclerViewAdapter<Datum> = RecyclerViewAdapter(
        R.layout.rv_location,
        onBind = { view, data, position ->
            view.tvNamaPenerima.text = data.receiverName
            view.tvKategori.text = data.category
            view.tvNomerTelepon.text = data.phone
            view.tvAlamat.text = data.address
            view.tvKecamatan.text = data.detail?.subdistrictName
            view.tvKabupaten.text = data.detail?.city
            view.tvProvinsi.text = data.detail?.province
            if (data.isMain?.equals("1")!!) {
                view.iconClose.visibility = View.GONE
                view.icon_maps.setImageResource(R.drawable.pinmaps)
                view.tvMain.visibility = View.VISIBLE
                view.tvKategori.setTextColor(resources.getColor(R.color.white))
                view.panel.setBackgroundColor(resources.getColor(R.color.blue))
                view.btRadio.isChecked = true
            } else {
                view.btRadio.isChecked = false
                view.iconClose.visibility = View.VISIBLE
                view.tvMain.visibility = View.GONE
                view.icon_maps.setImageResource(R.drawable.pinmapsblack)
                view.tvKategori.setTextColor(resources.getColor(R.color.blackgray))
                view.panel.setBackgroundColor(resources.getColor(R.color.white))
            }
            view.iconClose.onClick {
                loading?.show()
                hapusLocation(data.id.toString())
            }
            view.btRadio.onClick {
                loading?.show()
                setMain(data.id.toString())
            }
            view.onClick {
                launchActivity<TambahAlamatActivity> {
                    putExtra("data", data)
                    finish()
                }
            }
        })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aturalamat)
        btBack.onClick {
            finish()
        }
        btTambahAlamat.onClick {
            launchActivity<TambahAlamatActivity>()
            finish()
        }
        rvLocation.setVerticalLayout(true)
        rvLocation.adapter = getLocationAdapter
        loading?.show()
        getLocation()
    }


    fun getLocation() {
        repository!!.getLocation(
            repository?.getToken()!!,
            object : GetLocationResponse.GetLocationResponseCallback {
                override fun onSuccess(getLocationResponse: GetLocationResponse) {
                    if (getLocationResponse.status == true) {
                        loading?.hide()
                        Log.d("UbahProfil", "signInsuccess")
                        getLocationAdapter.clearItems()
                        getLocationAdapter.addItems(getLocationResponse.data!!)

                    }
                }

                override fun onFailure(message: String) {
                    loading?.hide()
                    Toast.makeText(applicationContext, "Server Sedang Error", Toast.LENGTH_LONG).show()
                }

            })
    }

    fun setMain(id: String) {
        repository!!.setMain(
            id,
            repository?.getToken()!!,
            object : GeneralResponse.GeneralResponseCallback {
                override fun onSuccess(generalResponse: GeneralResponse) {
                    if (generalResponse.status == true) {
                        getLocation()
                        getMain()
                        Toast.makeText(this@AturAlamatActivity, "Berhasil", Toast.LENGTH_SHORT)
                            .show()

                    }
                }

                override fun onFailure(message: String) {
                    Log.d("UbahProfil", "signInsuccess" + message)
                    Toast.makeText(applicationContext, "Server Sedang Error", Toast.LENGTH_LONG)
                        .show()

                }

            })
    }

    fun getMain() {
        repository!!.getMain(
            repository?.getToken()!!,
            object : GetMainResponse.GetMainResponseCallback {
                override fun onSuccess(getMainResponse: GetMainResponse) {
                    if (getMainResponse.status == true) {
                        repository!!.saveLokasi(getMainResponse.data?.subdistrictId.toString())
                        repository!!.saveLokasiKeterangan(getMainResponse.data?.category)
                    } else {
                        repository!!.saveLokasi("")

                    }
                }

                override fun onFailure(message: String) {
                    Log.e("Hasil", "Gagal Memuat" + message)


                }

            })
    }

    fun hapusLocation(id: String) {
        repository!!.hapusLocation(
            id,
            repository?.getToken()!!,
            object : GeneralResponse.GeneralResponseCallback {
                override fun onSuccess(generalResponse: GeneralResponse) {
                    if (generalResponse.status == true) {
                        getLocation()
                        Toast.makeText(this@AturAlamatActivity, "Berhasil", Toast.LENGTH_SHORT)
                            .show()

                    }
                }

                override fun onFailure(message: String) {
                    Toast.makeText(applicationContext, "Server Sedang Error", Toast.LENGTH_LONG).show()

                    Log.d("UbahProfil", "signInsuccess" + message)
                }

            })
    }
}