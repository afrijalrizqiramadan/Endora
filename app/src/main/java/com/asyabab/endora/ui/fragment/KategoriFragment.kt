package com.asyabab.endora.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.asyabab.endora.R
import com.asyabab.endora.base.BaseFragment
import com.asyabab.endora.data.models.billboard.jelajah.Data
import com.asyabab.endora.data.models.billboard.jelajah.JelajahResponse
import com.asyabab.endora.data.models.kategori.listkategori.Datum
import com.asyabab.endora.data.models.kategori.listkategori.ListKategoriResponse
import com.asyabab.endora.ui.activity.DetailProdukActivity
import com.asyabab.endora.ui.activity.JelajahActivity
import com.asyabab.endora.utils.*
import kotlinx.android.synthetic.main.fragment_jelajah.*
import kotlinx.android.synthetic.main.fragment_kategori.view.*
import kotlinx.android.synthetic.main.rv_jelajah.view.*

class KategoriFragment : BaseFragment() {


    private var jelajahAdapter: RecyclerViewAdapter<Data> = RecyclerViewAdapter(
        R.layout.rv_jelajah,
        onBind = { view, data, position ->
            view.tvNamaJelajah.text = data.name
            data.coverSmall?.let { view.roundedImage.loadImageFromResources(context, it) }

            view.onClick {
                requireActivity().launchActivity<JelajahActivity> {
                    putExtra("string1", data.id.toString())
                    putExtra("string2", data.name.toString())

                }
            }

        })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_kategori, container, false)

        root.rvKategori.setVerticalLayout(true)
        root.rvKategori.adapter = jelajahAdapter

        getJelajah()

        return root
    }

    fun getJelajah() {
        repository!!.getJelajah(
            repository?.getToken()!!,
            object : JelajahResponse.JelajahResponseCallback {
                override fun onSuccess(jelajahResponse: JelajahResponse) {
                    if (jelajahResponse.status == true) {
//                        if (keranjangResponse.data?.adaartikel == "ADA") {
                        jelajahAdapter.clearItems()
                        jelajahAdapter.addItems(jelajahResponse.data!!)

                        framemain.visibility = View.VISIBLE
                        frameshimmer.visibility = View.GONE
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