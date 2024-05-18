package com.asyabab.endora.ui.activity

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.asyabab.endora.R
import com.asyabab.endora.base.BaseActivity
import com.asyabab.endora.data.models.general.GeneralResponse
import com.asyabab.endora.data.models.payment.getdetailpembelian.GetDetailPembelianResponse
import com.asyabab.endora.data.models.payment.getdetailpembelian.Order
import com.asyabab.endora.data.models.payment.getpembelian.Order_
import com.asyabab.endora.data.models.payment.getpembelian.Order__
import com.asyabab.endora.data.models.payment.getpembelian.Packed
import com.asyabab.endora.data.models.payment.getpembelian.Sent
import com.asyabab.endora.data.models.payment.setsubmission.SetSubmissionResponse
import com.asyabab.endora.utils.*
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.android.synthetic.main.activity_rincianbelumbayar.*
import kotlinx.android.synthetic.main.activity_rinciandikirim.*
import kotlinx.android.synthetic.main.activity_rinciandikirim.btBack
import kotlinx.android.synthetic.main.activity_rinciandikirim.btPembayaran
import kotlinx.android.synthetic.main.activity_rinciandikirim.btPengiriman
import kotlinx.android.synthetic.main.activity_rinciandikirim.btResumePembayaran
import kotlinx.android.synthetic.main.activity_rinciandikirim.btRincianProduk
import kotlinx.android.synthetic.main.activity_rinciandikirim.btSalin
import kotlinx.android.synthetic.main.activity_rinciandikirim.iconPembayaranArrow
import kotlinx.android.synthetic.main.activity_rinciandikirim.iconPengirimanArrow
import kotlinx.android.synthetic.main.activity_rinciandikirim.iconResumePembayaranArrow
import kotlinx.android.synthetic.main.activity_rinciandikirim.iconRincianProdukArrow
import kotlinx.android.synthetic.main.activity_rinciandikirim.layoutPembayaran
import kotlinx.android.synthetic.main.activity_rinciandikirim.layoutPengiriman
import kotlinx.android.synthetic.main.activity_rinciandikirim.layoutResumePembayaran
import kotlinx.android.synthetic.main.activity_rinciandikirim.layoutRincianProduk
import kotlinx.android.synthetic.main.activity_rinciandikirim.rvGambarProduk
import kotlinx.android.synthetic.main.activity_rinciandikirim.rvRincianProduk
import kotlinx.android.synthetic.main.activity_rinciandikirim.tvBiayaPengiriman
import kotlinx.android.synthetic.main.activity_rinciandikirim.tvBiayaSubTotal
import kotlinx.android.synthetic.main.activity_rinciandikirim.tvBiayaTotal
import kotlinx.android.synthetic.main.activity_rinciandikirim.tvJumlahBarang
import kotlinx.android.synthetic.main.activity_rinciandikirim.tvKodePembayaran
import kotlinx.android.synthetic.main.activity_rinciandikirim.tvLokasiJalan
import kotlinx.android.synthetic.main.activity_rinciandikirim.tvLokasiKabupaten
import kotlinx.android.synthetic.main.activity_rinciandikirim.tvLokasiKategori
import kotlinx.android.synthetic.main.activity_rinciandikirim.tvLokasiKecamatan
import kotlinx.android.synthetic.main.activity_rinciandikirim.tvLokasiPenerima
import kotlinx.android.synthetic.main.activity_rinciandikirim.tvLokasiProvinsi
import kotlinx.android.synthetic.main.activity_rinciandikirim.tvLokasiTelepon
import kotlinx.android.synthetic.main.activity_rinciandikirim.tvLokasiUtama
import kotlinx.android.synthetic.main.activity_rinciandikirim.tvNamaPembayaran2
import kotlinx.android.synthetic.main.activity_rinciandikirim.tvNoPesanan
import kotlinx.android.synthetic.main.activity_rinciandikirim.tvSalinAlamat
import kotlinx.android.synthetic.main.activity_rinciandikirim.tvTanggalPemesanan
import kotlinx.android.synthetic.main.activity_rinciandikirim.tvTotalBiaya
import kotlinx.android.synthetic.main.activity_rinciandikirim.tvTotalBiaya2
import kotlinx.android.synthetic.main.popup_cancel.*
import kotlinx.android.synthetic.main.rv_gambarbarang.view.*
import kotlinx.android.synthetic.main.rv_rincianproduk.view.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class RincianDikirimActivity : BaseActivity() {
    var item = Sent()
    private var myClipboard: ClipboardManager? = null
    private var myClip: ClipData? = null
    var iditem = ""
    private var lat: String? = ""
    private var long: String? = ""
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
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

    private var gambarProdukAdapter: RecyclerViewAdapter<Order__> = RecyclerViewAdapter(
        R.layout.rv_gambarbarang,
        onBind = { view, data, position ->

            data.item?.images?.get(0)?.name?.let {
                view.tvGambarBarang.loadImageFromResources(
                    this@RincianDikirimActivity,
                    it
                )
            }
        })


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rinciandikirim)

        item = intent.getSerializableExtra("data") as Sent
        iditem = item.id.toString()
        tvNoPesanan.text = iditem
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
        tvSalinAlamat.onClick {
            myClipboard =
                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            myClip = ClipData.newPlainText("text", alamatlengkap)
            myClipboard!!.setPrimaryClip(myClip!!)

            Toast.makeText(this@RincianDikirimActivity, "Berhasil", Toast.LENGTH_SHORT).show();

        }

        btSalin.onClick {
            myClipboard =
                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val getstring: String = tvKodePembayaran.text.toString()

            myClip = ClipData.newPlainText("text", getstring)
            myClipboard!!.setPrimaryClip(myClip!!)

            Toast.makeText(this@RincianDikirimActivity, "Berhasil", Toast.LENGTH_SHORT).show();

        }
        btAjukanKomplain.onClick {
            setSubmission(iditem)
        }

        btPaketDiterima.onClick {
            popUpWindow(iditem)
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
        getRincianPesanan(iditem)
    }

    fun popUpWindow(id: String) {
        val dialog = Dialog(this)

        dialog?.apply {
            setContentView(R.layout.popup_konfirmasi)
            window?.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
//
            if (lat.equals("")) {
                lat = "0"
                long = "0"
            }

            btCancel.onClick { dismiss() }
            btApply.onClick {
                setConfirmation(id, lat!!, long!!)
                dismiss()
            }
            tvTitle.text = "Paket Diterima ?"


            show()
        }
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

    fun setConfirmation(
        id: String,
        latitude: String,
        longitude: String
//        image: File
    ) {
        repository!!.setConfirmation(
            id,
            latitude,
            longitude,
//            image,
            repository?.getToken()!!,
            object : GeneralResponse.GeneralResponseCallback {
                override fun onSuccess(getGeneralResponse: GeneralResponse) {
                    if (getGeneralResponse.status == true) {
                        Toast.makeText(this@RincianDikirimActivity, "Berhasil", Toast.LENGTH_LONG)
                            .show()

                    } else {
                        Toast.makeText(this@RincianDikirimActivity, "Gagal", Toast.LENGTH_LONG)
                            .show()

                    }
                }

                override fun onFailure(message: String) {
                    Toast.makeText(
                        this@RincianDikirimActivity,
                        "Server Sedang Error",
                        Toast.LENGTH_LONG
                    ).show()

                }

            })
    }

    fun setSubmission(
        id: String
    ) {
        repository!!.setSubmission(
            id,
            repository?.getToken()!!,
            object : SetSubmissionResponse.SetSubmissionResponseCallback {
                override fun onSuccess(setSubmissionResponse: SetSubmissionResponse) {
                    if (setSubmissionResponse.status == true) {
                        try {
                            val mobile = "+6283122122422"
                            val msg =
                                "Hai%20Admin%20saya%20ingin%komplain%20pesanan%20dengan%20kode%20" + iditem
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://api.whatsapp.com/send?phone=$mobile&text=$msg")
                                )
                            )
                        } catch (e: Exception) {
                            Toast.makeText(
                                this@RincianDikirimActivity,
                                "Whatsapp Belum Terinstall",
                                Toast.LENGTH_LONG
                            )
                                .show()

                        }
                    } else {
                        Toast.makeText(this@RincianDikirimActivity, "Gagal", Toast.LENGTH_LONG)
                            .show()

                    }
                }

                override fun onFailure(message: String) {
                    Toast.makeText(
                        this@RincianDikirimActivity,
                        "Server Sedang Error",
                        Toast.LENGTH_LONG
                    ).show()

                }

            })
    }
}