package com.asyabab.endora.ui.activity

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Window
import android.widget.RelativeLayout
import android.widget.Toast
import com.asyabab.endora.R
import com.asyabab.endora.base.BaseActivity
import com.asyabab.endora.data.models.general.GeneralResponse
import com.asyabab.endora.utils.launchActivityWithNewTask
import com.asyabab.endora.utils.onClick
import kotlinx.android.synthetic.main.activity_pengaturanlain.*
import kotlinx.android.synthetic.main.activity_ubahkatasandi1.btBack
import kotlinx.android.synthetic.main.popup_cancel.*


class PengaturanLainActivity : BaseActivity() {
    lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengaturanlain)

        btBack.onClick {
            finish()
        }

        btHapusAkun.onClick {
            val dialog = Dialog(context)
            dialog.apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                setContentView(R.layout.popup_cancel)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                window?.setLayout(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )
                if (tvTitle != null)
                    tvTitle.text =
                        "Apakah anda yakin akan menghapus akun ini?"
                btCancel.onClick {
                    dismiss()
                }
                btApply.onClick {
                    setHapusAkun()
                    dismiss()
                }

                show()
            }
        }

        btRate.onClick {
            rate()
        }

        btHapusRiwayatPencarian.onClick {
            val dialog = Dialog(context)
            dialog.apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                setContentView(R.layout.popup_cancel)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                window?.setLayout(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )
                if (tvTitle != null)
                    tvTitle.text =
                        "Apakah anda yakin akan menghapus riwayat pencarian?"
                btCancel.onClick {
                    dismiss()
                }
                btApply.onClick {
                    setHapusPencarian()
                    dismiss()
                }

                show()
            }
        }

    }

    fun rate() {
        val uri: Uri = Uri.parse("market://details?id=$packageName")
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)

        goToMarket.addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=$packageName")
                )
            )
        }

    }

    private fun setHapusAkun() {
        repository!!.setHapusAkun(
            repository?.getToken()!!,
            object : GeneralResponse.GeneralResponseCallback {
                override fun onSuccess(generalResponse: GeneralResponse) {
                    if (generalResponse.status == true) {
                        generalResponse.data
                        Toast.makeText(applicationContext, "Berhasil Dihapus", Toast.LENGTH_LONG)
                            .show()
                        repository!!.saveToken("")
                        launchActivityWithNewTask<LoginActivity>()
                    }
                }

                override fun onFailure(message: String) {
                    Toast.makeText(applicationContext, "Server Sedang Error", Toast.LENGTH_LONG)
                        .show()

                }

            })
    }

    private fun setHapusPencarian() {
        Log.d("teshapus","a")

        repository!!.setHapusPencarian(
            repository?.getToken()!!,
            object : GeneralResponse.GeneralResponseCallback {
                override fun onSuccess(generalResponse: GeneralResponse) {
                    if (generalResponse.status == true) {
                        Toast.makeText(applicationContext, "Berhasil Dihapus", Toast.LENGTH_LONG).show()
                        Log.d("teshapus",generalResponse.message.toString())
                    }
                }

                override fun onFailure(message: String) {
                    Toast.makeText(applicationContext, "Server Sedang Error", Toast.LENGTH_LONG).show()

                }

            })
    }
}