package com.asyabab.endora.ui.activity

import android.os.Bundle
import android.os.Handler
import com.asyabab.endora.R
import com.asyabab.endora.base.BaseActivity
import com.asyabab.endora.utils.launchActivityWithNewTask
import com.asyabab.endora.utils.onClick
import kotlinx.android.synthetic.main.activity_masuk.*

class KonfirmasiEmailActivity : BaseActivity() {
    lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_konfirmasiemail)
        btMasuk.onClick {
            launchActivityWithNewTask<LoginActivity>()
        }

    }


}