package com.asyabab.endora.ui.activity

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.asyabab.endora.R
import com.asyabab.endora.base.BaseActivity
import com.asyabab.endora.data.models.general.GeneralResponse
import com.asyabab.endora.data.models.offer.detailoffer.Category
import com.asyabab.endora.data.models.offer.detailoffer.DetailOfferResponse
import com.asyabab.endora.data.models.offer.detailoffer.Item
import com.asyabab.endora.ui.activity.cart.TasBelanjaActivity
import com.asyabab.endora.utils.*
import kotlinx.android.synthetic.main.activity_detailoffer.*
import kotlinx.android.synthetic.main.activity_detailoffer.framemain
import kotlinx.android.synthetic.main.activity_detailoffer.frameshimmer
import kotlinx.android.synthetic.main.activity_detailoffer.tvIconNumber
import kotlinx.android.synthetic.main.activity_detailoffer.*
import kotlinx.android.synthetic.main.activity_detailoffer.btBack
import kotlinx.android.synthetic.main.activity_detailoffer.rvKategori
import kotlinx.android.synthetic.main.activity_detailoffer.swipeResfresh
import kotlinx.android.synthetic.main.activity_ubahkatasandi1.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.popup_lanjut.*
import kotlinx.android.synthetic.main.rv_kategori.view.*
import kotlinx.android.synthetic.main.rv_produk.view.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*


class DetailOfferActivity : BaseActivity() {
    var item = ""
    var keterangaN = ""
    var nama = ""
    var gambar = ""
    private var mRequestPermissionHandler: RequestPermissionHandler? = null

    private var detailProdukAdapter: RecyclerViewAdapter<Item> = RecyclerViewAdapter(
        R.layout.rv_produk,
        onBind = { view, data, position ->
            view.tvNama.text = data.name
            view.tvHarga.text = data.price?.convertRupiah()
//            view.tvKeterangan.text = data.description
//            data.brandId?.let { view.tvGambar.loadImageFromResources(this, it) }
            if (data.promo?.isEmpty()!!) {
                view.tvKeteranganPromo.visibility = View.INVISIBLE
                view.tvKeterangan.visibility = View.INVISIBLE
                view.tvHarga.text = data.price?.convertRupiah()

            } else {
                var hasil = data.price!! * data.promo!![0].discount!! / 100
                view.tvKeterangan.visibility = View.VISIBLE
                view.tvKeteranganPromo.visibility = View.VISIBLE
                if (hasil < data.promo!![0].maximum!!) {
                    view.tvHarga.text = (data.price!! - hasil).convertRupiah()
                } else {
                    view.tvHarga.text = (data.price!! - data.promo!![0].maximum!!).convertRupiah()

                }
                view.tvKeteranganPromo.text = data.price?.convertRupiah()
                view.tvKeterangan.text = " Diskon " + data.promo!![0].discount!! + "% "

            }
            if (data.isFavorite!!) {
                this?.let { view.btIconFavorit.loadImageFromResources(it, R.drawable.icon_lovered) }
            } else {
                this?.let {
                    view.btIconFavorit.loadImageFromResources(
                        it,
                        R.drawable.icon_lovewhite
                    )
                }

            }
            view.btIconFavorit.onClick {
                repository!!.setFavorit(
                    data.id.toString(),
                    repository?.getToken()!!,
                    object : GeneralResponse.GeneralResponseCallback {
                        override fun onSuccess(generalResponse: GeneralResponse) {
                            if (generalResponse.status == true) {
                                if (generalResponse.message == "Item Favorit Berhasil Dihapus!!") {
                                    Toast.makeText(
                                        context,
                                        generalResponse.message,
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                    context?.let {
                                        view.btIconFavorit.loadImageFromResources(
                                            it,
                                            R.drawable.icon_lovewhite
                                        )
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        generalResponse.message,
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                    view.btIconFavorit.loadImageFromResources(
                                        context,
                                        R.drawable.icon_lovered
                                    )
                                }
                            } else {
                                Toast.makeText(context, "Gagal Menambahkan", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }

                        override fun onFailure(message: String) {
                            Log.e("Hasil", "Gagal Memuat" + message)

                            Toast.makeText(context, "Gagal Memuat" + message, Toast.LENGTH_SHORT)
                                .show()
                        }

                    })
            }
            data.images?.get(0)?.name?.let { view.tvGambar.loadImageFromResources(this, it) }
            view.onClick {
                launchActivity<DetailProdukActivity> {
                    putExtra("data", data.id.toString())
                }
            }
        })


    private var kategoriAdapter: RecyclerViewAdapter<Category> = RecyclerViewAdapter(
        R.layout.rv_kategori,
        onBind = { view, data, position ->
            data.image?.let {
                view.tvKategoriGambar.loadImageFromResources(
                    this,
                    it
                )
            }
            view.tvKategoriNama.text = data.name

            view.onClick {
                val intent = Intent(context, JelajahActivity::class.java)
                intent.putExtra("strings1", data.id.toString())
                intent.putExtra("strings2", data.name.toString())
                startActivityForResult(intent, Activity.RESULT_OK);
            }
        })


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailoffer)
        mRequestPermissionHandler = RequestPermissionHandler()
        item = intent.getSerializableExtra("data").toString()
        val nama = intent.getSerializableExtra("nama").toString()

        btTasBelanja.onClick {
            launchActivity<TasBelanjaActivity> { }
        }
        btSearch.onClick {
            launchActivity<PencarianActivity> { }
        }

        btBack.onClick {
            finish()
        }


//        btKeteranganBrand.onClick {
//            popUpWindow()
//        }
        tvTitle.text = nama
        tvIconNumber?.text = repository!!.getData("jumlahkeranjang")
        val layoutManager = GridLayoutManager(this, 2)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rvBarangOffer.layoutManager = layoutManager
        rvBarangOffer.adapter = detailProdukAdapter

        rvKategori.setHorizontalLayout(false)
        rvKategori.adapter = kategoriAdapter
        getDetailOffer(item)

        swipeResfresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            getDetailOffer(item)


            swipeResfresh.isRefreshing = false;

        })
    }


    fun popUpWindow() {
        val dialog = Dialog(this)

        dialog.apply {
            setContentView(R.layout.popup_lanjut)
            window?.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            btKeluar.onClick { dismiss() }
            tvGambarPopUp.loadImageFromResources(this@DetailOfferActivity, gambar)
            tvKeteranganPopUp.text = keterangaN
            tvNamaPopUp.text = nama


            show()
        }
    }
    fun Any.convertRupiah(): String {
        val df = DecimalFormat("#,###,##0")


        val strFormat = df.format(this)
        var bilangan = "Rp " + strFormat
        return bilangan
    }

    fun getDetailOffer(id: String) {
        repository!!.getDetailOffer(
            id,
            repository?.getToken()!!,
            object : DetailOfferResponse.DetailOfferResponseCallback {
                override fun onSuccess(detailOfferResponse: DetailOfferResponse) {
                    if (detailOfferResponse.status == true) {
//                        if (keranjangResponse.data?.adaartikel == "ADA") {
                        detailProdukAdapter.clearItems()
                        detailOfferResponse.data?.items?.let { detailProdukAdapter.addItems(it) }
                        kategoriAdapter.clearItems()
                        detailOfferResponse.data?.categories?.let { kategoriAdapter.addItems(it) }
//                        tvDeskripsiBrand.text = detailOfferResponse.data?.description
//                        keterangan = detailOfferResponse.data?.description.toString()
//                        nama = detailOfferResponse.data?.name.toString()
//                        gambar = detailOfferResponse.data?.image.toString()
//                        detailOfferResponse.data?.image?.let {
//                            tvBackgoundBrand.loadImageFromResources(
//                                this@DetailOfferActivity,
//                                it
//                            )
//                        }
//                        detailOfferResponse.data?.logo?.let {
//                            tvLogoBrand.loadImageFromResources(
//                                this@DetailOfferActivity,
//                                it
//                            )
//                        }
//                        tvJumlahProduk.text =
//                            detailOfferResponse.data?.items?.size.toString() + " Produk"
//                        tvNamaBrand.text = detailOfferResponse.data?.name
                        framemain.visibility = View.VISIBLE
                        frameshimmer.visibility = View.GONE
//                        } else {
//
//                        }
                    }
                }

                override fun onFailure(message: String) {
                    Toast.makeText(applicationContext, "Server Sedang Error", Toast.LENGTH_LONG)
                        .show()

                }

            })
    }


}

