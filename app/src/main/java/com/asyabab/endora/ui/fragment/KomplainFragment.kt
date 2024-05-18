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
import com.asyabab.endora.data.models.payment.getpembelian.GetPembelianResponse
import com.asyabab.endora.data.models.payment.getpembelian.Submission
import com.asyabab.endora.ui.activity.RincianKomplainActivity
import com.asyabab.endora.utils.*
import kotlinx.android.synthetic.main.fragment_komplain.*
import kotlinx.android.synthetic.main.fragment_komplain.view.*
import kotlinx.android.synthetic.main.rv_belumbayar.view.*
import kotlinx.android.synthetic.main.rv_komplain.view.*
import kotlinx.android.synthetic.main.rv_komplain.view.rvListBarang
import kotlinx.android.synthetic.main.rv_komplain.view.tvJumlahBarang
import kotlinx.android.synthetic.main.rv_komplain.view.tvNoPesanan
import kotlinx.android.synthetic.main.rv_komplain.view.tvTanggalPemesanan


class KomplainFragment : BaseFragment() {
    private val viewPool = RecyclerView.RecycledViewPool()

    private var komplainAdapter: RecyclerViewAdapter<Submission> = RecyclerViewAdapter(
        R.layout.rv_komplain,
        onBind = { view, data, position ->
            view.tvNoPesanan.text = " #" + data.id
            view.tvTanggalPemesanan.text = data.createdAt
            view.tvJumlahBarang.text = "Jumlah Produk " + data.order?.size.toString()
//            var jumlah = data.order?.size?.minus(1)
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
                context?.let { data.order?.let { it1 -> RecyclerViewKomplainAdapter(it, it1) } }
            data.order?.size?.let { layoutManager.initialPrefetchItemCount = it }
            view.rvListBarang.setHorizontalLayout(false)
            view.rvListBarang.adapter = subItemAdapter
            view.rvListBarang.setRecycledViewPool(viewPool)
            view.onClick {
                requireActivity().launchActivity<RincianKomplainActivity> {
                    putExtra("data", data)
                }

            }
        })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_komplain, container, false)

        root.rvKomplain.setVerticalLayout(false)
        root.rvKomplain.adapter = komplainAdapter

        getPembelian()
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
                        komplainAdapter.clearItems()
                        try {
                            if (getPembelianResponse.data?.submission?.isEmpty()!!) {
                                framekosong.visibility = View.VISIBLE
                                frameshimmer.visibility = View.GONE
                                rvKomplain.visibility = View.GONE
                                framemain.visibility = View.VISIBLE

                            } else {
                                getPembelianResponse.data?.submission?.let {
                                    komplainAdapter.addItems(
                                        it
                                    )
                                }
                                framemain.visibility = View.VISIBLE
                                frameshimmer.visibility = View.GONE
                                rvKomplain.visibility = View.VISIBLE
                                framekosong.visibility = View.GONE

                            }
                        } catch (e: KotlinNullPointerException) {
                            framekosong.visibility = View.VISIBLE
                            frameshimmer.visibility = View.GONE
                            rvKomplain.visibility = View.GONE
                            framemain.visibility = View.VISIBLE
                        }


//                        Toast.makeText(context, getPembelianResponse.data?.submission.toString(), Toast.LENGTH_LONG).show()

                    }
                }

                override fun onFailure(message: String) {
                    Toast.makeText(context, "Server Sedang Error", Toast.LENGTH_LONG).show()

                }

            })
    }
}