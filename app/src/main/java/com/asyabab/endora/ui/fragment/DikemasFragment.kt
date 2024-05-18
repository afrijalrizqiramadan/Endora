package com.asyabab.endora.ui.fragment

import android.content.Intent
import android.net.Uri
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
import com.asyabab.endora.data.models.payment.getpembelian.*
import com.asyabab.endora.ui.activity.RincianDikemasActivity
import com.asyabab.endora.utils.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.fragment_dikemas.*
import kotlinx.android.synthetic.main.fragment_dikemas.framekosong
import kotlinx.android.synthetic.main.fragment_dikemas.framemain
import kotlinx.android.synthetic.main.fragment_dikemas.frameshimmer
import kotlinx.android.synthetic.main.fragment_dikemas.swipeResfresh
import kotlinx.android.synthetic.main.fragment_dikemas.view.*
import kotlinx.android.synthetic.main.rv_dikemas.view.*
import kotlinx.android.synthetic.main.rv_dikemas.view.rvListBarang
import kotlinx.android.synthetic.main.rv_dikemas.view.tvJumlahBarang
import kotlinx.android.synthetic.main.rv_dikemas.view.tvKeteranganBayar
import kotlinx.android.synthetic.main.rv_dikemas.view.tvNoPesanan
import kotlinx.android.synthetic.main.rv_dikemas.view.tvTanggalPemesanan
import kotlinx.android.synthetic.main.rv_gambarbarang.view.*

class DikemasFragment : BaseFragment() {
    private val viewPool = RecyclerView.RecycledViewPool()

    private var dikemasAdapter: RecyclerViewAdapter<Packed> = RecyclerViewAdapter(
        R.layout.rv_dikemas,
        onBind = { view, data, position ->
            view.tvNoPesanan.text = " #" + data.id
            view.tvTanggalPemesanan.text = data.createdAt
            view.tvJumlahBarang.text = "Jumlah Produk " + data.order?.size.toString()
            view.tvKeteranganBayar.text = "Pesanan Anda akan dikirimkan sebelum " + data.expireDate

            val layoutManager = LinearLayoutManager(
                view.rvListBarang.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            val subItemAdapter =
                context?.let { data.order?.let { it1 -> RecyclerViewDikemasAdapter(it, it1) } }
            data.order?.size?.let { layoutManager.initialPrefetchItemCount = it }

//            data?.order?.let { detailBarangAdapter.addItems(it) }
            view.rvListBarang.setHorizontalLayout(false)
            view.rvListBarang.adapter = subItemAdapter
            view.rvListBarang.setRecycledViewPool(viewPool)

            view.btHubungiKami.onClick {
                btHubungiKami.onClick {
                    try {
                        val mobile = "+6283122122422"
                        val msg =
                            "Hai%20Admin%20saya%20ingin%20menanyakan%20pesanan%20dengan%20kode%20" + data.id
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://api.whatsapp.com/send?phone=$mobile&text=$msg")
                            )
                        )
                    } catch (e: Exception) {
                        Toast.makeText(context, "Whatsapp Belum Terinstall", Toast.LENGTH_LONG)
                            .show()

                    }
                }
            }
//            data.id?.let { getDetailPembelian(it) }


//            data?.order?.get(position)?.item?.images.let { detailBarangAdapter.addItems(it) }

            view.onClick {
                requireActivity().launchActivity<RincianDikemasActivity> {
                    putExtra("data", data)
                }

            }
//            view.rvListBarang.setHorizontalLayout(false)
//            detailBarangAdapter.clearItems()
//
//            data?.order?.let { detailBarangAdapter.addItems(it) }
//            view.rvListBarang.adapter = detailBarangAdapter

        })

    private var detailBarangAdapter: RecyclerViewAdapter<Order_> = RecyclerViewAdapter(
        R.layout.rv_gambarbarang,
        onBind = { view, data, position ->
            context?.let {
                Glide.with(it)
                    .load(data.item?.images?.get(0)?.name)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(view.tvGambarBarang)
            }


            view.textgambar.text=data.item?.name
        })


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dikemas, container, false)

        root.rvDikemas.setVerticalLayout(false)
        root.rvDikemas.adapter = dikemasAdapter

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
                        dikemasAdapter.clearItems()
                        if (getPembelianResponse.data?.packed?.size.toString() == "0") {
                            framekosong.visibility = View.VISIBLE
                            frameshimmer.visibility = View.GONE
                            rvDikemas.visibility = View.GONE
                            framemain.visibility = View.VISIBLE

                        } else {
                            getPembelianResponse.data?.packed?.let { dikemasAdapter.addItems(it) }
                            framemain.visibility = View.VISIBLE
                            frameshimmer.visibility = View.GONE
                            rvDikemas.visibility = View.VISIBLE
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