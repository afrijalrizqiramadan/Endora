package com.asyabab.endora.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.asyabab.endora.R
import com.asyabab.endora.base.BaseActivity
import com.asyabab.endora.data.models.payment.getdetailpembelian.GetDetailPembelianResponse
import com.asyabab.endora.data.models.payment.getpembelian.*
import com.asyabab.endora.utils.*
import kotlinx.android.synthetic.main.activity_rincianbelumbayar.*
import kotlinx.android.synthetic.main.activity_rinciankomplain.*
import kotlinx.android.synthetic.main.activity_rinciankomplain.btBack
import kotlinx.android.synthetic.main.activity_rinciankomplain.btHubungiKami
import kotlinx.android.synthetic.main.activity_rinciankomplain.btPembayaran
import kotlinx.android.synthetic.main.activity_rinciankomplain.btPengiriman
import kotlinx.android.synthetic.main.activity_rinciankomplain.btResumePembayaran
import kotlinx.android.synthetic.main.activity_rinciankomplain.btRincianProduk
import kotlinx.android.synthetic.main.activity_rinciankomplain.iconPembayaranArrow
import kotlinx.android.synthetic.main.activity_rinciankomplain.iconPengirimanArrow
import kotlinx.android.synthetic.main.activity_rinciankomplain.iconResumePembayaranArrow
import kotlinx.android.synthetic.main.activity_rinciankomplain.iconRincianProdukArrow
import kotlinx.android.synthetic.main.activity_rinciankomplain.layoutPembayaran
import kotlinx.android.synthetic.main.activity_rinciankomplain.layoutPengiriman
import kotlinx.android.synthetic.main.activity_rinciankomplain.layoutResumePembayaran
import kotlinx.android.synthetic.main.activity_rinciankomplain.layoutRincianProduk
import kotlinx.android.synthetic.main.activity_rinciankomplain.rvGambarProduk
import kotlinx.android.synthetic.main.activity_rinciankomplain.rvRincianProduk
import kotlinx.android.synthetic.main.activity_rinciankomplain.tvBiayaPengiriman
import kotlinx.android.synthetic.main.activity_rinciankomplain.tvBiayaSubTotal
import kotlinx.android.synthetic.main.activity_rinciankomplain.tvBiayaTotal
import kotlinx.android.synthetic.main.activity_rinciankomplain.tvJumlahBarang
import kotlinx.android.synthetic.main.activity_rinciankomplain.tvKodePembayaran
import kotlinx.android.synthetic.main.activity_rinciankomplain.tvLokasiJalan
import kotlinx.android.synthetic.main.activity_rinciankomplain.tvLokasiKabupaten
import kotlinx.android.synthetic.main.activity_rinciankomplain.tvLokasiKategori
import kotlinx.android.synthetic.main.activity_rinciankomplain.tvLokasiKecamatan
import kotlinx.android.synthetic.main.activity_rinciankomplain.tvLokasiPenerima
import kotlinx.android.synthetic.main.activity_rinciankomplain.tvLokasiProvinsi
import kotlinx.android.synthetic.main.activity_rinciankomplain.tvLokasiTelepon
import kotlinx.android.synthetic.main.activity_rinciankomplain.tvLokasiUtama
import kotlinx.android.synthetic.main.activity_rinciankomplain.tvNamaPembayaran2
import kotlinx.android.synthetic.main.activity_rinciankomplain.tvNoPesanan
import kotlinx.android.synthetic.main.activity_rinciankomplain.tvTanggalPemesanan
import kotlinx.android.synthetic.main.activity_rinciankomplain.tvTotalBiaya
import kotlinx.android.synthetic.main.activity_rinciankomplain.tvTotalBiaya2

import kotlinx.android.synthetic.main.rv_gambarbarang.view.*
import kotlinx.android.synthetic.main.rv_rincianproduk.view.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class RincianKomplainActivity : BaseActivity() {
    var item = Submission()
    var itemid = ""
    var alamatlengkap = ""

    private var rincianProdukAdapter: RecyclerViewAdapter<com.asyabab.endora.data.models.payment.getdetailpembelian.Order> =
        RecyclerViewAdapter(
            R.layout.rv_rincianproduk,
            onBind = { view, data, position ->
                view.tvRincianProdukNama.text = data.name
                view.tvRincianProdukKeterangan.text = data.variant
                data.item?.images?.get(0)?.name?.let {
                    view.tvRincianProdukGambar.loadImageFromResources(
                        this,
                        it
                    )
                }
                view.tvRincianProdukJumlah.text = data.qty.toString()
                view.tvRincianProdukHarga.text = data.price.toString()
                view.onClick {
                    launchActivity<DetailProdukActivity> {
                        putExtra("data", data.id.toString())
                    }
                }
            })

    private var gambarProdukAdapter: RecyclerViewAdapter<Order___> = RecyclerViewAdapter(
        R.layout.rv_gambarbarang,
        onBind = { view, data, position ->

            data.item?.images?.get(0)?.name?.let {
                view.tvGambarBarang.loadImageFromResources(
                    this@RincianKomplainActivity,
                    it
                )
            }
        })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rinciankomplain)

        item = intent.getSerializableExtra("data") as Submission
        itemid = item.id.toString()
        tvNoPesanan.text = itemid
        tvTanggalPemesanan.text = item.createdAt
        tvJumlahBarang.text = item.order?.size.toString()
        tvTotalBiaya.text = item.total?.convertRupiah()
        gambarProdukAdapter.clearItems()
        item?.order?.let { gambarProdukAdapter.addItems(it) }
        btBack.onClick {
            finish()
        }

        btRincianProduk.onClick {
            if (layoutRincianProduk.visibility == View.VISIBLE) {
                iconRincianProdukArrow.setImageResource(R.drawable.arrowright)
                layoutRincianProduk.visibility = View.GONE
            } else {
                layoutRincianProduk.visibility = View.VISIBLE
                iconRincianProdukArrow.setImageResource(R.drawable.arrowtop)
            }
        }

        btPengiriman.onClick {
            if (layoutPengiriman.visibility == View.VISIBLE) {
                iconPengirimanArrow.setImageResource(R.drawable.arrowright)
                layoutPengiriman.visibility = View.GONE
            } else {
                layoutPengiriman.visibility = View.VISIBLE
                iconPengirimanArrow.setImageResource(R.drawable.arrowtop)
            }
        }
        btPembayaran.onClick {
            if (layoutPembayaran.visibility == View.VISIBLE) {
                iconPembayaranArrow.setImageResource(R.drawable.arrowright)
                layoutPembayaran.visibility = View.GONE
            } else {
                layoutPembayaran.visibility = View.VISIBLE
                iconPembayaranArrow.setImageResource(R.drawable.arrowtop)
            }
        }
        btHubungiKami.onClick {
            try {
                val mobile = "+6283122122422"
                val msg =
                    "Hai%20Admin%20saya%20ingin%20menanyakan%20pesanan%20dengan%20kode%20$itemid"
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://api.whatsapp.com/send?phone=$mobile&text=$msg")
                    )
                )
            } catch (e: Exception) {
                Toast.makeText(applicationContext, "Whatsapp Belum Terinstall", Toast.LENGTH_LONG)
                    .show()

            }
        }
        btResumePembayaran.onClick {
            if (layoutResumePembayaran.visibility == View.VISIBLE) {
                iconResumePembayaranArrow.setImageResource(R.drawable.arrowright)
                layoutResumePembayaran.visibility = View.GONE
            } else {
                layoutResumePembayaran.visibility = View.VISIBLE
                iconResumePembayaranArrow.setImageResource(R.drawable.arrowtop)
            }
        }
        rvRincianProduk.setVerticalLayout(true)
        rvRincianProduk.adapter = rincianProdukAdapter
        rvGambarProduk.setHorizontalLayout(true)
        rvGambarProduk.adapter = gambarProdukAdapter
        loading?.show()
        getRincianPesanan(itemid)
    }

    fun Any.convertRupiah(): String {
        val df = DecimalFormat("#,###,##0")


        val strFormat = df.format(this)
        var bilangan = "Rp " + strFormat
        return bilangan
    }

    fun getRincianPesanan(id: String) {
        repository!!.getRincianPesanan(
            id,
            repository?.getToken()!!,
            object : GetDetailPembelianResponse.GetDetailPembelianResponseCallback {
                override fun onSuccess(getDetailPembelianResponse: GetDetailPembelianResponse) {
                    if (getDetailPembelianResponse.status == true) {
                        loading?.hide()
//                        if (keranjangResponse.data?.adaartikel == "ADA") {
                        rincianProdukAdapter.clearItems()
                        getDetailPembelianResponse.data?.order?.let {
                            rincianProdukAdapter.addItems(
                                it
                            )
                        }
                        if (getDetailPembelianResponse.data?.location?.isMain.equals("1")) {
                            tvLokasiUtama.text = "[Utama]"
                        } else {
                            tvLokasiUtama.text = ""
                        }
                        tvLokasiKategori.text = getDetailPembelianResponse.data?.location?.category
                        tvLokasiTelepon.text = getDetailPembelianResponse.data?.location?.phone
                        tvLokasiJalan.text = getDetailPembelianResponse.data?.location?.address
                        tvLokasiKecamatan.text =
                            getDetailPembelianResponse.data?.location?.detail?.subdistrictName
                        tvLokasiKabupaten.text =
                            getDetailPembelianResponse.data?.location?.detail?.city
                        tvLokasiProvinsi.text =
                            getDetailPembelianResponse.data?.location?.detail?.province
                        tvLokasiPenerima.text =
                            getDetailPembelianResponse.data?.location?.receiverName
                        tvTotalBiaya2.text =
                            getDetailPembelianResponse.data?.total?.convertRupiah()
                        tvKodePembayaran.text = getDetailPembelianResponse.data?.invoiceNumber
                        var namapembayaran = ""
                        if (getDetailPembelianResponse.data?.type.toString() == "xendit-bni") {
                            namapembayaran = " BNI Virtual Account"
                        } else if (getDetailPembelianResponse.data?.type.toString() == "xendit-bri") {
                            namapembayaran = " BRI Virtual Account"

                        } else if (getDetailPembelianResponse.data?.type.toString() == "xendit-mandiri") {
                            namapembayaran = " Mandiri Virtual Account"
                        } else if (getDetailPembelianResponse.data?.type.toString() == "bca-va") {
                            namapembayaran = " BCA Virtual Account"
                        }
                        tvNamaPembayaran2.text = namapembayaran

                        alamatlengkap =
                            getDetailPembelianResponse.data?.location?.receiverName + ", " + getDetailPembelianResponse.data?.location?.address + ", " + getDetailPembelianResponse.data?.location?.district + ", " + getDetailPembelianResponse.data?.location?.city + ", " + getDetailPembelianResponse.data?.location?.province
                        var subtotal = getDetailPembelianResponse.data?.total?.minus(
                            getDetailPembelianResponse.data?.totalShipment!!
                        )
                        tvBiayaSubTotal.text = subtotal?.convertRupiah()
                        tvBiayaPengiriman.text =
                            getDetailPembelianResponse.data?.totalShipment?.convertRupiah()
//                        tvBiayaTransaksi.text=getDetailPembelianResponse.data?.invoiceNumber
                        tvBiayaTotal.text = getDetailPembelianResponse.data?.total?.convertRupiah()
//                        } else {
//
//                        }
                    }
                }

                override fun onFailure(message: String) {
                    loading?.hide()

                    Toast.makeText(applicationContext, "Server Sedang Error", Toast.LENGTH_LONG)
                        .show()

                }

            })
    }
}