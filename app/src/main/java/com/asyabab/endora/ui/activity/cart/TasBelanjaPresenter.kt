package com.asyabab.endora.ui.activity.cart

import android.util.Log
import com.asyabab.endora.data.Repository
import com.asyabab.endora.data.models.cart.keranjang.Data
import com.asyabab.endora.ui.activity.cart.TasBelanjaMVP.Presenter
import java.util.*

class TasBelanjaPresenter(repository: Repository, view: TasBelanjaMVP.View) : Presenter {

    private var repository: Repository? = repository
    private var view: TasBelanjaMVP.View? = view
    private var productItems = ArrayList<Data>()

    override fun addOrder(productItem: Data) {
        if (productItems.size > 0) {
            var same = false
            for (i in productItems.indices) {
                if (productItems[i].id == productItem.id) {
                    productItems[i] = productItem
                    same = true
                }
            }
            if (!same) {
                productItems.add(productItem)
            }
        } else {
            productItems.add(productItem)
        }

        var price = 0

        for (i in productItems.indices) {
            price += productItems[i].item?.price!! * productItems[i].qty!!
        }
        view!!.updateTotalPrice(price)
        view!!.updateTotalItem(productItems)
//        repository.setProductItems(productItems)
    }

    override fun removeOrder(productItem: Data) {

        productItems.remove(productItem)

        var price = 0

        for (i in productItems.indices) {
            price += productItems[i].item?.price!! * productItems[i].qty!!
        }
        view!!.updateTotalPrice(price)
        view!!.updateTotalItem(productItems)
    }

    override fun loadData() {
        view!!.showLoading(true)
        repository?.getKeranjang(
            repository?.getToken().toString(),
            object :
                com.asyabab.endora.data.models.cart.keranjang.KeranjangResponse.KeranjangResponseCallback {
                override fun onSuccess(keranjangResponse: com.asyabab.endora.data.models.cart.keranjang.KeranjangResponse) {
                    Log.e("orderr", "onSuccess: ")
                    if (view != null) {
                        view!!.showLoading(false)
                        view!!.upDateList(keranjangResponse)
//                        productItems= ArrayList()
//                        view!!.updateTotalItem(keranjangResponse.data as ArrayList<Data>)
                    }

                }

                override fun onFailure(message: String) {
                    Log.e("orderr", "onFailure: ")

                    if (view != null) {
                        view!!.showLoading(false)
                        view!!.showError("Gagal memuat")
                    }


                }

            })
    }


    override fun updateTotal() {}
    override fun onDestroy() {}
}