package com.asyabab.endora.ui.activity

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RelativeLayout
import android.widget.Toast
import com.asyabab.endora.R
import com.asyabab.endora.base.BaseActivity
import com.asyabab.endora.data.models.general.GeneralResponse
import com.asyabab.endora.data.models.payment.getpembelian.Unpaid
import com.asyabab.endora.utils.*
import kotlinx.android.synthetic.main.activity_pembatalanpesanan.*

import kotlinx.android.synthetic.main.popup_cancel.*
import kotlinx.android.synthetic.main.rv_gambarbarang.view.*
import java.text.NumberFormat
import java.util.*


class PembatalanPesananActivity : BaseActivity() {
    var item = Unpaid()
    var noid=""
    var temp=""
    lateinit var radioButton: RadioButton
    var radioGroupa: RadioGroup? = null
    var itext = ""

    private var gambarProdukAdapter: RecyclerViewAdapter<com.asyabab.endora.data.models.payment.getpembelian.Order> = RecyclerViewAdapter(
        R.layout.rv_gambarbarang,
        onBind = { view, data, position ->
            data.item?.images?.get(0)?.name?.let {
                view.tvGambarBarang.loadImageFromResources(this@PembatalanPesananActivity,
                    it
                )
            }
        })


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembatalanpesanan)
        radioGroupa = findViewById(R.id.radioGroup)

        item = intent.getSerializableExtra("data") as Unpaid
        noid = item.id.toString()
        tvNoPesanan.text = noid
        tvTanggalPemesanan.text = item.createdAt
        tvJumlahBarang.text = item.order?.size.toString()
        tvTotalBiaya.text = item.total?.convertRupiah()
        gambarProdukAdapter.clearItems()
        item?.order?.let { gambarProdukAdapter.addItems(it) }
        btBack.onClick {
            finish()
        }
        rvGambarProduk.setHorizontalLayout(true)
        rvGambarProduk.adapter = gambarProdukAdapter

        radioGroup?.setOnCheckedChangeListener { group, checkedId ->
            itext += if (R.id.radioBtn1 == checkedId) radioBtn1.text else radioBtn2.text
            Log.d("teshapus", itext)

        }
        btBack.onClick {
            finish()
        }
        btBatalkanPesanan.onClick {
            if (itext == "") {
                Toast.makeText(
                    applicationContext,
                    "Pilih Alasan Terlebih Dahulu",
                    Toast.LENGTH_LONG
                ).show()

            } else {
                val dialog = Dialog(context)
                dialog.apply {
                    requestWindowFeature(Window.FEATURE_NO_TITLE)
                    setContentView(R.layout.popup_cancel)
                    window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    window?.setLayout(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                    )
                    if (tvTitle != null)
                        tvTitle.text =
                            "Yakin ingin membatalkan pesanan ini?"
                    btCancel.onClick {
                        dismiss()
                    }


                    btApply.setOnClickListener {

                        setCanceled(noid.toString(), text as String)
                        dismiss()
                    }

                    show()
                }
            }

        }

    }

    fun Any.convertRupiah(): String {
        val localId = Locale("in", "ID")
        val formatter = NumberFormat.getCurrencyInstance(localId)
        formatter.maximumFractionDigits = 0;

        val strFormat = formatter.format(this)

        return strFormat
    }

    fun setCanceled(id: String, reason: String) {
        repository!!.setCanceled(
            id,
            reason,
            repository?.getToken()!!,
            object : GeneralResponse.GeneralResponseCallback {
                override fun onSuccess(generalResponse: GeneralResponse) {
                    loading?.hide()
                    if (generalResponse.status == true) {
                        Toast.makeText(applicationContext, "Berhasil", Toast.LENGTH_LONG).show()
                        launchActivity<PembelianActivity>()
                    }else{
                        Toast.makeText(applicationContext, "Gagal", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(message: String) {
                    loading?.hide()
                    Log.d("teshapus",message)

                    Toast.makeText(applicationContext, "Server Sedang Error", Toast.LENGTH_LONG).show()

                }

            })
    }
}