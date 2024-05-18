package com.asyabab.endora.ui.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.asyabab.endora.R
import com.asyabab.endora.base.BaseActivity
import com.asyabab.endora.data.models.payment.getdetailpembelian.GetDetailPembelianResponse
import com.asyabab.endora.data.models.payment.getdetailpembelian.Order
import com.asyabab.endora.data.models.payment.getpembelian.Order_
import com.asyabab.endora.data.models.payment.getpembelian.Packed
import com.asyabab.endora.utils.*
import kotlinx.android.synthetic.main.activity_rincianbelumbayar.*
import kotlinx.android.synthetic.main.activity_rinciandikemas.*
import kotlinx.android.synthetic.main.activity_rinciandikemas.btSalin
import kotlinx.android.synthetic.main.activity_rinciandikemas.tvBiayaPengiriman
import kotlinx.android.synthetic.main.activity_rinciandikemas.tvBiayaSubTotal
import kotlinx.android.synthetic.main.activity_rinciandikemas.tvBiayaTotal
import kotlinx.android.synthetic.main.activity_rinciandikemas.tvJumlahBarang
import kotlinx.android.synthetic.main.activity_rinciandikemas.tvKodePembayaran
import kotlinx.android.synthetic.main.activity_rinciandikemas.tvLokasiJalan
import kotlinx.android.synthetic.main.activity_rinciandikemas.tvLokasiKabupaten
import kotlinx.android.synthetic.main.activity_rinciandikemas.tvLokasiKategori
import kotlinx.android.synthetic.main.activity_rinciandikemas.tvLokasiKecamatan
import kotlinx.android.synthetic.main.activity_rinciandikemas.tvLokasiPenerima
import kotlinx.android.synthetic.main.activity_rinciandikemas.tvLokasiProvinsi
import kotlinx.android.synthetic.main.activity_rinciandikemas.tvLokasiTelepon
import kotlinx.android.synthetic.main.activity_rinciandikemas.tvLokasiUtama
import kotlinx.android.synthetic.main.activity_rinciandikemas.tvNamaPembayaran2
import kotlinx.android.synthetic.main.activity_rinciandikemas.tvNoPesanan
import kotlinx.android.synthetic.main.activity_rinciandikemas.tvSalinAlamat
import kotlinx.android.synthetic.main.activity_rinciandikemas.tvTanggalPemesanan
import kotlinx.android.synthetic.main.activity_rinciandikemas.tvTotalBiaya
import kotlinx.android.synthetic.main.activity_rinciandikemas.tvTotalBiaya2
import kotlinx.android.synthetic.main.activity_rincianpesanan.btBack
import kotlinx.android.synthetic.main.activity_rincianpesanan.btPembayaran
import kotlinx.android.synthetic.main.activity_rincianpesanan.btPengiriman
import kotlinx.android.synthetic.main.activity_rincianpesanan.btResumePembayaran
import kotlinx.android.synthetic.main.activity_rincianpesanan.btRincianProduk
import kotlinx.android.synthetic.main.activity_rincianpesanan.iconPembayaranArrow
import kotlinx.android.synthetic.main.activity_rincianpesanan.iconPengirimanArrow
import kotlinx.android.synthetic.main.activity_rincianpesanan.iconResumePembayaranArrow
import kotlinx.android.synthetic.main.activity_rincianpesanan.iconRincianProdukArrow
import kotlinx.android.synthetic.main.activity_rincianpesanan.layoutPembayaran
import kotlinx.android.synthetic.main.activity_rincianpesanan.layoutPengiriman
import kotlinx.android.synthetic.main.activity_rincianpesanan.layoutResumePembayaran
import kotlinx.android.synthetic.main.activity_rincianpesanan.layoutRincianProduk
import kotlinx.android.synthetic.main.activity_rincianpesanan.rvGambarProduk
import kotlinx.android.synthetic.main.activity_rincianpesanan.rvRincianProduk
import kotlinx.android.synthetic.main.rv_gambarbarang.view.*
import kotlinx.android.synthetic.main.rv_rincianproduk.view.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*


class RincianDikemasActivity : BaseActivity() {
    var item = Packed()
    private var myClipboard: ClipboardManager? = null
    private var myClip: ClipData? = null
    var id = ""
    var alamatlengkap = ""
    private var rincianProdukAdapter: RecyclerViewAdapter<Order> = RecyclerViewAdapter(
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
            view.tvRincianProdukHarga.text = data.price?.convertRupiah()
            view.onClick {
                launchActivity<DetailProdukActivity> {
                    putExtra("data", data.id.toString())
                }
            }
        })

    private var gambarProdukAdapter: RecyclerViewAdapter<Order_> = RecyclerViewAdapter(
        R.layout.rv_gambarbarang,
        onBind = { view, data, position ->

            data.item?.images?.get(0)?.name?.let {
                view.tvGambarBarang.loadImageFromResources(
                    this@RincianDikemasActivity,
                    it
                )
            }
        })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rinciandikemas)

        item = intent.getSerializableExtra("data") as Packed
        id = item.id.toString()

        tvNoPesanan.text = id
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
        tvSalinAlamat.onClick {
            myClipboard =
                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            myClip = ClipData.newPlainText("text", alamatlengkap)
            myClipboard!!.setPrimaryClip(myClip!!)

            Toast.makeText(this@RincianDikemasActivity, "Berhasil", Toast.LENGTH_SHORT).show();

        }

        btSalin.onClick {
            myClipboard =
                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val getstring: String = tvKodePembayaran.text.toString()

            myClip = ClipData.newPlainText("text", getstring)
            myClipboard!!.setPrimaryClip(myClip!!)

            Toast.makeText(this@RincianDikemasActivity, "Berhasil", Toast.LENGTH_SHORT).show();

        }
        btHubungiKami.onClick {
            try {
                val mobile = "+6283122122422"
                val msg = "Hai%20Admin%20saya%20ingin%20menanyakan%20pesanan%20dengan%20kode%20$id"
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
        rvGambarProduk.setHorizontalLayout(false)
        rvGambarProduk.adapter = gambarProdukAdapter
        loading?.show()
        getRincianPesanan(id)
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