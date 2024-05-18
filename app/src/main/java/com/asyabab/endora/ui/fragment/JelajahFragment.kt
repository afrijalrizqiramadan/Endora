package com.asyabab.endora.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.asyabab.endora.R
import com.asyabab.endora.base.BaseFragment
import com.asyabab.endora.data.models.billboard.jelajah.Data
import com.asyabab.endora.data.models.billboard.jelajah.JelajahResponse
import com.asyabab.endora.utils.*
import kotlinx.android.synthetic.main.activity_jelajah.*
import kotlinx.android.synthetic.main.fragment_jelajah.*
import kotlinx.android.synthetic.main.fragment_jelajah.view.*
import kotlinx.android.synthetic.main.rv_jelajah.view.*


class JelajahFragment : BaseFragment() {
    private val jelajahDetailFragment = JelajahDetailFragment()

    private var jelajahAdapter: RecyclerViewAdapter<Data> = RecyclerViewAdapter(
        R.layout.rv_jelajah,
        onBind = { view, data, position ->
            view.tvNamaJelajah.text = data.name
            data.coverSmall?.let { view.roundedImage.loadImageFromResources(context, it) }

            view.onClick {
                loadFragment(jelajahDetailFragment, data.id.toString(), data.name.toString())

            }

        })


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_jelajah, container, false)

        activity?.titletoolbar?.text = "Jelajah" // Key, default value

        root.rvJelajah.setVerticalLayout(false)
        root.rvJelajah.adapter = jelajahAdapter
        root.swipeResfresh.setOnRefreshListener(OnRefreshListener {
            getJelajah()
            root.swipeResfresh.isRefreshing = false;

        })
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