package com.asyabab.endora.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.asyabab.endora.R
import com.asyabab.endora.base.BaseActivity
import com.asyabab.endora.data.models.general.GeneralResponse
import com.asyabab.endora.data.models.user.ubahpassword.UbahPasswordResponse
import com.asyabab.endora.utils.launchActivity
import com.asyabab.endora.utils.onClick
import kotlinx.android.synthetic.main.activity_daftar.*
import kotlinx.android.synthetic.main.activity_daftar.inputUsername
import kotlinx.android.synthetic.main.activity_masuk.*
import kotlinx.android.synthetic.main.activity_rincianprofil.*
import kotlinx.android.synthetic.main.activity_ubahkatasandi2.*
import kotlinx.android.synthetic.main.activity_ubahkatasandi2.btBack
import kotlinx.android.synthetic.main.activity_ubahkatasandi2.inputPassword
import kotlinx.android.synthetic.main.activity_ubahkatasandi2.inputPasswordUlang
import kotlinx.android.synthetic.main.activity_ubahprofil.*


class UbahKataSandi2Activity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ubahkatasandi2)
        btUbahKataSandi2.onClick {
            val password = inputPassword.text.toString()
            val passwordulang = inputPasswordUlang.text.toString()
            if ((password == "") || (passwordulang == "")) {
                Toast.makeText(this@UbahKataSandi2Activity, "Isi semua masukan", Toast.LENGTH_SHORT)
                    .show()
            } else if (inputUsername.text.toString().length < 8) {
                Toast.makeText(
                    this@UbahKataSandi2Activity,
                    "Minimal 8 Karakter",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else if (passwordulang != password) {
                Toast.makeText(
                    this@UbahKataSandi2Activity,
                    "Password Harus Sama",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                loading?.show()

                setUbahPassword(
                    "",
                    inputPassword.text.toString(),
                    inputPasswordUlang.text.toString(),
                    repository?.getToken()!!
                )
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
                        launchActivity<RincianProfilActivity>()
                        Toast.makeText(
                            this@UbahKataSandi2Activity,
                            "Password Berhasil Diganti",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@UbahKataSandi2Activity,
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
                                            this@UbahKataSandi2Activity,
                                            "Minimal 8 Karakter",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()

                                    } else {
                                        Toast.makeText(
                                            this@UbahKataSandi2Activity,
                                            "Ganti Password Gagal",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()

                                    }

                                }

                                override fun onFailure(message: String) {
                                    Toast.makeText(
                                        this@UbahKataSandi2Activity,
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
                            "General Error",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }

            })
    }
}