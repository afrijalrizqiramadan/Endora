package com.asyabab.endora.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.asyabab.endora.R
import com.asyabab.endora.base.BaseFragment
import com.asyabab.endora.data.models.favorit.Data
import com.asyabab.endora.data.models.favorit.Favoritresponse
import com.asyabab.endora.data.models.general.GeneralResponse
import com.asyabab.endora.ui.activity.DetailProdukActivity
import com.asyabab.endora.utils.*
import kotlinx.android.synthetic.main.activity_jelajah.*
import kotlinx.android.synthetic.main.fragment_favorit.*
import kotlinx.android.synthetic.main.fragment_favorit.view.*
import kotlinx.android.synthetic.main.rv_favorit.view.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*


class FavoritFragment : BaseFragment() {
    private val mHomeFragment = HomeFragment()

    private var favoritAdapter: RecyclerViewAdapter<Data> = RecyclerViewAdapter(
        R.layout.rv_favorit,
        onBind = { view, data, position ->
            view.tvFavoritNama.text = data.item?.name
            data.item?.images?.get(0)?.name?.let {
                view.tvFavoritGambar.loadImageFromResources(
                    context,
                    it
                )
            }
            if (data.item?.promo?.isEmpty()!!) {
                view.tvKeteranganPromo.visibility = View.INVISIBLE
                view.tvKeterangan.visibility = View.INVISIBLE
                view.tvHarga.text = data.item?.price?.convertRupiah()

            } else {
                var hasil = data.item?.price!! * data.item?.promo!![0].discount!! / 100
                view.tvKeterangan.visibility = View.VISIBLE
                view.tvKeteranganPromo.visibility = View.VISIBLE
                if (hasil < data.item?.promo!![0].maximum!!) {
                    view.tvHarga.text = (data.item?.price!! - hasil).convertRupiah()
                } else {
                    view.tvHarga.text =
                        (data.item?.price!! - data.item?.promo!![0].maximum!!).convertRupiah()

                }
                view.tvKeteranganPromo.text = data.item?.price?.convertRupiah()
                view.tvKeterangan.text = " Diskon " + data.item?.promo!![0].discount!! + "% "

            }
            view.btFavoritHapus.onClick {
                setFavorit(data.itemId.toString())

            }
            view.onClick {
                requireActivity().launchActivity<DetailProdukActivity> {
                    putExtra("data", data.itemId.toString())
                }

            }


        })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_favorit, container, false)
        activity?.titletoolbar?.text = "Favorit Saya" // Key, default value

        root.rvFavorit.setVerticalLayout(true)


        root.rvFavorit.adapter = favoritAdapter

        root.btLanjutBelanja.onClick {
            loadFragment(mHomeFragment)
        }

        getFavorit()

        root.swipeResfresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            getFavorit()
            swipeResfresh.isRefreshing = false;

        })



        return root
    }

    fun onBackPressed() {
        loadFragment(mHomeFragment)
    }

    fun Any.convertRupiah(): String {
        val df = DecimalFormat("#,###,##0")


        val strFormat = df.format(this)
        var bilangan = "Rp " + strFormat
        return bilangan
    }

    fun setFavorit(item: String) {
        repository!!.setFavorit(
            item,
            repository?.getToken()!!,
            object : GeneralResponse.GeneralResponseCallback {
                override fun onSuccess(generalResponse: GeneralResponse) {
                    if (generalResponse.status == true) {
                        Toast.makeText(context, "" + generalResponse.message!!, Toast.LENGTH_SHORT)
                            .show()
                        getFavorit()
                    }
                }

                override fun onFailure(message: String) {
                }

            })
    }

    fun getFavorit() {
        repository!!.getFavorit(
            repository?.getToken()!!,
            object : Favoritresponse.FavoritResponseCallback {
                override fun onSuccess(favoritresponse: Favoritresponse) {
                    if (favoritresponse.status == true) {
//                        if (keranjangResponse.data?.adaartikel == "ADA") {
                        favoritAdapter.clearItems()
                        favoritAdapter.addItems(favoritresponse.data!!)
                        tvJumlahBarang.text = "Jumlah Item ("+favoritresponse.data!!.size.toString()+")"

                        framemain.visibility=View.VISIBLE
                        frameshimmer.visibility=View.GONE
//                        } else {
//
//                        }
                    }
                }

                override fun onFailure(message: String) {
                    Toast.makeText(context, "Server Sedang Error", Toast.LENGTH_LONG).show()

                }

            })
    }
}