package com.asyabab.endora.ui.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.asyabab.endora.R
import com.asyabab.endora.base.BaseActivity
import com.asyabab.endora.data.adapter.SliderItemAdapterExample
import com.asyabab.endora.data.models.Varian
import com.asyabab.endora.data.models.cart.addkeranjang.SetKeranjangResponse
import com.asyabab.endora.data.models.cart.keranjang.Data
import com.asyabab.endora.data.models.courier.CourierResponse
import com.asyabab.endora.data.models.courier.Datum
import com.asyabab.endora.data.models.general.GeneralResponse
import com.asyabab.endora.data.models.item.detailitem.DetailItemResponse
import com.asyabab.endora.data.models.item.detailitem.Image
import com.asyabab.endora.data.models.item.detailitem.SItem
import com.asyabab.endora.data.models.ongkir.OngkirResponse
import com.asyabab.endora.ui.activity.cart.TasBelanjaActivity
import com.asyabab.endora.utils.*
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.activity_detailproduk.*
import kotlinx.android.synthetic.main.popup_ekspedisi.*
import kotlinx.android.synthetic.main.popup_quantity.*
import kotlinx.android.synthetic.main.popup_varian.*
import kotlinx.android.synthetic.main.rv_produk.view.*
import kotlinx.android.synthetic.main.rv_varian.view.*
import java.text.DecimalFormat
import java.util.*


class DetailProdukActivity : BaseActivity() {
    var item = ""
    var title = ""
    var varian = ""
    var berat = ""
    private var ongkir = 0
    private var price = 0
    private var weight = 0
    private var lokasia = ""
    private var service = ""
    private var mRequestPermissionHandler: RequestPermissionHandler? = null
    private val varianlist = ArrayList<Varian>()
    var detailItem = DetailItemResponse()
    private var courier = ArrayList<Datum>()
    private var courier_id = "0"

    var sliderView: SliderView? = null
    var adapter: SliderItemAdapterExample? = null
    private var state: CollapsingToolbarLayoutState? = null
    lateinit var dialog: Dialog
    private enum class CollapsingToolbarLayoutState {
        EXPANDED, COLLAPSED, INTERNEDIATE
    }

    private var mCheckedPostion = 0

    private var varianAdapter: RecyclerViewAdapter<String> = RecyclerViewAdapter(
        R.layout.rv_varian,
        onBind = { view, data, position ->
            view.radioBtn.text = data


            view.radioBtn.onClick {
                varian = view.radioBtn.text.toString()

                dialog.dismiss()
                tvVarian.text = varian
            }

        })

    private var itemAdapter: RecyclerViewAdapter<SItem> = RecyclerViewAdapter(
        R.layout.rv_produk,
        onBind = { view, data, position ->
            view.tvNama.text = data.name
            view.tvHarga.text = data.price?.convertRupiah()
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
            data.images?.get(0)?.name?.let {
                view.tvGambar.loadImageFromResources(
                    this@DetailProdukActivity,
                    it
                )
            }
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
        val w: Window = window
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        handleIntent(this.intent)

        setContentView(R.layout.activity_detailproduk)
        try {
            if (item.isEmpty()){
                item = intent.getStringExtra("data")
                getItemDetail(item)
                Log.d("Berhasil", item)

            }
        } catch (e: Exception) {
            Log.d("Gagal", e.toString())

        }


        mRequestPermissionHandler = RequestPermissionHandler()
        Log.d("Percoba", item)

        tvIconNumber?.text = repository!!.getData("jumlahkeranjang")

        viewFavorit.onClick {
            setFavorit(item)
        }
        btTasBelanja.onClick {
            launchActivity<TasBelanjaActivity>()
        }


        btTambahTasBelanja.onClick {
            if (varian == "") {
                Toast.makeText(this@DetailProdukActivity, "Pilih Varian Dahulu", Toast.LENGTH_SHORT)
                    .show()

            } else {
                loading?.show()

                setTambahKeranjang(item, "1", varian)
            }
        }
        tv_ekspedisi.onClick {
            dialog = Dialog(this@DetailProdukActivity)

            dialog.apply {
                setContentView(R.layout.popup_ekspedisi)
                window?.setLayout(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )

                rvPayment.setVerticalLayout(true)
                rvPayment.adapter = courierAdapter
                courierAdapter.clearItems()
                courierAdapter.addItems(courier)


                show()
            }
        }

        btCheckout.onClick {
            var qty = 1
            val btnsheet = layoutInflater.inflate(R.layout.popup_quantity, null)
            val dialog = BottomSheetDialog(this@DetailProdukActivity)
            dialog.setContentView(btnsheet)

            dialog.btPlus.onClick {
                var quantity = dialog.tvQty.text.toString().toInt()
                quantity++
                dialog.tvQty.setText(quantity.toString())
                qty = quantity
            }
            dialog.btMin.onClick {
                var quantity = dialog.tvQty.text.toString().toInt()
                if (quantity > 1) {
                    quantity--
                    dialog.tvQty.setText(quantity.toString())
                    qty = quantity
                } else {

                }
            }
            dialog.btApply.onClick {
                val productItems = ArrayList<Data>()
                val productItem = Data()
                productItem.id = detailItem.data?.item?.id
                productItem.item = detailItem.data?.item
                productItem.itemId = detailItem.data?.item?.id
                productItem.qty = qty
                productItems.add(productItem)
                repository?.saveProduct(productItems)
                launchActivity<CheckoutActivity>()
            }
            dialog.btCancel.onClick {
                dialog.dismiss()
            }
            dialog.show()
        }

        btShare.onClick {
            var url = "www.endoracare.com/product/$item"
//            var url="com.asyabab.endora/product?$item"

            share(title, url)
        }
        btSearch.onClick {
            launchActivity<PencarianActivity>()
        }
        btAlamatPengiriman.onClick {
            launchActivity<AturAlamatActivity> { }
        }
        btBack.onClick {
            finish()
        }

        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val collapsingToolbarLayout =
            findViewById<View>(R.id.title) as TextView

        val appbar = findViewById<View>(R.id.appbar) as AppBarLayout
        appbar.addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (verticalOffset == 0) {
                if (state !== CollapsingToolbarLayoutState.EXPANDED) {
                    collapsingToolbarLayout.text = "" //Set title to EXPANDED
                }
            } else if (Math.abs(verticalOffset) >= appBarLayout.totalScrollRange) {
                if (state !== CollapsingToolbarLayoutState.COLLAPSED) {
                    collapsingToolbarLayout.text = title
                    state =
                        CollapsingToolbarLayoutState.COLLAPSED //Modified status marked as folded
                }
            } else {
                if (state !== CollapsingToolbarLayoutState.INTERNEDIATE) {
                    if (state === CollapsingToolbarLayoutState.COLLAPSED) {
//                        playButton.setVisibility(View.GONE) //Hide Play Button When Changed from Folding to Intermediate State
                    }
                    state =
                        CollapsingToolbarLayoutState.INTERNEDIATE //Modify the status tag to the middle
                }
            }
        })
        sliderView = findViewById(R.id.imageSlider)

        swipeResfresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            getItemDetail(item)
            swipeResfresh.isRefreshing = false;

        })

        adapter = SliderItemAdapterExample(this)
        sliderView!!.setSliderAdapter(adapter!!)
        sliderView!!.setIndicatorAnimation(IndicatorAnimationType.WORM) //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!

        sliderView!!.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        sliderView!!.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_RIGHT
        sliderView!!.indicatorSelectedColor = Color.WHITE
        sliderView!!.indicatorUnselectedColor = Color.GRAY

        sliderView!!.setOnIndicatorClickListener {
            Log.i(
                "GGG",
                "onIndicatorClicked: " + sliderView!!.currentPagePosition
            )
        }

        getCourier()
        rvSItem.setHorizontalLayout(false)
        rvSItem.adapter = itemAdapter
//        renewItems()
    }
    private fun handleIntent(intent: Intent?) {
        val appLinkAction: String? = intent?.action
        val appLinkData: Uri? = intent?.data
        showDeepLinkOffer(appLinkAction, appLinkData)
    }

    private fun showDeepLinkOffer(appLinkAction: String?, appLinkData: Uri?) {
        // 1
        if (Intent.ACTION_VIEW == appLinkAction && appLinkData != null) {
            // 2
            item = appLinkData.getQueryParameter("i").toString()
            if (item.isNullOrBlank().not()) {
                getItemDetail(item)
            } else {
                Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show()

            }
        }
    }
    private var courierAdapter: RecyclerViewAdapter<Datum> = RecyclerViewAdapter(
        R.layout.rv_ekspedisi,
        onBind = { view, data, position ->
            view.radioBtn.text = data.name + " " + data.service
            view.radioBtn.onClick {
                varian = view.radioBtn.text.toString()
                dialog.dismiss()
                tv_ekspedisi.text = data.name + " " + data.service
                courier_id = data.id.toString()
                cekOngkir(data.code.toString(), data.service.toString())
                lokasia = data.code.toString()
                service = data.service.toString()
            }
        })

    private fun getCourier() {
        loading!!.show()
        repository?.getCourier(
            repository?.getToken().toString(),
            object : CourierResponse.CourierResponseCallback {
                override fun onSuccess(courierResponse: CourierResponse) {
                    loading!!.dismiss()
                    if (courierResponse.status == true) {
                        courier.clear()
                        courier.addAll(courierResponse.data!!)
                    } else {
                        Log.e("hasil", "gagal")
                    }
                }

                override fun onFailure(message: String) {
                    loading!!.dismiss()
                    Log.e("hasil", message)
                }

            })
    }
    fun popUpWindow() {
        dialog = Dialog(this)

        dialog.apply {
            setContentView(R.layout.popup_varian)
            window?.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            rvVarian.setVerticalLayout(true)
            rvVarian.adapter = varianAdapter

            show()
        }
    }

    override fun onRestart() {
        super.onRestart()
        tvKeteranganAlamat.text = "Kirim ke " + repository!!.getLokasiKeterangan()
        cekOngkir(lokasia, service)

//        cekOngkir("151", repository!!.getLokasi().toString(), berat, "jne", "REG")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mRequestPermissionHandler!!.onRequestPermissionsResult(
            requestCode, permissions,
            grantResults
        )
    }

    fun share(judul: String, text: String) {
        val intent= Intent()
        intent.action=Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_SUBJECT, judul);
        intent.putExtra(Intent.EXTRA_TEXT, text)
        intent.type="text/plain"
        startActivity(Intent.createChooser(intent, "Share To:"))
    }

    fun getItemDetail(id: String) {
        repository!!.getDetailItem(
            id,
            repository?.getToken()!!,
            object : DetailItemResponse.DetailItemResponseCallback {
                override fun onSuccess(detailItemResponse: DetailItemResponse) {
                    if (detailItemResponse.status == true) {
                        detailItem = detailItemResponse
                        adapter!!.renewItems((detailItemResponse.data?.item?.images as MutableList<Image>?)!!)

                        itemAdapter.clearItems()
                        detailItemResponse.data?.sItem?.let { itemAdapter.addItems(it) }
                        varianAdapter.clearItems()
                        detailItemResponse.data?.item?.variant?.let { varianAdapter.addItems(it) }
                        berat = detailItemResponse.data?.item?.weight.toString()
                        if (detailItemResponse.data?.item?.promo?.isEmpty()!!) {
                            tvDetailHargaDiskon.visibility = View.INVISIBLE
                            tvDetailNamaDiskon.visibility = View.INVISIBLE
                            tvDetailHarga.text =
                                detailItemResponse.data?.item?.price?.convertRupiah()

                        } else {
                            var hasil =
                                detailItemResponse.data?.item?.price!! * detailItemResponse.data?.item?.promo!![0].discount!! / 100
                            tvDetailHargaDiskon.visibility = View.VISIBLE
                            tvDetailNamaDiskon.visibility = View.VISIBLE
                            if (hasil < detailItemResponse.data?.item?.promo!![0].maximum!!) {
                                tvDetailHarga.text =
                                    (detailItemResponse.data?.item?.price!! - hasil).convertRupiah()
                            } else {
                                tvDetailHarga.text =
                                    (detailItemResponse.data?.item?.price!! - detailItemResponse.data?.item?.promo!![0].maximum!!).convertRupiah()

                            }
                            tvDetailHargaDiskon.text =
                                detailItemResponse.data?.item?.price?.convertRupiah()
                            tvDetailNamaDiskon.text =
                                " Diskon " + detailItemResponse.data?.item?.promo!![0].discount!! + "% "
                        }
//                        tvDetailHarga.text = detailItemResponse.data?.item?.price?.convertRupiah()
                        tvDetailDeskripsi.text =
                            detailItemResponse.data?.item?.description.toString()
//                        tvKategori.text = detailItemResponse.data?.item?.categories?.get(0)?.name.toString()
                        tvBerat.text = detailItemResponse.data?.item?.weight.toString() + " gr"
                        title = detailItemResponse.data?.item?.name.toString()
                        tvDetailNama.text = title
                        tvVarian.text = detailItemResponse.data?.item?.variant?.get(0)
                        btVarian.onClick { popUpWindow() }
                        if (detailItemResponse.data?.item?.categories?.get(0)?.name == null) {
                            tvKategori.text = "-"
                        } else {
                            tvKategori.text =
                                detailItemResponse.data?.item?.categories?.get(0)?.name
                        }
                        if (detailItemResponse.data?.item?.favorite == false) {
                            btFavorit.loadImageFromResources(
                                this@DetailProdukActivity,
                                R.drawable.icon_lovewhite
                            )
                        } else {
                            btFavorit.loadImageFromResources(
                                this@DetailProdukActivity,
                                R.drawable.icon_lovered
                            )
                        }
                        framemain.visibility = View.VISIBLE
                        frameshimmer.visibility = View.GONE
                    }
                }

                override fun onFailure(message: String) {
                    Toast.makeText(
                        this@DetailProdukActivity,
                        "Server Sedang Error$message",
                        Toast.LENGTH_LONG
                    ).show()
                }

            })
    }

    fun setTambahKeranjang(id: String, qty: String, varian: String) {
        repository!!.setTambahKeranjang(
            id,
            qty,
            varian,
            repository?.getToken()!!,
            object : SetKeranjangResponse.SetKeranjangResponseCallback {
                override fun onSuccess(setKeranjangResponse: SetKeranjangResponse) {
                    if (setKeranjangResponse.status == true) {
                        loading?.hide()
                        Toast.makeText(
                            this@DetailProdukActivity,
                            "Berhasil Menambahkan",
                            Toast.LENGTH_SHORT
                        )
                            .show()

                    } else {
                        loading?.hide()
                        Toast.makeText(
                            this@DetailProdukActivity,
                            "Gagal Menambahkan",
                            Toast.LENGTH_SHORT
                        )
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

    fun setFavorit(
        id: String
    ) {
        repository!!.setFavorit(
            id,
            repository?.getToken()!!,
            object : GeneralResponse.GeneralResponseCallback {
                override fun onSuccess(generalResponse: GeneralResponse) {
                    if (generalResponse.status == true) {
                        if (generalResponse.message == "Item Favorit Berhasil Dihapus!!") {
                            Toast.makeText(
                                this@DetailProdukActivity,
                                generalResponse.message,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            btFavorit.setImageResource(R.drawable.icon_lovewhite)
                        } else {
                            Toast.makeText(
                                this@DetailProdukActivity,
                                generalResponse.message,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            btFavorit.setImageResource(R.drawable.icon_lovered)
                        }
                    } else {
                        Toast.makeText(
                            this@DetailProdukActivity,
                            "Gagal Menambahkan",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }

                override fun onFailure(message: String) {
                    Log.e("Hasil", "Gagal Memuat" + message)

                    Toast.makeText(
                        this@DetailProdukActivity,
                        "Gagal Memuat" + message,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

            })
    }

//    fun getMain() {
//        repository!!.getMain(
//            repository?.getToken()!!,
//            object : GetMainResponse.GetMainResponseCallback {
//                override fun onSuccess(getMainResponse: GetMainResponse) {
//                    if (getMainResponse.status == true) {
//                        tvKeteranganAlamat.text = "Kirim ke "+getMainResponse.data?.category
//                        tvEkpedisi.text = "JNE Reguler"
//                        cekOngkir("2091",getMainResponse.data?.subdistrictId.toString(),berat,"jne","REG")
//                        btPengiriman.onClick {
//
//                        }
//                    } else {
//                        tvKeteranganAlamat.text="Pilih Alamat Terlebih Dahulu"
//                        tvBiayaPengiriman.text="Rp 0"
//                        btPengiriman.onClick {
//                            launchActivity<AturAlamatActivity> {  }
//                        }
//                    }
//                }
//                override fun onFailure(message: String) {
//                    Log.e("Hasil", "Gagal Memuat" + message)
//
//                    Toast.makeText(
//                        this@DetailProdukActivity,
//                        "Gagal Memuat",
//                        Toast.LENGTH_SHORT
//                    )
//                        .show()
//                }
//
//            })
//    }

    fun cekOngkir(courier: String, service: String) {
        loading!!.show()
        if (weight < 1000) {
            weight = 1
        } else {
            weight = Math.ceil((weight / 1000).toDouble()).toInt()
        }

        Log.e(
            "hasil-ongkir",
            "hasil parameter : ${repository!!.getLokasi()} $berat $courier $service"
        )

//        var weightToKilo
        repository?.cekOngkir(
            repository?.getToken().toString(),
            "151",
            repository!!.getLokasi().toString(),
            weight.toString(),
            courier,
            service,
            object : OngkirResponse.OngkirResponseCallback {
                override fun onSuccess(ongkirResponse: OngkirResponse) {
                    loading!!.dismiss()
                    try {
                        tv_ongkir.text =
                            ongkirResponse.data?.costs?.cost?.get(0)?.value!!.convertRupiah()
                        tvEstimasi.text =
                            "Estimasi " + ongkirResponse.data?.costs?.cost?.get(0)?.etd!!.toString() + " hari"
                        tvEstimasi.visibility = View.VISIBLE
                        ongkir = ongkirResponse.data?.costs?.cost?.get(0)?.value!!
                        price += ongkir
                    } catch (e: Exception) {

                    }

                }

                override fun onFailure(message: String) {
                    loading!!.dismiss()
                    Log.e("hasil", message)
                }

            })
    }

//    fun cekOngkir(
//        origin: String,
//        destination: String,
//        weight: String,
//        courier: String,
//        service: String
//    ) {
//        repository!!.cekOngkir(
//            repository?.getToken()!!,
//            origin,
//            destination,
//            weight,
//            courier,
//            service,
//            object : OngkirResponse.OngkirResponseCallback {
//                override fun onSuccess(ongkirResponse: OngkirResponse) {
//                    if (ongkirResponse.status == true) {
//                        tvBiayaPengiriman.text =
//                            ongkirResponse.data?.costs?.cost?.get(0)?.value?.convertRupiah()
//
//                    } else {
//
//                    }
//                }
//
//                override fun onFailure(message: String) {
//                    Log.e("Hasil", "Gagal Memuat" + message)
//
//                    Toast.makeText(
//                        this@DetailProdukActivity,
//                        "Gagal Memuat",
//                        Toast.LENGTH_SHORT
//                    )
//                        .show()
//                }
//
//            })
//    }
}
