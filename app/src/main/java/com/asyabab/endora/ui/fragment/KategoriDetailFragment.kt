package com.asyabab.endora.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.asyabab.endora.R
import com.asyabab.endora.base.BaseFragment
import com.asyabab.endora.data.models.general.GeneralResponse
import com.asyabab.endora.data.models.kategori.detailkategori.Category
import com.asyabab.endora.data.models.kategori.detailkategori.DetailKategoriResponse
import com.asyabab.endora.data.models.kategori.detailkategori.Item
import com.asyabab.endora.data.models.kategori.detailkategori.Offer
import com.asyabab.endora.ui.activity.DetailOfferActivity
import com.asyabab.endora.ui.activity.DetailProdukActivity
import com.asyabab.endora.ui.activity.JelajahActivity
import com.asyabab.endora.utils.*
import kotlinx.android.synthetic.main.activity_detailoffer.*
import kotlinx.android.synthetic.main.activity_jelajah.*
import kotlinx.android.synthetic.main.fragment_detailjelajah.*
import kotlinx.android.synthetic.main.fragment_detailjelajah.swipeResfresh
import kotlinx.android.synthetic.main.fragment_detailjelajah.view.*
import kotlinx.android.synthetic.main.rv_kategori.view.*
import kotlinx.android.synthetic.main.rv_produk.view.*
import kotlinx.android.synthetic.main.rv_promolain.view.*
import kotlinx.android.synthetic.main.rv_promolain.view.tvGambar
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*


class KategoriDetailFragment : BaseFragment() {

    private val offerFragment = OfferFragment()
    var idkategori = ""
    private var offerAdapter: RecyclerViewAdapter<Offer> = RecyclerViewAdapter(
        R.layout.rv_promolain,
        onBind = { view, data, position ->
            data.cover?.let {
                view.tvGambar.loadImageFromResources(
                    context,
                    it
                )
            }

            view.tvNamaPromo.text = data.name

            view.onClick {
                requireActivity().launchActivity<DetailOfferActivity> {
                    putExtra("data", data.id)
                    putExtra("nama", data.name)
                }

            }
        })
    private var itemAdapter: RecyclerViewAdapter<Item> = RecyclerViewAdapter(
        R.layout.rv_produk,
        onBind = { view, data, position ->
            view.tvNama.text = data.name
            view.tvHarga.text = data.price?.convertRupiah()
            if (data.isFavorite!!) {
                context?.let {
                    view.btIconFavorit.loadImageFromResources(
                        it,
                        R.drawable.icon_lovered
                    )
                }
            } else {
                context?.let {
                    view.btIconFavorit.loadImageFromResources(
                        it,
                        R.drawable.icon_lovewhite
                    )
                }

            }
            if (data.promo?.isEmpty()!!) {
                view.tvKeteranganPromo.visibility = View.INVISIBLE
                view.tvKeterangan.visibility = View.INVISIBLE
                view.tvHarga.text = data.price?.convertRupiah()

            } else {
                var hasil = data.price!! * data.promo!![0].discount!! / 100
                view.tvKeterangan.visibility = View.VISIBLE
                view.tvKeteranganPromo.visibility = View.VISIBLE
                if (hasil < data.promo!![0].maximum!!) {
                    view.tvHarga.text = (data.price!! - hasil).convertRupiah()
                } else {
                    view.tvHarga.text = (data.price!! - data.promo!![0].maximum!!).convertRupiah()

                }
                view.tvKeteranganPromo.text = data.price?.convertRupiah()
                view.tvKeterangan.text = " Diskon " + data.promo!![0].discount!! + "% "

            }
//            view.tvKeterangan.text = data.description
//            data.brandId?.let { view.tvGambar.loadImageFromResources(this, it) }
            view.btIconFavorit.onClick {
                repository!!.setFavorit(
                    data.id.toString(),
                    repository?.getToken()!!,
                    object : GeneralResponse.GeneralResponseCallback {
                        override fun onSuccess(generalResponse: GeneralResponse) {
                            if (generalResponse.status == true) {
                                if (generalResponse.message == "Item Favorit Berhasil Dihapus!!") {
                                    Toast.makeText(
                                        context,
                                        generalResponse.message,
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                    context?.let {
                                        view.btIconFavorit.loadImageFromResources(
                                            it,
                                            R.drawable.icon_lovewhite
                                        )
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        generalResponse.message,
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                    view.btIconFavorit.loadImageFromResources(
                                        context,
                                        R.drawable.icon_lovered
                                    )
                                }
                            } else {
                                Toast.makeText(context, "Gagal Menambahkan", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }

                        override fun onFailure(message: String) {
                            Log.e("Hasil", "Gagal Memuat" + message)

                            Toast.makeText(context, "Gagal Memuat" + message, Toast.LENGTH_SHORT)
                                .show()
                        }

                    })
            }
            data.images?.get(0)?.name?.let { view.tvGambar.loadImageFromResources(context, it) }
            view.onClick {
                requireActivity().launchActivity<DetailProdukActivity> {
                    putExtra("data", data.id.toString())
                }
            }
        })

    private var kategoriAdapter: RecyclerViewAdapter<Category> = RecyclerViewAdapter(
        R.layout.rv_kategori,
        onBind = { view, data, position ->
            data.image?.let {
                view.tvKategoriGambar.loadImageFromResources(
                    context,
                    it
                )
            }
            view.tvKategoriNama.text = data.name

            view.onClick {
                val intent = Intent(context, JelajahActivity::class.java)
                intent.putExtra("strings1", data.id.toString())
                intent.putExtra("strings2", data.name.toString())
                startActivityForResult(intent, Activity.RESULT_OK);
            }
        })


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_detailkategori, container, false)

        val bundle = this.arguments
        if (bundle != null) {
            idkategori = bundle.getString("string1", "") // Key, default value
            activity?.titletoolbar?.text = bundle.getString("string2", "") // Key, default value
        }

        root.rvOffer.setVerticalLayout(false)
        root.rvOffer.adapter = offerAdapter


        val layoutManager = GridLayoutManager(context, 2)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        root.rvOfferMedium.layoutManager = layoutManager
        root.rvOfferMedium.adapter = itemAdapter

        root.rvKategori.setHorizontalLayout(false)
        root.rvKategori.adapter = kategoriAdapter
        root.btLihatSemua.onClick {
            loadFragment(offerFragment)
        }
        root.swipeResfresh.setOnRefreshListener(OnRefreshListener {
            getDetailKategori(idkategori)
            swipeResfresh.isRefreshing = false;

        })
        getDetailKategori(idkategori)
        return root
    }

    fun Any.convertRupiah(): String {
        val df = DecimalFormat("#,###,##0")


        val strFormat = df.format(this)
        var bilangan = "Rp " + strFormat
        return bilangan
    }

    fun getDetailKategori(id: String) {
        repository!!.getDetailKategori(
            id,
            repository?.getToken()!!,
            object : DetailKategoriResponse.DetailKategoriResponseCallback {
                override fun onSuccess(detailKategoriResponse: DetailKategoriResponse) {
                    if (detailKategoriResponse.status == true) {
                        offerAdapter.clearItems()
                        detailKategoriResponse.data!!.offers?.let { offerAdapter.addItems(it) }
                        itemAdapter.clearItems()
                        detailKategoriResponse.data!!.items?.let { itemAdapter.addItems(it) }
                        kategoriAdapter.clearItems()
                        detailKategoriResponse.data!!.categories?.let { kategoriAdapter.addItems(it) }
                        if (detailKategoriResponse.data!!.offers?.size!! == 0) {
                            btLihatSemua.visibility = View.GONE
                        } else {
                            btLihatSemua.visibility = View.VISIBLE

                        }
                    }
                }

                override fun onFailure(message: String) {
                    Toast.makeText(context, "Server Sedang Error", Toast.LENGTH_LONG).show()

                }

            })
    }
}

