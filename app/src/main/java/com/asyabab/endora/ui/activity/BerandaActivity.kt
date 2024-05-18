package com.asyabab.endora.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.asyabab.endora.R
import com.asyabab.endora.base.BaseActivity
import com.asyabab.endora.data.models.cart.keranjang.KeranjangResponse
import com.asyabab.endora.data.models.lokasi.getmain.GetMainResponse
import com.asyabab.endora.ui.activity.cart.TasBelanjaActivity
import com.asyabab.endora.ui.fragment.FavoritFragment
import com.asyabab.endora.ui.fragment.HomeFragment
import com.asyabab.endora.ui.fragment.ProfilSayaFragment
import com.asyabab.endora.utils.launchActivity
import com.asyabab.endora.utils.onClick
import kotlinx.android.synthetic.main.activity_beranda.*
import kotlinx.android.synthetic.main.activity_beranda.btTasBelanja
import kotlinx.android.synthetic.main.activity_beranda.tvIconNumber
import kotlinx.android.synthetic.main.activity_detailproduk.*


class BerandaActivity : BaseActivity() {
    private val mHomeFragment = HomeFragment()
    private val mFavoritFragment = FavoritFragment()
    private val mProfilSayaFragment = ProfilSayaFragment()
    private val mBerandaFragment = HomeFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beranda)
//        val toolbar: Toolbar = findViewById(R.id.toolbar)
//        setSupportActionBar(toolbar)


        btJelajah.onClick {
            launchActivity<JelajahActivity> { }
        }
        btTasBelanja.onClick {
            launchActivity<TasBelanjaActivity> { }
        }


        loadFragment(mHomeFragment)

        btMore.setOnClickListener {
            if (drawer_layout.isDrawerVisible(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START);
//                btMore.setImageResource(R.drawable.icon_more)

            } else {
//                btMore.setImageResource(R.drawable.icon_left)
                drawer_layout.openDrawer(GravityCompat.START);
            }
        }
        getKeranjang()
        getMain()

    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
//            btMore.setImageResource(R.drawable.icon_more)
        } else {
            super.onBackPressed()
        }
    }

    fun pageBantuan(view: View?) {
        launchActivity<BantuanActivity> {  }

    }

    fun pagePembelian(view: View?) {
        launchActivity<PembelianActivity> {  }

    }
    fun pageFavorit(view: View?) {
        loadFragmentBack(mFavoritFragment)
        drawer_layout.closeDrawer(GravityCompat.START)
    }
    fun pageProfilSaya(view: View?) {
        loadFragmentBack(mProfilSayaFragment)
        drawer_layout.closeDrawer(GravityCompat.START)
    }
    fun pageBeranda(view: View?) {
        loadFragment(mBerandaFragment)
        drawer_layout.closeDrawer(GravityCompat.START)
    }

    fun pagePencarian(view: View?) {
        launchActivity<PencarianActivity> { }
    }

    fun pagePrivacy(view: View?) {
        launchActivity<WebViewActivity> { }
    }

    fun getKeranjang() {
        repository!!.getKeranjang(
            repository?.getToken()!!,
            object : KeranjangResponse.KeranjangResponseCallback {
                override fun onSuccess(keranjangResponse: KeranjangResponse) {
                    if (keranjangResponse.status == true) {
                        tvIconNumber.text = keranjangResponse.data!!.size.toString()
                    }

                }

                override fun onFailure(message: String) {

                    Toast.makeText(applicationContext, "Server Sedang Error", Toast.LENGTH_LONG)
                        .show()


                }

            })
    }

    fun getMain() {
        repository!!.getMain(
            repository?.getToken()!!,
            object : GetMainResponse.GetMainResponseCallback {
                override fun onSuccess(getMainResponse: GetMainResponse) {
                    if (getMainResponse.status == true) {
                        repository!!.saveLokasi(getMainResponse.data?.subdistrictId.toString())
                        repository!!.saveLokasiKeterangan(getMainResponse.data?.category)
                    } else {
                        repository!!.saveLokasi("")

                    }
                }

                override fun onFailure(message: String) {
                    Log.e("Hasil", "Gagal Memuat" + message)


                }

            })
    }

}