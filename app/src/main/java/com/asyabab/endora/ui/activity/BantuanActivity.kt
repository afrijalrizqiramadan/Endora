package com.asyabab.endora.ui.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import com.asyabab.endora.R
import com.asyabab.endora.base.BaseActivity
import com.asyabab.endora.utils.launchActivity
import com.asyabab.endora.utils.onClick
import kotlinx.android.synthetic.main.activity_bantuan.*

class BantuanActivity : BaseActivity() {
    lateinit var handler: Handler


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bantuan)
        btBack.onClick {
            finish()
        }
        btEmailKami.onClick {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:email@endoracare.com")
            }
            startActivity(Intent.createChooser(emailIntent, "Kirim Email"))
        }
        btPusatBantuan.onClick {
            launchActivity<WebViewActivity> { }
        }
        btUmpanBalik.onClick {
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$packageName")
                    )
                )
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                    )
                )
            }
        }

    }
}