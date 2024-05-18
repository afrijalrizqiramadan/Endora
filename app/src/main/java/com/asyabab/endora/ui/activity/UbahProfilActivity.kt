package com.asyabab.endora.ui.activity

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import com.asyabab.endora.R
import com.asyabab.endora.base.BaseActivity
import com.asyabab.endora.data.models.user.ubahprofil.UbahProfilResponse
import com.asyabab.endora.utils.launchActivityWithNewTask
import com.asyabab.endora.utils.onClick
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_daftar.*
import kotlinx.android.synthetic.main.activity_ubahprofil.*
import kotlinx.android.synthetic.main.activity_ubahprofil.inputEmail
import kotlinx.android.synthetic.main.activity_ubahprofil.inputUsername
import java.util.regex.Matcher
import java.util.regex.Pattern


class UbahProfilActivity : BaseActivity() {
    lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ubahprofil)

        btBack.onClick {
            finish()
        }

        btSimpan.onClick {
            if ((inputUsername.text.toString() == "") || (inputEmail.text.toString()) == "") {
                Toast.makeText(
                    this@UbahProfilActivity,
                    "Isi Data Dengan Lengkap",
                    Toast.LENGTH_SHORT
                ).show()

            } else if (inputUsername.text.toString().length < 6) {
                Toast.makeText(
                    this@UbahProfilActivity,
                    "Minimal Username 6 Karakter",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (!emailValidator(inputEmail.text.toString())) {
                Toast.makeText(
                    this@UbahProfilActivity,
                    "Masukkan Email dengan Benar",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                setUbahProfil(
                    inputUsername.text.toString(),
                    inputEmail.text.toString(),
                    "",
                    repository?.getToken()!!
                )
            }

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

    fun setUbahProfil(username: String, email: String, password: String, auth: String) {
        repository?.setUbahProfil(
            username,
            email,
            password,
            auth,
            object : UbahProfilResponse.UbahProfilResponseCallback {
                override fun onSuccess(ubahProfilResponse: UbahProfilResponse) {
                    Log.d("UbahProfil", "signInsuccess")
                    loading?.dismiss()
                    if (ubahProfilResponse.status == true) {
                        Toast.makeText(this@UbahProfilActivity, "Berhasil", Toast.LENGTH_SHORT)
                            .show()
                        val gson = Gson()
                        val json: String = gson.toJson(ubahProfilResponse.data)
                        repository?.saveProfile(json)
                        launchActivityWithNewTask<RincianProfilActivity>()
                        finish()
                    } else {
                        if (ubahProfilResponse.message == "User Data Failed to Update!") {
                            Toast.makeText(
                                this@UbahProfilActivity,
                                "Password Salah",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                this@UbahProfilActivity,
                                "Gagal Memperbarui",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                }

                override fun onFailure(message: String) {
                    Log.d("Login", message)
                    loading?.dismiss()
                    Toast.makeText(applicationContext, "Email Sudah Terpakai", Toast.LENGTH_LONG)
                        .show()
                }

            })
    }
}