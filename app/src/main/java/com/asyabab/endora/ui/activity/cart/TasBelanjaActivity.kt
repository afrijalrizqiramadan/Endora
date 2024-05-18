package com.asyabab.endora.ui.activity.cart

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.asyabab.endora.R
import com.asyabab.endora.base.BaseActivity
import com.asyabab.endora.data.models.cart.keranjang.Data
import com.asyabab.endora.data.models.cart.keranjang.KeranjangResponse
import com.asyabab.endora.data.models.general.GeneralResponse
import com.asyabab.endora.ui.activity.CheckoutActivity
import com.asyabab.endora.ui.activity.DetailProdukActivity
import com.asyabab.endora.utils.*
import kotlinx.android.synthetic.main.activity_pencarian.*
import kotlinx.android.synthetic.main.activity_tasbelanja.*
import kotlinx.android.synthetic.main.activity_tasbelanja.btBack
import kotlinx.android.synthetic.main.rv_tasbelanja.*
import kotlinx.android.synthetic.main.rv_tasbelanja.view.*
import java.text.DecimalFormat
import java.util.*


class TasBelanjaActivity : BaseActivity(), TasBelanjaMVP.View {

    private var presenter: TasBelanjaPresenter? = null
    private var productItem = ArrayList<Data>()
    private var tasBelanjaAdapter: RecyclerViewAdapter<Data> = RecyclerViewAdapter(
        R.layout.rv_tasbelanja
    ) { view, data, position ->

        view.tvKeranjangNama.text = data.item?.name
        view.tvQty.setText(data.qty.toString())
        data.item?.images?.get(0)?.name?.let {
            view.tvKeranjangGambar.loadImageFromResources(
                this,
                it
            )
        }
        view.tvQty.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                setUpdateJumlah(
                    data.itemId.toString(),
                    data.id.toString(),
                    tvQty.text.toString()
                )
            }
        }

        view.btPlus.onClick {
            var icounter = data.qty
            Log.d("posisi", icounter.toString())
            view.btMin.isClickable = true

            if (icounter != null) {
                if (icounter < data.item?.inventory!!) {
                    icounter += 1
                    Log.d("posisi+", icounter.toString())
                    setUpdateJumlah(data.itemId.toString(), data.id.toString(), icounter.toString())
                    view.tvQty.setText(icounter.toString())
                } else {
                    view.btPlus.isClickable = false
                }
            }
        }
        view.btMin.onClick {
            var icounter = data.qty
            var temp = true
            Log.d("posisi", icounter.toString())
            view.btPlus.isClickable = true

            if (icounter != null) {
                if (icounter != 0) {
                    icounter -= 1
                    Log.d("posisi-", icounter.toString())
                    setUpdateJumlah(data.itemId.toString(), data.id.toString(), icounter.toString())
                    view.tvQty.setText(icounter.toString())
                } else {
                    view.btMin.isClickable = false

                }
            }
        }
        view.cbItem.isChecked = RecyclerViewAdapter.isSelectedAll == true
        view.cbItem.setOnCheckedChangeListener { buttonView, p1 ->
            if (p1) {
                presenter?.addOrder(data)
            } else {
                presenter?.removeOrder(data)
            }
        }
        view.btHapusKeranjang.onClick {
            deleteKeranjang(data.id.toString())
        }
        view.tvKeranjangHarga.text = data.item?.price?.convertRupiah()
        view.onClick {
            launchActivity<DetailProdukActivity> {
                putExtra("data", data.itemId.toString())
            }
        }

        view.tvQty.setOnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                inputSearch.clearFocus()
                val imm =
                    getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(
                    InputMethodManager.HIDE_IMPLICIT_ONLY,
                    0
                )
                true
            } else false
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasbelanja)

        btBack.onClick {
            finish()
        }

        rvTasBelanja.setVerticalLayout(true)
        rvTasBelanja.adapter = tasBelanjaAdapter

        presenter = TasBelanjaPresenter(repository!!, this)
        presenter?.loadData()

        cbAll.setOnClickListener {
            if (cbAll.isChecked) {
                tasBelanjaAdapter.selectAll()
            } else {
                tasBelanjaAdapter.unselectall()

            }
        }

        swipeResfresh.setOnRefreshListener {
            presenter?.loadData()
            swipeResfresh.isRefreshing = false

        }

        Log.d("cook1", productItem.size.toString())
        btCheckout.onClick {
            if (productItem.size > 0) {
                repository?.saveProduct(productItem)
                launchActivity<CheckoutActivity> {}
            } else {
                Toast.makeText(
                    this@TasBelanjaActivity,
                    "Anda setidaknya harus memilih 1 produk",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    fun deleteKeranjang(itemId: String) {
        repository!!.deleteKeranjang(
            itemId,
            repository?.getToken()!!,
            object : GeneralResponse.GeneralResponseCallback {
                override fun onSuccess(generalResponse: GeneralResponse) {
                    if (generalResponse.status == true) {
                        presenter?.loadData()
                    }
                }

                override fun onFailure(message: String) {
                    Toast.makeText(applicationContext, "Server Sedang Error", Toast.LENGTH_LONG)
                        .show()
                }

            })
    }

    fun setUpdateJumlah(itemId: String, id: String, qty: String): Boolean {
        var temp = false
        repository!!.setUpdateJumlah(
            id,
            itemId,
            qty,
            repository?.getToken()!!,
            object : GeneralResponse.GeneralResponseCallback {
                override fun onSuccess(generalResponse: GeneralResponse) {
                    if (generalResponse.status == true) {
                        presenter?.loadData()
                        temp = true
                        if (generalResponse.data == "400") {
                            Toast.makeText(
                                applicationContext,
                                "Jumlah Melebihi Stok",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                    }
                }

                override fun onFailure(message: String) {
                    Toast.makeText(applicationContext, "Server Sedang Error", Toast.LENGTH_LONG)
                        .show()
                    temp = false

                }

            })
        return temp
    }

    override fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            loading!!.show()
        } else {
            loading!!.dismiss()
        }
    }

    override fun showError(message: String?) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG)
            .show()
    }

    override fun upDateList(
        productItems: KeranjangResponse
    ) {
        if (productItems.status == true) {
            if (productItems.data!!.size.toString() == "0") {
                panelKosong.visibility = View.VISIBLE
                rvTasBelanja.visibility = View.GONE
                panelBottom.visibility = View.INVISIBLE
            } else {
                panelKosong.visibility = View.GONE
                rvTasBelanja.visibility = View.VISIBLE
                panelBottom.visibility = View.VISIBLE
                tasBelanjaAdapter.clearItems()
                tasBelanjaAdapter.addItems(productItems.data!!)
                tvJumlahItem.text =
                    "Item Anda (" + productItems.data!!.size.toString() + ")"
            }
            framemain.visibility = View.VISIBLE
            frameshimmer.visibility = View.GONE
        }
    }

    override fun updateTotalItem(productItems: ArrayList<Data>) {
        productItem = productItems
        Log.d("cook2", productItem.size.toString())
    }

    override fun updateTotalPrice(value: Int) {
        tv_SubTotal.text = value.convertRupiah()
    }

    fun Any.convertRupiah(): String {
        val df = DecimalFormat("#,###,###")


        val strFormat = df.format(this)
        var bilangan = "Rp " + strFormat
        return bilangan
    }
}
