package com.asyabab.endora.ui.fragment

import android.app.Dialog
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.asyabab.endora.R
import com.asyabab.endora.base.BaseFragment
import com.asyabab.endora.data.models.general.GeneralResponse
import com.asyabab.endora.data.models.payment.getpembelian.*
import com.asyabab.endora.ui.activity.RincianDikemasActivity
import com.asyabab.endora.ui.activity.RincianDikirimActivity
import com.asyabab.endora.ui.activity.StatusPengirimanActivity
import com.asyabab.endora.utils.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.fragment_dikemas.*
import kotlinx.android.synthetic.main.fragment_dikemas.framekosong
import kotlinx.android.synthetic.main.fragment_dikemas.framemain
import kotlinx.android.synthetic.main.fragment_dikemas.frameshimmer
import kotlinx.android.synthetic.main.fragment_dikemas.swipeResfresh
import kotlinx.android.synthetic.main.fragment_dikemas.view.*
import kotlinx.android.synthetic.main.popup_konfirmasi.*
import kotlinx.android.synthetic.main.rv_dikirim.view.*

import kotlinx.android.synthetic.main.rv_gambarbarang.view.*

class DikirimFragment : BaseFragment() {
    private val viewPool = RecyclerView.RecycledViewPool()
    private var lat: String? = ""
    private var long: String? = ""
    private lateinit var lastLocation: Location

    private var dikemasAdapter: RecyclerViewAdapter<Sent> = RecyclerViewAdapter(
        R.layout.rv_dikirim,
        onBind = { view, data, position ->
            view.tvNoPesanan.text = " #" + data.id
            view.tvTanggalPemesanan.text = data.createdAt
            view.tvJumlahBarang.text = "Jumlah Produk " + data.order?.size.toString()
            view.tvKeterangan.text = "Konfirmasi terima produk sebelum " + data.expireDate

            val layoutManager = LinearLayoutManager(
                view.rvListBarang.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            val subItemAdapter =
                context?.let { data.order?.let { it1 -> RecyclerViewDikirimAdapter(it, it1) } }
            data.order?.size?.let { layoutManager.initialPrefetchItemCount = it }

//            data?.order?.let { detailBarangAdapter.addItems(it) }
            view.rvListBarang.setHorizontalLayout(false)
            view.rvListBarang.adapter = subItemAdapter
            view.rvListBarang.setRecycledViewPool(viewPool)


//            data.id?.let { getDetailPembelian(it) }
            view.tvKeteranganPengiriman.text =
                data.statusShipment?.let { data.histories?.get(0)?.status?.name } + " " + data.statusShipment?.let {
                    data.histories?.get(0)?.createdAt
                }
            view.btPaketDiterima.onClick {
                data.id?.let { popUpWindow(it) }
            }
            view.tvDetailPengiriman.onClick {
                requireActivity().launchActivity<StatusPengirimanActivity> {
                    putExtra("data", data)
                }
            }
//            data?.order?.get(position)?.item?.images.let { detailBarangAdapter.addItems(it) }

            view.onClick {
                requireActivity().launchActivity<RincianDikirimActivity> {
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
                        if (getPembelianResponse.data?.sent?.size.toString() == "0") {
                            framekosong.visibility = View.VISIBLE
                            frameshimmer.visibility = View.GONE
                            rvDikemas.visibility = View.GONE
                            framemain.visibility = View.VISIBLE

                        } else {
                            getPembelianResponse.data?.sent?.let { dikemasAdapter.addItems(it) }
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

    fun popUpWindow(id: String) {
        val dialog = context?.let { Dialog(it) }

        dialog?.apply {
            setContentView(R.layout.popup_konfirmasi)
            window?.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
//
            if (lat.equals("")) {
                lat = "0"
                long = "0"
            }

            btCancel.onClick { dismiss() }
            btApply.onClick {
                setConfirmation(id, lat!!, long!!)
                dismiss()
            }
            tvTitle.text = "Paket Diterima ?"


            show()
        }
    }


    fun setConfirmation(
        id: String,
        latitude: String,
        longitude: String
//        image: File
    ) {
        repository!!.setConfirmation(
            id,
            latitude,
            longitude,
//            image,
            repository?.getToken()!!,
            object : GeneralResponse.GeneralResponseCallback {
                override fun onSuccess(getGeneralResponse: GeneralResponse) {
                    if (getGeneralResponse.status == true) {
                        Toast.makeText(context, "Berhasil", Toast.LENGTH_LONG).show()
                        getPembelian()
                    } else {
                        Toast.makeText(context, "Gagal", Toast.LENGTH_LONG).show()

                    }
                }

                override fun onFailure(message: String) {
                    Toast.makeText(context, "Server Sedang Error", Toast.LENGTH_LONG).show()

                }

            })
    }
}