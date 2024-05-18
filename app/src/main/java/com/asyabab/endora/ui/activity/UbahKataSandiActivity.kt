package com.asyabab.endora.ui.activity

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.asyabab.endora.R
import com.asyabab.endora.base.BaseActivity
import com.asyabab.endora.data.models.general.GeneralResponse
import com.asyabab.endora.data.models.user.ubahpassword.UbahPasswordResponse
import com.asyabab.endora.utils.launchActivity
import com.asyabab.endora.utils.onClick
import kotlinx.android.synthetic.main.activity_ubahkatasandi1.*
import kotlinx.android.synthetic.main.activity_ubahkatasandi1.btBack
import kotlinx.android.synthetic.main.activity_ubahkatasandi1.inputPassword
import kotlinx.android.synthetic.main.activity_ubahkatasandi1.inputPasswordUlang
import kotlinx.android.synthetic.main.activity_ubahkatasandi2.*

class UbahKataSandiActivity : BaseActivity() {
    lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ubahkatasandi1)
        btUbahKataSandi.onClick {
            val password = inputPassword.text.toString()
            val passwordulang = inputPasswordUlang.text.toString()
            if ((password == "") || (passwordulang == "")) {
                Toast.makeText(this@UbahKataSandiActivity, "Isi Password", Toast.LENGTH_SHORT)
                    .show()
            } else if (passwordulang != password) {
                Toast.makeText(
                    this@UbahKataSandiActivity,
                    "Password Harus Sama",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                repository?.getToken()?.let { setUbahPassword("", password, passwordulang, it) }
            }
        }
        btBack.onClick {
            finish()
        }

    }

    fun setUbahPassword(password: String, newpassword: String, newpassword2: String, auth: String) {
        repository?.setUbahPasswrod(
            password,
            newpassword,
            newpassword2,
            auth,
            object : GeneralResponse.GeneralResponseCallback {
                override fun onSuccess(generalResponse: GeneralResponse) {
                    Log.d("Ubah", "signInsuccess")
                    loading?.dismiss()
                    if (generalResponse.status == true) {
                        launchActivity<BerhasilDaftarActivity>()
                        finish()
                        Toast.makeText(
                            this@UbahKataSandiActivity,
                            "Password Berhasil Diganti",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@UbahKataSandiActivity,
                            "Password Sebelumnya Salah",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }

                override fun onFailure(message: String) {
                    if (message == "Kondisi2") {
                        repository?.setUbahPasswrod2(
                            password,
                            newpassword,
                            newpassword2,
                            auth,
                            object : UbahPasswordResponse.UbahPasswordResponseCallback {
                                override fun onSuccess(ubahPasswordResponse: UbahPasswordResponse) {
                                    Log.d("Login", "signInsuccess")
                                    loading?.dismiss()

                                    if (ubahPasswordResponse.message?.newPassword?.get(0) == "validation.min.string") {
                                        Toast.makeText(
                                            this@UbahKataSandiActivity,
                                            "Minimal 8 Karakter",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()

                                    } else {
                                        Toast.makeText(
                                            this@UbahKataSandiActivity,
                                            "Ganti Password Gagal",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()

                                    }

                                }

                                override fun onFailure(message: String) {
                                    Toast.makeText(
                                        this@UbahKataSandiActivity,
                                        "Ganti Password Gagal",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()

                                }


                            }
                        )

                    } else {
                        Log.d("Login", message)
                        loading?.dismiss()
                        Toast.makeText(
                            applicationContext,
                            message,
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }

            })
    }
}