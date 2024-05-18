package com.asyabab.endora.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.asyabab.endora.R
import com.asyabab.endora.base.BaseFragment
import com.asyabab.endora.data.models.payment.getpembelian.Canceled
import com.asyabab.endora.data.models.payment.getpembelian.Data
import com.asyabab.endora.data.models.payment.getpembelian.GetPembelianResponse
import com.asyabab.endora.ui.activity.RincianDibatalkanActivity
import com.asyabab.endora.utils.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_dibatalkan.*
import kotlinx.android.synthetic.main.fragment_dibatalkan.view.*
import kotlinx.android.synthetic.main.rv_dibatalkan.view.*


class DibatalkanFragment : BaseFragment() {
    private val viewPool = RecyclerView.RecycledViewPool()

    private var dibatalkanAdapter: RecyclerViewAdapter<Canceled> = RecyclerViewAdapter(
        R.layout.rv_dibatalkan,
        onBind = { view, data, position ->
            view.tvNoPesanan.text = " #" + data.id
            view.tvTanggalPemesanan.text = data.createdAt
            view.tvJumlahBarang.text = "Jumlah Produk " + data.order?.size.toString()
            var jumlah = data.order?.size?.minus(1)
            view.tvTanggalDibatalkan.text = data.cancel?.createdAt.toString()
            view.tvReason.text = data.cancel?.reason
//            if(jumlah==0){
//                view.tvJumlahBarang2.text = ""
//            }else{
//                view.tvJumlahBarang2.text = "+" + jumlah.toString()
//
//            }
//            data.order?.get(0)?.item?.images?.get(0)?.name?.let {
//                view.tvThumbnail.loadImageFromResources(
//                    context,
//                    it
//                )
//            }
            val layoutManager = LinearLayoutManager(
                view.rvListBarang.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            val subItemAdapter =
                context?.let { data.order?.let { it1 -> RecyclerViewDibatalkanAdapter(it, it1) } }
            data.order?.size?.let { layoutManager.initialPrefetchItemCount = it }
//            data.id?.let { getDetailPembelian(it) }
            view.rvListBarang.setHorizontalLayout(false)
            view.rvListBarang.adapter = subItemAdapter
            view.rvListBarang.setRecycledViewPool(viewPool)
            view.tvReason.text = data.cancel?.reason
//            data?.order?.get(position)?.item?.images.let { detailBarangAdapter.addItems(it) }

            view.onClick {
                requireActivity().launchActivity<RincianDibatalkanActivity> {
                    putExtra("data", data)
                }

            }
        })
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dibatalkan, container, false)
        root.rvDibatalkan.setVerticalLayout(false)
        root.rvDibatalkan.adapter = dibatalkanAdapter

        getPembelian()//        val gson = Gson()
//        val json: String = repository?.getPembelian()!!
//        val obj: Data = gson.fromJson(json, Data::class.java)
//        dibatalkanAdapter.clearItems()
//        obj.canceled?.let { dibatalkanAdapter.addItems(it) }

//        getPembelian()
        root.swipeResfresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            getPembelian()
            swipeResfresh.isRefreshing = false;

        })
        return root
    }

    fun getPembelian() {
        repository!!.getPembelian(
            repository?.getToken()!!,
            object : GetPembelianResponse.GetPembelianResponseCallback {
                override fun onSuccess(getPembelianResponse: GetPembelianResponse) {
                    if (getPembelianResponse.status == true) {
//                        if (keranjangResponse.data?.adaartikel == "ADA") {
                        dibatalkanAdapter.clearItems()
                        if (getPembelianResponse.data?.canceled?.size.toString() == "0") {
                            framekosong.visibility = View.VISIBLE
                            frameshimmer.visibility = View.GONE
                            rvDibatalkan.visibility = View.GONE
                            framemain.visibility = View.VISIBLE

                        } else {
                            getPembelianResponse.data?.canceled?.let { dibatalkanAdapter.addItems(it) }
                            framemain.visibility = View.VISIBLE
                            frameshimmer.visibility = View.GONE
                            rvDibatalkan.visibility = View.VISIBLE
                            framekosong.visibility = View.GONE

                        }
                    }
                }

                override fun onFailure(message: String) {
                    Toast.makeText(context, "Server Sedang Error", Toast.LENGTH_LONG).show()

                }

            })
    }
}