package com.asyabab.endora.ui.activity

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.asyabab.endora.R
import com.asyabab.endora.base.BaseActivity
import com.asyabab.endora.data.models.bank.Bank
import com.asyabab.endora.data.models.cart.keranjang.Data
import com.asyabab.endora.data.models.checkout.CheckoutResponse
import com.asyabab.endora.data.models.courier.CourierResponse
import com.asyabab.endora.data.models.courier.Datum
import com.asyabab.endora.data.models.lokasi.getmain.GetMainResponse
import com.asyabab.endora.data.models.ongkir.OngkirResponse
import com.asyabab.endora.utils.*
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.activity_rincianprofil.btBack
import kotlinx.android.synthetic.main.popup_ekspedisi.*
import kotlinx.android.synthetic.main.rv_checkout.view.*
import kotlinx.android.synthetic.main.rv_ekspedisi.view.*
import kotlinx.android.synthetic.main.rv_location.view.*
import kotlinx.android.synthetic.main.rv_payment.view.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList


class CheckoutActivity : BaseActivity() {

    private var productItems: ArrayList<Data>? = null
    private var courier = ArrayList<Datum>()
    private var varian = ""
    private var ongkir = 0
    private var price = 0
    private var weight = 0
    private var location_id = ""
    private var courier_id = "0"
    private var payment_slug = "xendit-bri"
    private lateinit var dialog: Dialog
    private lateinit var dialogMetodePembayaran: Dialog
    fun Any.convertRupiah(): String {
        val df = DecimalFormat("#,###,##0")


        val strFormat = df.format(this)
        var bilangan = "Rp " + strFormat
        return bilangan
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        loading?.setCancelable(true)
        productItems = repository?.getProduvt()

        Log.d("cook55", productItems?.size.toString())
        rvTasBelanja.setVerticalLayout(true)
        rvTasBelanja.adapter = tasBelanjaAdapter
        tasBelanjaAdapter.clearItems()
        if (productItems?.isNotEmpty() == true) {
            tasBelanjaAdapter.addItems(productItems!!)

            price = 0
            weight = 0
            for (i in productItems!!.indices) {
                price += productItems!![i].item?.price!! * productItems!![i].qty!!
                weight += productItems!![i].item?.weight!! * productItems!![i].qty!!
            }
            tv_subtotal.text = price.convertRupiah()
        }
        llMainAlamat.onClick {
            launchActivity<AturAlamatActivity> { }
        }
        tv_ekspedisi.onClick {
            dialog = Dialog(this@CheckoutActivity)

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

        rlPayment.onClick {
//            launchActivity<MetodePembayaranActivity> { }

            dialogMetodePembayaran = Dialog(this@CheckoutActivity)

            dialogMetodePembayaran.apply {
                setContentView(R.layout.popup_ekspedisi)
                window?.setLayout(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )

                rvPayment.setVerticalLayout(true)
                rvPayment.adapter = metodePembayaranAdapter
                metodePembayaranAdapter.clearItems()
                val bankBRI = Bank()
                bankBRI.name = "BRI Virtual Account"
                bankBRI.slug = "xendit-bri"
                bankBRI.image = R.drawable.icon_bank_bri
                val bankBNI = Bank()
                bankBNI.name = "BNI Virtual Account"
                bankBNI.slug = "xendit-bni"
                bankBNI.image = R.drawable.icon_bank_bni
                val bankMandiri = Bank()
                bankMandiri.name = "Mandiri Virtual Account"
                bankMandiri.slug = "xendit-mandiri"
                bankMandiri.image = R.drawable.icon_bank_mandiri
                metodePembayaranAdapter.addItem(bankBRI)
                metodePembayaranAdapter.addItem(bankBNI)
                metodePembayaranAdapter.addItem(bankMandiri)


                show()
            }
        }

        btBack.onClick {
            finish()
        }

        btBayar.onClick {
            if (tv_ekspedisi.text.toString().equals("Belum memilih", true)) {
                Toast.makeText(
                    this@CheckoutActivity,
                    "Anda  harus memilih ekspedisi",
                    Toast.LENGTH_SHORT
                ).show()

                return@onClick
            }
            if (tv_payment.text.toString().equals("Belum memilih", true)) {
                Toast.makeText(
                    this@CheckoutActivity,
                    "Anda  harus memilih metode pembayaran",
                    Toast.LENGTH_SHORT
                ).show()
                return@onClick
            } else {

                loading!!.show()
                val jsonObject = JSONObject()
                try {
                    jsonObject.put("location_id", repository!!.getLokasi())
                    jsonObject.put("shipmentable_id", courier_id)
                    jsonObject.put("shipmentable_type", "shipment")
                    jsonObject.put("description", "ditunggu kedatangan barangnya")
                    jsonObject.put("total_shipment", ongkir)
                    jsonObject.put("total", price)
                    jsonObject.put("type", payment_slug)

                    val jsonArray = JSONArray()

                    for (i in productItems!!.indices) {
                        val jsonObj = JSONObject()
                        jsonObj.put("item_id", productItems!![i].itemId)
                        jsonObj.put("name", productItems!![i].item?.name)
                        jsonObj.put("price", productItems!![i].item?.price)
                        jsonObj.put("qty", productItems!![i].qty)
                        jsonObj.put("variant", productItems!![i].item?.variant?.get(0))
                        jsonObj.put("t_weight", productItems!![i].item?.weight)
                        jsonObj.put("discount", "0")
                        jsonObj.put(
                            "total",
                            (productItems!![i].item?.price!! * productItems!![i].qty!!)
                        )
                        jsonObj.put("note", productItems!![i].item?.description.toString())

                        jsonArray.put(jsonObj)
                    }


                    jsonObject.put("item", jsonArray)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                repository?.checkout(repository?.getToken().toString(), jsonObject,
                    object : CheckoutResponse.CheckoutResponseCallback {
                        override fun onSuccess(checkoutResponse: CheckoutResponse) {
                            loading!!.dismiss()
                            if (checkoutResponse.status == true) {
                                repository?.saveCheckoutResponse(checkoutResponse)
                                launchActivity<FinalPembayaranActivity> {}
                            } else {
                                Log.e("hasil", "gagal")
                            }
                        }

                        override fun onFailure(message: String) {
                            loading!!.dismiss()
                            Log.e("hasil", message)
//                            launchActivity<MetodePembayaranActivity> {}
                        }

                    })
            }
        }

        getCourier()
        getMain()
    }

    fun getMain() {
        loading!!.show()
        repository!!.getMain(
            repository?.getToken()!!,
            object : GetMainResponse.GetMainResponseCallback {
                override fun onSuccess(getMainResponse: GetMainResponse) {
                    loading!!.dismiss()
                    if (getMainResponse.status == true) {
                        repository!!.saveLokasi(getMainResponse.data?.subdistrictId.toString())

                        tvNamaPenerima.text = getMainResponse.data?.receiverName
                        tvNomerTelepon.text = getMainResponse.data?.phone
                        tvAlamat.text = getMainResponse.data?.address
                        tvKecamatan.text = getMainResponse.data?.detail?.subdistrictName
                        tvKabupaten.text = getMainResponse.data?.detail?.city
                        tvProvinsi.text = getMainResponse.data?.detail?.province
                    } else {
//                        repository!!.saveLokasi("")

                    }
                }

                override fun onFailure(message: String) {
                    loading!!.dismiss()
                    Log.e("Hasil", "Gagal Memuat" + message)
                }

            })
    }

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


    fun cekOngkir(courier: String, service: String) {
        loading!!.show()
//        if (weight < 1000) {
//            weight = 1
//        } else {
//            weight = Math.ceil((weight / 1000).toDouble()).toInt()
//        }

        Log.e(
            "hasil-ongkir",
            "hasil parameter : ${repository!!.getLokasi()} $weight $courier $service"
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
                        tv_subtotal.text = price.convertRupiah()
                    } catch (e: Exception) {

                    }

                }

                override fun onFailure(message: String) {
                    loading!!.dismiss()
                    Log.e("hasil", message)
                }

            })
    }

    private var tasBelanjaAdapter: RecyclerViewAdapter<Data> = RecyclerViewAdapter(
        R.layout.rv_checkout
    ) { view, data, position ->

        view.tvKeranjangNama.text = data.item?.name
        view.tvQty.setText("X" + data.qty.toString())
        data.item?.images?.get(0)?.name?.let {
            view.tvKeranjangGambar.loadImageFromResources(
                this,
                it
            )
        }

        view.tvKeranjangHarga.text = data.item?.price?.convertRupiah()
        view.onClick {
            launchActivity<DetailProdukActivity> {
                putExtra("data", data.id.toString())
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
            }
        })

    private var metodePembayaranAdapter: RecyclerViewAdapter<Bank> = RecyclerViewAdapter(
        R.layout.rv_payment,
        onBind = { view, data, position ->
            view.tv_bank.text = data.name
            view.icon_bank.setImageDrawable(getResources().getDrawable(data.image!!));
            view.radio_bank.onClick {
                dialogMetodePembayaran.dismiss()
                payment_slug = data.slug.toString()
                tv_payment.text = data.name
            }
            view.rlBayar.onClick {
                dialogMetodePembayaran.dismiss()
                payment_slug = data.slug.toString()
                tv_payment.text = data.name
            }
        })
}