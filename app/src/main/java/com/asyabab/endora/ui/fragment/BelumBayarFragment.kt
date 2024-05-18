package com.asyabab.endora.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.asyabab.endora.R
import com.asyabab.endora.base.BaseFragment
import com.asyabab.endora.data.models.payment.getpembelian.Data
import com.asyabab.endora.data.models.payment.getpembelian.GetPembelianResponse
import com.asyabab.endora.data.models.payment.getpembelian.Order
import com.asyabab.endora.data.models.payment.getpembelian.Unpaid
import com.asyabab.endora.ui.activity.FinalPembayaranActivity
import com.asyabab.endora.ui.activity.RincianBelumBayarActivity
import com.asyabab.endora.utils.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_belumbayar.*
import kotlinx.android.synthetic.main.fragment_belumbayar.view.*
import kotlinx.android.synthetic.main.rv_belumbayar.*
import kotlinx.android.synthetic.main.rv_belumbayar.view.*
import kotlinx.android.synthetic.main.rv_gambarbarang.view.*

class BelumBayarFragment : BaseFragment() {
    private val viewPool = RecycledViewPool()
    private val mKosongFragment = KosongFragment()

    private var belumBayarAdapter: RecyclerViewAdapter<Unpaid> = RecyclerViewAdapter(
        R.layout.rv_belumbayar,
        onBind = { view, data, position ->

//            if (position % 2 == 0) {
//                view.panelBackground.setBackgroundColor(resources.getColor(R.color.grayback))
//            } else {
//                view.panelBackground.setBackgroundColor(resources.getColor(R.color.white))
//
//            }
            view.btBayarSekarang.onClick {
                requireActivity().launchActivity<FinalPembayaranActivity> {
                    putExtra("data", data)
                }
            }
            view.tvNoPesanan.text = "#" + data.id
            view.tvTanggalPemesanan.text = data.createdAt
//            detailBarangAdapter.clearItems()
            val layoutManager = LinearLayoutManager(
                view.rvListBarang.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            var namapembayaran = ""
            if (data?.type.toString() == "xendit-bni") {
                namapembayaran = "BNI Virtual Account"
            } else if (data?.type.toString() == "xendit-bri") {
                namapembayaran = "BRI Virtual Account"

            } else if (data?.type.toString() == "xendit-mandiri") {
                namapembayaran = "Mandiri Virtual Account"
            } else if (data?.type.toString() == "bca-va") {
                namapembayaran = "BCA Virtual Account"
            }
            view.tvNamaPembayarana.text = namapembayaran

            view.tvKeteranganBayar.text = "Bayar sebelum " + data.expireDate
            val subItemAdapter =
                context?.let { data.order?.let { it1 -> RecyclerViewBelumBayarAdapter(it, it1) } }
            data.order?.size?.let { layoutManager.initialPrefetchItemCount = it }

//            data?.order?.let { detailBarangAdapter.addItems(it) }
            view.tvJumlahBarang.text = "Jumlah Produk " + data.order?.size.toString()
            view.rvListBarang.setHorizontalLayout(false)
            view.rvListBarang.adapter = subItemAdapter
            view.rvListBarang.setRecycledViewPool(viewPool)
//            data.id?.let { getDetailPembelian(it) }

//            view.tvKeteranganBayar.text="Bayar sebelum" +data.
//            data?.order?.get(position)?.item?.images.let { detailBarangAdapter.addItems(it) }

            view.onClick {
                requireActivity().launchActivity<RincianBelumBayarActivity> {
                    putExtra("data", data)
                }

            }
        })

    private var detailBarangAdapter: RecyclerViewAdapter<Order> = RecyclerViewAdapter(
        R.layout.rv_gambarbarang,
        onBind = { view, data, position ->

            data.item?.images?.get(0)?.name?.let {
                view.tvGambarBarang.loadImageFromResources(
                    context,
                    it
                )
            }

//            context?.let {
//                Glide.with(it)
//                    .load(data.item?.images?.get(0)?.name)
//                    .skipMemoryCache(true)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .into(view.tvGambarBarang)
//            }


            view.textgambar.text = data.item?.name

        })


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_belumbayar, container, false)
        root.rvBelumBayar.setVerticalLayout(false)
        root.rvBelumBayar.adapter = belumBayarAdapter

        getPembelian()

//        val gson = Gson()
//        val json: String = repository?.getPembelian()!!
//        val obj: Data = gson.fromJson(json, Data::class.java)
//        belumBayarAdapter.clearItems()
//        obj.unpaid?.let { belumBayarAdapter.addItems(it) }
        root.swipeResfresh.setOnRefreshListener(OnRefreshListener {
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
                        belumBayarAdapter.clearItems()
//                        Toast.makeText(context, getPembelianResponse.data?.unpaid?.size.toString(), Toast.LENGTH_LONG).show()

                        if (getPembelianResponse.data?.unpaid?.size.toString() == "0") {
                            framekosong.visibility = View.VISIBLE
                            frameshimmer.visibility = View.GONE
                            rvBelumBayar.visibility = View.GONE
                            framemain.visibility = View.VISIBLE

                        } else {
                            getPembelianResponse.data?.unpaid?.let { belumBayarAdapter.addItems(it) }
                            framemain.visibility = View.VISIBLE
                            frameshimmer.visibility = View.GONE
                            rvBelumBayar.visibility = View.VISIBLE
                            framekosong.visibility = View.GONE

                        }

                    }

                }

                override fun onFailure(message: String) {
                    Toast.makeText(context, "Server Sedang Error", Toast.LENGTH_LONG).show()
                }

            })
    }

//    fun getDetailPembelian(id:String) {
//        repository!!.getDetailPembelian(
//            id, repository?.getToken()!!,
//            object : GetDetailPembelianResponse.GetDetailPembelianResponseCallback {
//                override fun onSuccess(getDetailPembelianResponse: GetDetailPembelianResponse) {
//                    if (getDetailPembelianResponse.status == true) {
////                        if (keranjangResponse.data?.adaartikel == "ADA") {
//                        detailBarangAdapter.clearItems()
//                        getDetailPembelianResponse.data?.order?.let { detailBarangAdapter.addItems(it) }
//                    }
//                }
//
//                override fun onFailure(message: String) {
//                    Toast.makeText(context, "Server Sedang Error", Toast.LENGTH_LONG).show()
//
//                }
//
//            })
//    }

}