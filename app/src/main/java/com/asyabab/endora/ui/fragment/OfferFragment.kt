package com.asyabab.endora.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.asyabab.endora.R
import com.asyabab.endora.base.BaseFragment
import com.asyabab.endora.data.models.offer.getoffer.Data
import com.asyabab.endora.data.models.offer.getoffer.GetOfferResponse
import com.asyabab.endora.ui.activity.DetailBrandActivity
import com.asyabab.endora.ui.activity.DetailOfferActivity
import com.asyabab.endora.utils.*
import kotlinx.android.synthetic.main.activity_jelajah.*
import kotlinx.android.synthetic.main.fragment_promolain.*
import kotlinx.android.synthetic.main.fragment_promolain.view.*
import kotlinx.android.synthetic.main.rv_promolain.view.*


class OfferFragment : BaseFragment() {

    private var offerAdapter: RecyclerViewAdapter<Data> = RecyclerViewAdapter(
        R.layout.rv_promolain,
        onBind = { view, data, position ->
            view.tvNamaPromo.text = data.name
            view.tvDeskripsiPromo.text = ""
            data.cover?.let { view.tvGambar.loadImageFromResources(context, it) }

            view.onClick {
                requireActivity().launchActivity<DetailOfferActivity> {
                    putExtra("data", data.id)
                    putExtra("nama", data.name)
                }

            }
        })


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_promolain, container, false)

        root.rvPromoLain.setVerticalLayout(false)
        root.rvPromoLain.adapter = offerAdapter
        activity?.titletoolbar?.text = "Promo Lain"

        getOffer()
        root.swipeResfresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            getOffer()
            root.swipeResfresh.isRefreshing = false;

        })
        return root
    }

    fun getOffer() {
        repository!!.getOffer(
            repository?.getToken()!!,
            object : GetOfferResponse.GetOfferCallback {
                override fun onSuccess(getOfferResponse: GetOfferResponse) {
                    if (getOfferResponse.status == true) {
//                        if (keranjangResponse.data?.adaartikel == "ADA") {
                        offerAdapter.clearItems()
                        offerAdapter.addItems(getOfferResponse.data!!)

                        framemain.visibility = View.VISIBLE
                        frameshimmer.visibility = View.GONE
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