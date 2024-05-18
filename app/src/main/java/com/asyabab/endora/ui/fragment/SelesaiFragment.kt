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
import com.asyabab.endora.data.models.general.GeneralResponse
import com.asyabab.endora.data.models.payment.getpembelian.Finished
import com.asyabab.endora.data.models.payment.getpembelian.GetPembelianResponse
import com.asyabab.endora.ui.activity.RincianSelesaiActivity
import com.asyabab.endora.ui.activity.StatusPengirimanActivity
import com.asyabab.endora.utils.*
import kotlinx.android.synthetic.main.fragment_selesai.*
import kotlinx.android.synthetic.main.fragment_selesai.view.*
import kotlinx.android.synthetic.main.rv_belumbayar.view.*
import kotlinx.android.synthetic.main.rv_selesai.view.*
import kotlinx.android.synthetic.main.rv_selesai.view.rvListBarang
import kotlinx.android.synthetic.main.rv_selesai.view.tvJumlahBarang
import kotlinx.android.synthetic.main.rv_selesai.view.tvKeterangan
import kotlinx.android.synthetic.main.rv_selesai.view.tvNoPesanan
import kotlinx.android.synthetic.main.rv_selesai.view.tvTanggalPemesanan

class SelesaiFragment : BaseFragment() {
    private val viewPool = RecyclerView.RecycledViewPool()

    private var selesaiAdapter: RecyclerViewAdapter<Finished> = RecyclerViewAdapter(
        R.layout.rv_selesai,
        onBind = { view, data, position ->
            view.tvNoPesanan.text = " #" + data.id
            view.tvTanggalPemesanan.text = data.createdAt

            view.tvJumlahBarang.text = "Jumlah Produk " + data.order?.size.toString()
            var jumlah = data.order?.size?.minus(1)
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
                context?.let { data.order?.let { it1 -> RecyclerViewSelesaiAdapter(it, it1) } }
            data.order?.size?.let { layoutManager.initialPrefetchItemCount = it }
            view.rvListBarang.setHorizontalLayout(false)
            view.rvListBarang.adapter = subItemAdapter
            view.rvListBarang.setRecycledViewPool(viewPool)
            if (data?.review?.review.equals(null)) {
                view.tvKeterangan.text =
                    "Beri ulasan Anda untuk pelayanan kami menangani pesanan Anda"
                view.btKirimUlasan.visibility = View.VISIBLE
            } else {
                view.tvKeterangan.text = "Selesai memberi ulasan"
                view.btKirimUlasan.visibility = View.GONE
            }
            view.tvKeteranganPengiriman.text =
                data.statusShipment?.let { data.histories?.get(0)?.status?.name } + " " + data.statusShipment?.let {
                    data.histories?.get(0)?.createdAt
                }
//            data.id?.let { getDetailPembelian(it) }
            view.btKirimUlasan.onClick {
                requireActivity().launchActivity<RincianSelesaiActivity> {
                    putExtra("data", data)


                }
            }
            view.btDetailPengiriman.onClick {
                requireActivity().launchActivity<StatusPengirimanActivity> {
                    putExtra("data", data)

                }
            }

//            view.btKirimUlasan.onClick {
//
//            }
//            data?.order?.get(position)?.item?.images.let { detailBarangAdapter.addItems(it) }

            view.onClick {
                requireActivity().launchActivity<RincianSelesaiActivity> {
                    putExtra("data", data)
                }

            }
        })
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_selesai, container, false)

        root.rvSelesai.setVerticalLayout(true)
        root.rvSelesai.adapter = selesaiAdapter

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
                        selesaiAdapter.clearItems()
                        if (getPembelianResponse.data?.finished?.size.toString() == "0") {
                            framekosong.visibility = View.VISIBLE
                            frameshimmer.visibility = View.GONE
                            rvSelesai.visibility = View.GONE
                            framemain.visibility = View.VISIBLE

                        } else {
                            getPembelianResponse.data?.finished?.let { selesaiAdapter.addItems(it) }
                            framemain.visibility = View.VISIBLE
                            frameshimmer.visibility = View.GONE
                            rvSelesai.visibility = View.VISIBLE
                            framekosong.visibility = View.GONE

                        }


                    }
                }

                override fun onFailure(message: String) {
                    Toast.makeText(context, "Server Sedang Error", Toast.LENGTH_LONG).show()

                }

            })
    }

    fun setUlasan(
        id: String,
        rating: String,
        review: String
//        image: File
    ) {
        repository!!.setUlasan(
            id,
            rating,
            review,
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