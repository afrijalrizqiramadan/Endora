package com.asyabab.endora.ui.activity

import android.os.Bundle
import com.asyabab.endora.R
import com.asyabab.endora.base.BaseActivity
import com.asyabab.endora.utils.onClick
import kotlinx.android.synthetic.main.activity_metodepembayaran.*

class MetodePembayaranActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metodepembayaran)

        btBack.onClick {
            finish()
        }

        rlBayarBRI.onClick {
//            val jsonObject = JSONObject()
//            repository?.simulatePayment(repository?.getToken().toString(),
//                jsonObject, object : GeneralResponse.GeneralResponseCallback {
//                    override fun onSuccess(generalResponse: GeneralResponse) {
//
//                    }
//
//                    override fun onFailure(message: String) {
//
//                    }
//
//                }
//            )
        }
    }
}