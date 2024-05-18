package com.asyabab.endora.ui.activity

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.asyabab.endora.R
import com.asyabab.endora.base.BaseActivity
import com.asyabab.endora.data.models.general.GeneralResponse
import com.asyabab.endora.utils.launchActivity
import com.asyabab.endora.utils.launchActivityWithNewTask
import com.asyabab.endora.utils.loadImageFromResources
import com.asyabab.endora.utils.onClick
import kotlinx.android.synthetic.main.activity_opengambar.*


class GambarPengirimanActivity : BaseActivity() {
    lateinit var handler: Handler
    var url: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opengambar)
        url = intent.getSerializableExtra("data").toString()

        btBack.onClick {
            finish()
        }

        tvGambar.loadImageFromResources(this, url)
    }

}