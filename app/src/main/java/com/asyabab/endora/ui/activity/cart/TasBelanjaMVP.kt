package com.asyabab.endora.ui.activity.cart

import com.asyabab.endora.data.models.cart.keranjang.Data
import com.asyabab.endora.data.models.cart.keranjang.KeranjangResponse
import java.util.*

class TasBelanjaMVP {
    interface View {
        fun showLoading(isLoading: Boolean)
        fun showError(message: String?)
        fun upDateList(productItems: KeranjangResponse)
        fun updateTotalItem(productItems: ArrayList<Data>)

        fun updateTotalPrice(value: Int)
    }

    interface Presenter {
        fun loadData()
        fun onDestroy()
        fun addOrder(productItem: Data)
        fun updateTotal()
        fun removeOrder(productItem: Data)
    }
}