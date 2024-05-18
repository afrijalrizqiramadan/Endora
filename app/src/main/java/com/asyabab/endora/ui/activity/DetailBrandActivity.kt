package com.asyabab.endora.ui.activity

import android.app.Dialog
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
import com.asyabab.endora.data.models.brand.detailbrand.DetailBrandResponse
import com.asyabab.endora.data.models.brand.detailbrand.Item
import com.asyabab.endora.data.models.general.GeneralResponse
import com.asyabab.endora.ui.activity.cart.TasBelanjaActivity
import com.asyabab.endora.utils.*
import kotlinx.android.synthetic.main.activity_detailbrand.*
import kotlinx.android.synthetic.main.activity_detailbrand.framemain
import kotlinx.android.synthetic.main.activity_detailbrand.frameshimmer
import kotlinx.android.synthetic.main.activity_detailbrand.swipeResfresh
import kotlinx.android.synthetic.main.fragment_hasilpencarian.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.popup_filter.*
import kotlinx.android.synthetic.main.popup_lanjut.*
import kotlinx.android.synthetic.main.popup_urutkan.*
import kotlinx.android.synthetic.main.rv_produk.view.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*


class DetailBrandActivity : BaseActivity() {
    var item = ""
    var keteranganN = ""
    var nama = ""
    var gambar = ""
    private var mRequestPermissionHandler: RequestPermissionHandler? = null

    private var detailBrandAdapter: RecyclerViewAdapter<Item> = RecyclerViewAdapter(
        R.layout.rv_produk,
        onBind = { view, data, position ->
            view.tvNama.text = data.name
            view.tvHarga.text = data.price?.convertRupiah()
//            view.tvKeterangan.text = data.description
//            data.brandId?.let { view.tvGambar.loadImageFromResources(this, it) }
            if (data.favorite!!) {
                this?.let { view.btIconFavorit.loadImageFromResources(it, R.drawable.icon_lovered) }
            } else {
                this?.let {
                    view.btIconFavorit.loadImageFromResources(
                        it,
                        R.drawable.icon_lovewhite
                    )
                }

            }
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

    fun Any.convertRupiah(): String {
        val df = DecimalFormat("#,###,##0")


        val strFormat = df.format(this)
        var bilangan = "Rp " + strFormat
        return bilangan
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailbrand)
        mRequestPermissionHandler = RequestPermissionHandler()
        item = intent.getSerializableExtra("data").toString()

        btKeteranganBrand.onClick {
            popUpWindow()
        }

        btTasBelanja.onClick {
            launchActivity<TasBelanjaActivity>()
        }

        btSearch.onClick {
            launchActivity<PencarianActivity>()
        }
//        btFilter.onClick {
//            popUpFilter()
//        }
        btUrutkan.onClick {
            popUpUrutkan()
        }
        btBack.onClick {
            onBackPressed()
        }


        tvIconNumber?.text = repository!!.getData("jumlahkeranjang")
        val layoutManager = GridLayoutManager(this, 2)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rvBarangBrand.layoutManager = layoutManager
        rvBarangBrand.adapter = detailBrandAdapter

        getDetailBrand(item)

        swipeResfresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            getDetailBrand(item)

            swipeResfresh.isRefreshing = false;

        })
    }

    fun popUpFilter() {
        val dialog = Dialog(this)

        dialog?.apply {
            setContentView(R.layout.popup_filter)
            window?.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            btAturUlang.onClick {
                inputMin.setText("")
                inputMax.setText("")
            }
            btTerapkan.onClick {
//                detailBrandAdapter.items.sortBy { it.price }
//                detailBrandAdapter.notifyDataSetChanged()
//                setSearch(result, "", inputMin.text.toString(), inputMax.text.toString(), token)
                dismiss()
            }
            show()
        }
    }

    fun popUpUrutkan() {
        val dialog = Dialog(this)

        dialog?.apply {
            setContentView(R.layout.popup_urutkan)
            window?.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            radioGroup?.setOnCheckedChangeListener { group, checkedId ->
                if (R.id.radioBtn1 == checkedId) {
                    detailBrandAdapter.items.sortBy { it.price }
                    detailBrandAdapter.notifyDataSetChanged()
                    dismiss()
                } else if (R.id.radioBtn2 == checkedId) {
                    detailBrandAdapter.items.sortByDescending { it.price }
                    detailBrandAdapter.notifyDataSetChanged()
                    dismiss()

                }
//                else if(R.id.radioBtn3 == checkedId){
//                    searchAdapter.items.sortBy { it.price }
//                    searchAdapter.notifyDataSetChanged()
//                    dismiss()
//
//                }else if(R.id.radioBtn4 == checkedId){
//                    searchAdapter.items.sortBy { it.price }
//                    searchAdapter.notifyDataSetChanged()
//                    dismiss()
//
//                }
                else if (R.id.radioBtn5 == checkedId) {
                    detailBrandAdapter.items.sortBy { it.name }
                    detailBrandAdapter.notifyDataSetChanged()
                    dismiss()

                } else {
                    detailBrandAdapter.items.sortByDescending { it.name }
                    detailBrandAdapter.notifyDataSetChanged()
                    dismiss()

                }
            }


            show()
        }
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
            tvGambarPopUp.loadImageFromResources(this@DetailBrandActivity, gambar)
            tvKeteranganPopUp.text = keteranganN
            tvNamaPopUp.text = nama


            show()
        }
    }

    fun getDetailBrand(id: String) {
        repository!!.getDetailBrand(
            id,
            repository?.getToken()!!,
            object : DetailBrandResponse.DetailBrandResponseCallback {
                override fun onSuccess(detailBrandResponse: DetailBrandResponse) {
                    if (detailBrandResponse.status == true) {
//                        if (keranjangResponse.data?.adaartikel == "ADA") {
                        detailBrandAdapter.clearItems()
                        detailBrandResponse.data?.items?.let { detailBrandAdapter.addItems(it) }


                        if (detailBrandResponse.data?.description?.length!! >= 200) {
                            tvDeskripsiBrand.text =
                                detailBrandResponse.data?.description?.substring(0, 100) + "..."
                        }


                        keteranganN = detailBrandResponse.data?.description.toString()
                        nama = detailBrandResponse.data?.name.toString()
                        gambar = detailBrandResponse.data?.image.toString()
                        detailBrandResponse.data?.image?.let {
                            tvBackgoundBrand.loadImageFromResources(
                                this@DetailBrandActivity,
                                it
                            )
                        }
                        detailBrandResponse.data?.logo?.let {
                            tvLogoBrand.loadImageFromResources(
                                this@DetailBrandActivity,
                                it
                            )
                        }
                        tvJumlahProduk.text =
                            detailBrandResponse.data?.items?.size.toString() + " Produk"
                        tvNamaBrand.text = detailBrandResponse.data?.name
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

