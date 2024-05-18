package com.asyabab.endora.ui.activity

import android.app.Dialog
import android.content.*
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.LinearLayout
import android.widget.RatingBar.OnRatingBarChangeListener
import android.widget.Toast
import com.asyabab.endora.R
import com.asyabab.endora.base.BaseActivity
import com.asyabab.endora.data.models.general.GeneralResponse
import com.asyabab.endora.data.models.payment.getdetailpembelian.GetDetailPembelianResponse
import com.asyabab.endora.data.models.payment.getdetailpembelian.Order
import com.asyabab.endora.data.models.payment.getpembelian.Finished
import com.asyabab.endora.data.models.payment.getpembelian.Order___
import com.asyabab.endora.utils.*
import kotlinx.android.synthetic.main.activity_rincianbelumbayar.*
import kotlinx.android.synthetic.main.activity_rincianselesai.*
import kotlinx.android.synthetic.main.activity_rincianselesai.btBack
import kotlinx.android.synthetic.main.activity_rincianselesai.btPembayaran
import kotlinx.android.synthetic.main.activity_rincianselesai.btPengiriman
import kotlinx.android.synthetic.main.activity_rincianselesai.btResumePembayaran
import kotlinx.android.synthetic.main.activity_rincianselesai.btRincianProduk
import kotlinx.android.synthetic.main.activity_rincianselesai.btSalin
import kotlinx.android.synthetic.main.activity_rincianselesai.iconPembayaranArrow
import kotlinx.android.synthetic.main.activity_rincianselesai.iconPengirimanArrow
import kotlinx.android.synthetic.main.activity_rincianselesai.iconResumePembayaranArrow
import kotlinx.android.synthetic.main.activity_rincianselesai.iconRincianProdukArrow
import kotlinx.android.synthetic.main.activity_rincianselesai.layoutPembayaran
import kotlinx.android.synthetic.main.activity_rincianselesai.layoutPengiriman
import kotlinx.android.synthetic.main.activity_rincianselesai.layoutResumePembayaran
import kotlinx.android.synthetic.main.activity_rincianselesai.layoutRincianProduk
import kotlinx.android.synthetic.main.activity_rincianselesai.rvGambarProduk
import kotlinx.android.synthetic.main.activity_rincianselesai.rvRincianProduk
import kotlinx.android.synthetic.main.activity_rincianselesai.tvBiayaPengiriman
import kotlinx.android.synthetic.main.activity_rincianselesai.tvBiayaSubTotal
import kotlinx.android.synthetic.main.activity_rincianselesai.tvBiayaTotal
import kotlinx.android.synthetic.main.activity_rincianselesai.tvJumlahBarang
import kotlinx.android.synthetic.main.activity_rincianselesai.tvKodePembayaran
import kotlinx.android.synthetic.main.activity_rincianselesai.tvLokasiJalan
import kotlinx.android.synthetic.main.activity_rincianselesai.tvLokasiKabupaten
import kotlinx.android.synthetic.main.activity_rincianselesai.tvLokasiKategori
import kotlinx.android.synthetic.main.activity_rincianselesai.tvLokasiKecamatan
import kotlinx.android.synthetic.main.activity_rincianselesai.tvLokasiPenerima
import kotlinx.android.synthetic.main.activity_rincianselesai.tvLokasiProvinsi
import kotlinx.android.synthetic.main.activity_rincianselesai.tvLokasiTelepon
import kotlinx.android.synthetic.main.activity_rincianselesai.tvLokasiUtama
import kotlinx.android.synthetic.main.activity_rincianselesai.tvNamaPembayaran2
import kotlinx.android.synthetic.main.activity_rincianselesai.tvNoPesanan
import kotlinx.android.synthetic.main.activity_rincianselesai.tvSalinAlamat
import kotlinx.android.synthetic.main.activity_rincianselesai.tvTanggalPemesanan
import kotlinx.android.synthetic.main.activity_rincianselesai.tvTotalBiaya
import kotlinx.android.synthetic.main.activity_rincianselesai.tvTotalBiaya2
import kotlinx.android.synthetic.main.popup_lanjut.btKeluar
import kotlinx.android.synthetic.main.popup_ulasan.*
import kotlinx.android.synthetic.main.rv_gambarbarang.view.*
import kotlinx.android.synthetic.main.rv_rincianproduk.view.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*


class RincianSelesaiActivity : BaseActivity() {
    var item = Finished()
    var itemid = ""
    var alamatlengkap = ""
    private var myClipboard: ClipboardManager? = null
    private var myClip: ClipData? = null
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
                    this@RincianSelesaiActivity,
                    it
                )
            }
        })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rincianselesai)

        item = intent.getSerializableExtra("data") as Finished
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

        tvSalinAlamat.onClick {
            myClipboard =
                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            myClip = ClipData.newPlainText("text", alamatlengkap)
            myClipboard!!.setPrimaryClip(myClip!!)

            Toast.makeText(this@RincianSelesaiActivity, "Berhasil", Toast.LENGTH_SHORT).show();

        }

        btSalin.onClick {
            myClipboard =
                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val getstring: String = tvKodePembayaran.text.toString()

            myClip = ClipData.newPlainText("text", getstring)
            myClipboard!!.setPrimaryClip(myClip!!)

            Toast.makeText(this@RincianSelesaiActivity, "Berhasil", Toast.LENGTH_SHORT).show();

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


        inputUlasan.addTextChangedListener(mTextEditorWatcher);

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

        penilaian.onRatingBarChangeListener = OnRatingBarChangeListener { ratingBar, nilai, b ->

        }
        btDetailPesanan.onClick {
            launchActivity<RincianPesananActivity> {
                putExtra("data", item)

            }
        }

        btKirimUlasan.setOnClickListener(View.OnClickListener {

            if (penilaian.rating.toString() == "0.0") {
                Toast.makeText(
                    applicationContext,
                    "Pilih Rating Terlebih Dahulu",
                    Toast.LENGTH_LONG
                )
                    .show()

            } else if (inputUlasan.text.toString() == "") {
                Toast.makeText(applicationContext, "Masukkan Ulasan Anda", Toast.LENGTH_LONG)
                    .show()

            } else {
                setUlasan(itemid, penilaian.rating.toString(), inputUlasan.text.toString())

            }


        })



        rvRincianProduk.setVerticalLayout(true)
        rvRincianProduk.adapter = rincianProdukAdapter
        rvGambarProduk.setHorizontalLayout(false)
        rvGambarProduk.adapter = gambarProdukAdapter
        loading?.show()
        getRincianPesanan(itemid)
    }

    val mTextEditorWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(
            s: CharSequence,
            start: Int,
            count: Int,
            after: Int
        ) {
        }

        override fun onTextChanged(
            s: CharSequence,
            start: Int,
            before: Int,
            count: Int
        ) {
            //This sets a textview to the current length
            tvLiveText.text = "Tersisa " + (200 - s.length).toString()
        }

        override fun afterTextChanged(s: Editable) {}
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
                        if (getDetailPembelianResponse.data?.review?.review.equals(null)) {
                            btProdukOriginal.onClick {
                                inputUlasan.setText(inputUlasan.text.toString() + "Produk Original, ")

                            }
                            btPengirimanCepat.onClick {
                                inputUlasan.setText(inputUlasan.text.toString() + btPengirimanCepat.text)

                            }

                            btHargaBaik.onClick {
                                inputUlasan.setText(inputUlasan.text.toString() + btHargaBaik.text)

                            }
                            btResponBaik.onClick {
                                inputUlasan.setText(inputUlasan.text.toString() + btResponBaik.text)

                            }
                        } else {
                            inputUlasan.setText(getDetailPembelianResponse.data?.review?.review)
                            inputUlasan.isEnabled = false
                            penilaian.rating =
                                getDetailPembelianResponse.data?.review?.rating?.toFloat()!!
                            penilaian.setIsIndicator(true);
                            btKirimUlasan.visibility = View.GONE
                        }

                    }
                }

                override fun onFailure(message: String) {
                    loading?.hide()

                    Toast.makeText(applicationContext, "Server Sedang Error", Toast.LENGTH_LONG)
                        .show()

                }

            })
    }

    fun setUlasan(id: String, rating: String, review: String) {
        repository!!.setUlasan(
            id,
            rating,
            review,
            repository?.getToken()!!,
            object : GeneralResponse.GeneralResponseCallback {
                override fun onSuccess(generalResponse: GeneralResponse) {
                    if (generalResponse.status == true) {
                        loading?.hide()

                        popUpWindow()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Penilaian Gagal",
                            Toast.LENGTH_SHORT
                        ).show()


                    }
                }

                override fun onFailure(message: String) {
                    loading?.hide()

                    Toast.makeText(applicationContext, "Server Sedang Error", Toast.LENGTH_LONG)
                        .show()

                }

            })
    }

    fun popUpWindow() {
        val dialog = Dialog(this)

        dialog.apply {
            setContentView(R.layout.popup_ulasan)
            window?.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            btKeluar.onClick { launchActivityWithNewTask<BerandaActivity>() }

            btRatingAplikasi.onClick {
                try {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=$packageName")
                        )
                    )
                } catch (e: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                        )
                    )
                }
            }

            show()
        }
    }


}