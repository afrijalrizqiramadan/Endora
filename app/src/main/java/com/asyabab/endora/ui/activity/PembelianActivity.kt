package com.asyabab.endora.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
import androidx.fragment.app.FragmentStatePagerAdapter
import com.asyabab.endora.R
import com.asyabab.endora.base.BaseActivity
import com.asyabab.endora.data.models.payment.getpembelian.GetPembelianResponse
import com.asyabab.endora.ui.fragment.*
import com.asyabab.endora.utils.launchActivityWithNewTask
import com.asyabab.endora.utils.onClick
import kotlinx.android.synthetic.main.activity_pembelian.*
import kotlinx.android.synthetic.main.fragment_dibatalkan.*

class PembelianActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembelian)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        btBack.onClick {
            launchActivityWithNewTask<BerandaActivity>()
        }

        val adapter = TabAdapter(
            supportFragmentManager,
            BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        viewPager.adapter = adapter

        tabs.setupWithViewPager(viewPager)

    }

    class TabAdapter(fm: FragmentManager, behavior: Int) : FragmentStatePagerAdapter(fm, behavior) {
        private val tabName : Array<String> = arrayOf("Belum Bayar", "Dikemas", "Dikirim", "Selesai", "Dibatalkan","Komplain")

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> BelumBayarFragment()
                1 -> DikemasFragment()
                2 -> DikirimFragment()
                3 -> SelesaiFragment()
                4 -> DibatalkanFragment()
                5 -> KomplainFragment()
                else -> BelumBayarFragment()
            }
        }

        override fun getCount(): Int = 6
        override fun getPageTitle(position: Int): CharSequence? = tabName[position]
    }

    override fun onBackPressed() {
        super.onBackPressed()
        launchActivityWithNewTask<BerandaActivity>()

    }

    fun getPembelian() {
        repository!!.getPembelian(
            repository?.getToken()!!,
            object : GetPembelianResponse.GetPembelianResponseCallback {
                override fun onSuccess(getPembelianResponse: GetPembelianResponse) {
                    if (getPembelianResponse.status == true) {
                        getPembelianResponse.data?.let { repository!!.savePembelian(it) }

                    }
                }

                override fun onFailure(message: String) {
                    Toast.makeText(this@PembelianActivity, "Server Sedang Error", Toast.LENGTH_LONG)
                        .show()

                }

            })
    }

}