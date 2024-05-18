package com.asyabab.endora.ui.activity

import android.os.Bundle
import android.os.Handler
import android.util.AndroidRuntimeException
import com.asyabab.endora.R
import com.asyabab.endora.base.BaseActivity
import com.asyabab.endora.data.models.payment.getpembelian.Finished
import com.asyabab.endora.data.models.payment.getpembelian.History_
import com.asyabab.endora.data.models.payment.getpembelian.History__
import com.asyabab.endora.data.models.payment.getpembelian.Sent
import com.asyabab.endora.utils.*
import kotlinx.android.synthetic.main.activity_statuspengiriman.*
import kotlinx.android.synthetic.main.rv_history.view.*


class StatusPengirimanActivity : BaseActivity() {
    var item = Sent()
    var item2 = Finished()

    lateinit var handler: Handler

    private var getStatusPengirimanAdapter: RecyclerViewAdapter<History_> = RecyclerViewAdapter(
        R.layout.rv_history,
        onBind = { view, data, position ->
            view.tvKeterangan.text = data.status?.name
            view.tvTanggalKeterangan.text = data.createdAt
            data.image?.let { view.tvGambar.loadImageFromResources(this, it) }
            view.tvGambar.onClick {
                launchActivity<GambarPengirimanActivity> {
                    putExtra("data", data.image)
                }
            }
        })
    private var getStatusPengirimanAdapter2: RecyclerViewAdapter<History__> = RecyclerViewAdapter(
        R.layout.rv_history,
        onBind = { view, data, position ->
            view.tvKeterangan.text = data.status?.name
            view.tvTanggalKeterangan.text = data.createdAt

            data.image?.let { view.tvGambar.loadImageFromResources(this, it) }
            view.tvGambar.onClick {
                launchActivity<GambarPengirimanActivity> {
                    putExtra("data", data.image)
                }
            }
        })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statuspengiriman)
        btBack.onClick {
            finish()
        }



        loading?.show()

        getStatusPengirimanAdapter.clearItems()
        getStatusPengirimanAdapter2.clearItems()
        try {
            rvHistoryPengiriman.setVerticalLayout(false)
            rvHistoryPengiriman.adapter = getStatusPengirimanAdapter
            item = intent.getSerializableExtra("data") as Sent
            item.histories?.let { getStatusPengirimanAdapter.addItems(it) }

        } catch (E: ClassCastException) {
            rvHistoryPengiriman.setVerticalLayout(false)
            rvHistoryPengiriman.adapter = getStatusPengirimanAdapter2
            item2 = intent.getSerializableExtra("data") as Finished
            item2.histories?.let { getStatusPengirimanAdapter2.addItems(it) }

        }
        loading?.hide()

    }
}