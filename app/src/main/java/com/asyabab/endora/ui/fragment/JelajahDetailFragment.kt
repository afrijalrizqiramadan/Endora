package com.asyabab.endora.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.asyabab.endora.R
import com.asyabab.endora.base.BaseFragment
import com.asyabab.endora.data.models.billboard.detailbillboard.Brand
import com.asyabab.endora.data.models.billboard.detailbillboard.Category
import com.asyabab.endora.data.models.billboard.detailbillboard.GetDetailBillboard
import com.asyabab.endora.data.models.billboard.detailbillboard.Offer
import com.asyabab.endora.ui.activity.DetailBrandActivity
import com.asyabab.endora.ui.activity.DetailOfferActivity
import com.asyabab.endora.ui.activity.JelajahActivity
import com.asyabab.endora.utils.*
import kotlinx.android.synthetic.main.activity_jelajah.*
import kotlinx.android.synthetic.main.fragment_detailjelajah.*
import kotlinx.android.synthetic.main.fragment_detailjelajah.view.*
import kotlinx.android.synthetic.main.rv_kategori.view.*
import kotlinx.android.synthetic.main.rv_offermedium.view.*
import kotlinx.android.synthetic.main.rv_promolain.view.*


class JelajahDetailFragment : BaseFragment() {

    private val offerFragment = OfferFragment()
    var idbillboard = ""
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
    private var brandAdapter: RecyclerViewAdapter<Brand> = RecyclerViewAdapter(
        R.layout.rv_offermedium,
        onBind = { view, data, position ->
            data.image?.let {
                view.tvOfferMediumGambar.loadImageFromResources(
                    context,
                    it
                )
            }
            view.onClick {
                requireActivity().launchActivity<DetailBrandActivity> {
                    putExtra("data", data.id)
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
        val root = inflater.inflate(R.layout.fragment_detailjelajah, container, false)

        val bundle = this.arguments
        if (bundle != null) {
            idbillboard = bundle.getString("string1", "") // Key, default value
            activity?.titletoolbar?.text = bundle.getString("string2", "") // Key, default value
        }

        root.rvOffer.setVerticalLayout(false)
        root.rvOffer.adapter = offerAdapter

        root.rvOfferMedium.setVerticalLayout(false)
        root.rvOfferMedium.adapter = brandAdapter
        root.rvKategori.setHorizontalLayout(false)
        root.rvKategori.adapter = kategoriAdapter
        root.btLihatSemua.onClick {
            loadFragmentBack(offerFragment)
        }
        root.swipeResfresh.setOnRefreshListener(OnRefreshListener {
            getDetailJelajah(idbillboard)
            swipeResfresh.isRefreshing = false;

        })
        getDetailJelajah(idbillboard)

        return root
    }


    fun getDetailJelajah(id: String) {
        repository!!.getDetailBillboard(
            id,
            repository?.getToken()!!,
            object : GetDetailBillboard.GetDetailBillboardResponseCallback {
                override fun onSuccess(getDetailBillboard: GetDetailBillboard) {
                    if (getDetailBillboard.status == true) {
                        offerAdapter.clearItems()
                        getDetailBillboard.data!!.offers?.let { offerAdapter.addItems(it) }
                        brandAdapter.clearItems()
                        getDetailBillboard.data!!.brands?.let { brandAdapter.addItems(it) }
                        kategoriAdapter.clearItems()
                        getDetailBillboard.data?.categories?.let { kategoriAdapter.addItems(it) }
                        if (getDetailBillboard.data!!.offers?.size!! == 0) {
                            btLihatSemua.visibility = View.GONE
                        }
                    }
                }

                override fun onFailure(message: String) {
                    Toast.makeText(context, "Server Sedang Error" + message, Toast.LENGTH_LONG)
                        .show()

                }

            })
    }
}

