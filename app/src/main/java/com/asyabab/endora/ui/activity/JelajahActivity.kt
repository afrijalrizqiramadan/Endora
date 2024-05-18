package com.asyabab.endora.ui.activity

import android.os.Bundle
import com.asyabab.endora.base.BaseActivity
import com.asyabab.endora.ui.activity.cart.TasBelanjaActivity
import com.asyabab.endora.ui.fragment.JelajahDetailFragment
import com.asyabab.endora.ui.fragment.JelajahFragment
import com.asyabab.endora.ui.fragment.KategoriDetailFragment
import com.asyabab.endora.utils.launchActivity
import com.asyabab.endora.utils.onClick
import kotlinx.android.synthetic.main.activity_jelajah.*


class JelajahActivity : BaseActivity() {
    private val jelajahFragment = JelajahFragment()
    private val jelajahDetailFragment = JelajahDetailFragment()
    private val kategoriDetailFragment = KategoriDetailFragment()

//    lateinit var handler: Handler
//    private var jelajahAdapter: RecyclerViewAdapter<Data> = RecyclerViewAdapter(
//        R.layout.rv_jelajah,
//        onBind = { view, data, position ->
//            view.tvNamaJelajah.text = data.name
//            data.coverSmall?.let { view.roundedImage.loadImageFromResources(this, it) }
//
//            view.onClick {
//                launchActivity<DetailProdukActivity> {
//                    putExtra("data", data.id)
//                }
//            }
//        })

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == RESULT_OK) {
//            var idkategori = data?.getStringExtra("strings1").toString()
//            var namakategori = data?.getStringExtra("strings2").toString()
//            loadFragment(kategoriDetailFragment, idkategori, namakategori)
//
//        }else {
//            loadFragment(jelajahFragment)
//        }
////        var idkategori = "A"
////        var namakategori = "B"
//
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.asyabab.endora.R.layout.activity_jelajah)
        tvIconNumber?.text = repository!!.getData("jumlahkeranjang")
        btTasBelanja.onClick {
            launchActivity<TasBelanjaActivity>()
        }

        btSearch.onClick {
            launchActivity<PencarianActivity>()
        }

        btBack.onClick {
            onBackPressed()
        }

        val extras = intent.extras
        if (extras?.getString("string1") != null) {
            val fragment = extras.getInt("fragment")
            val string1 = extras.getString("string1")
            val string2 = extras.getString("string2")
//            Toast.makeText(this, "This is item in position $fragment", Toast.LENGTH_SHORT)
//                .show()
            loadFragment(jelajahDetailFragment, string1!!, string2!!)

        } else if (extras?.getString("strings1") != null) {
            var idkategori = extras?.getString("strings1").toString()
            var namakategori = extras?.getString("strings2").toString()
            loadFragment(kategoriDetailFragment, idkategori, namakategori)

        } else {
            loadFragment(jelajahFragment)
        }

//        rvJelajah.setVerticalLayout(true)
//        rvJelajah.adapter = jelajahAdapter
//
//        getJelajah()
    }

//    fun getJelajah() {
//        repository!!.getJelajah(
//            repository?.getToken()!!,
//            object : JelajahResponse.JelajahResponseCallback {
//                override fun onSuccess(jelajahResponse: JelajahResponse) {
//                    if (jelajahResponse.status == true) {
////                        if (keranjangResponse.data?.adaartikel == "ADA") {
//                        jelajahAdapter.clearItems()
//                        jelajahAdapter.addItems(jelajahResponse.data!!)
//
//                        framemain.visibility= View.VISIBLE
//                        frameshimmer.visibility= View.GONE
////                        } else {
////
////                        }
//                    }
//                }
//
//                override fun onFailure(message: String) {
//                    Toast.makeText(applicationContext, "Server Sedang Error", Toast.LENGTH_LONG).show()
//
//                }
//
//            })
//    }

}