package com.asyabab.endora.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.asyabab.endora.R
import com.asyabab.endora.base.BaseFragment
import com.asyabab.endora.data.models.brand.getbrand.GetBrandResponse
import com.asyabab.endora.data.models.brand.getbrand.Data
import com.asyabab.endora.ui.activity.DetailBrandActivity
import com.asyabab.endora.utils.*
import kotlinx.android.synthetic.main.fragment_brand.view.*
import kotlinx.android.synthetic.main.rv_brand.view.*


class BrandFragment : BaseFragment() {

    private var brandAdapter: RecyclerViewAdapter<Data> = RecyclerViewAdapter(
        R.layout.rv_brand,
        onBind = { view, data, position ->
            view.tvBrandNama.text = data.name
            data.logo?.let { view.tvBrandGambar.loadImageFromResources(context, it) }
            view.onClick {
                requireActivity().launchActivity<DetailBrandActivity> {
                    putExtra("data", data.id)
                }

            }
        })


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_brand, container, false)

        val layoutManager = GridLayoutManager(context, 3)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        root.rvBrand.layoutManager = layoutManager
        root.rvBrand.adapter = brandAdapter

        getBrand()

        return root
    }

    fun getBrand() {
        repository!!.getBrand(
            repository?.getToken()!!,
            object : GetBrandResponse.GetBrandResponseCallback {
                override fun onSuccess(brandtresponse: GetBrandResponse) {
                    if (brandtresponse.status == true) {
//                        if (keranjangResponse.data?.adaartikel == "ADA") {
                        brandAdapter.clearItems()
                        brandAdapter.addItems(brandtresponse.data!!)
//                        } else {
//
//                        }
                    }
                }

                override fun onFailure(message: String) {
                }

            })
    }
}