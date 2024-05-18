package com.asyabab.endora.ui.activity

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.asyabab.endora.R
import com.asyabab.endora.base.BaseActivity
import com.asyabab.endora.data.models.general.GeneralResponse
import com.asyabab.endora.utils.launchActivity
import com.asyabab.endora.utils.launchActivityWithNewTask
import com.asyabab.endora.utils.onClick
import kotlinx.android.synthetic.main.activity_lupasandi.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class LupaSandiActivity : BaseActivity() {
    lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lupasandi)
        btKirim.onClick {
            if (inputEmail.text == null) {
                Toast.makeText(
                    applicationContext,
                    "Masukkan Email",
                    Toast.LENGTH_LONG
                )
                    .show()
            } else if (!emailValidator(inputEmail.text.toString())) {
                Toast.makeText(
                    applicationContext,
                    "Email Salah",
                    Toast.LENGTH_LONG
                )
                    .show()
            } else {
                loading?.show()
                setLupaPassword(inputEmail.text.toString())
            }
        }



        btBack.onClick {
            finish()
        }
    }

    fun emailValidator(email: String?): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val EMAIL_PATTERN =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        pattern = Pattern.compile(EMAIL_PATTERN)
        matcher = pattern.matcher(email)
        return matcher.matches()
    }

    private fun setLupaPassword(email: String?) {
        repository!!.setLupaPassword(
            email!!,
            object : GeneralResponse.GeneralResponseCallback {
                override fun onSuccess(generalResponse: GeneralResponse) {
                    if (generalResponse.status == true) {
                        loading?.dismiss()
                        generalResponse.data
                        Toast.makeText(applicationContext, "Berhasil Dikirim", Toast.LENGTH_LONG)
                            .show()
                        repository!!.saveToken("")
                        launchActivity<KonfirmasiEmailActivity> { }
                        finish()
                    } else {
                        loading?.dismiss()
                        Toast.makeText(
                            applicationContext,
                            "User Tidak Ditemukan",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }

                override fun onFailure(message: String) {
                    loading?.dismiss()

                    Toast.makeText(applicationContext, "Server Sedang Error", Toast.LENGTH_LONG)
                        .show()

                }

            })
    }
}