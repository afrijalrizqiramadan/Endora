package com.asyabab.endora.ui.activity

import android.os.Bundle
import android.os.Handler
import com.asyabab.endora.R
import com.asyabab.endora.base.BaseActivity
import com.asyabab.endora.utils.onClick
import kotlinx.android.synthetic.main.activity_ubahkatasandi1.btBack

class PemberitahuanActivity : BaseActivity() {
    lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pemberitahuan)

        btBack.onClick {
            finish()
        }

    }
}