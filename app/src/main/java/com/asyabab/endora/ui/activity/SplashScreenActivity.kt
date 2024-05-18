package com.asyabab.endora.ui.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.asyabab.endora.R
import com.asyabab.endora.base.BaseActivity
import com.asyabab.endora.utils.RequestPermission
import com.asyabab.endora.utils.getAppColor
import com.asyabab.endora.utils.launchActivity
import com.asyabab.endora.utils.lightStatusBar
import com.google.firebase.auth.FirebaseUser


class SplashScreenActivity : BaseActivity() {
    lateinit var handler: Handler
    private lateinit var smsAndStoragePermissionHandler: RequestPermission

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        lightStatusBar(getAppColor(R.color.colorPrimary))
        smsAndStoragePermissionHandler = RequestPermission(this@SplashScreenActivity,
            permissions = setOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            listener = object : RequestPermission.Listener {
                override fun onComplete(
                    grantedPermissions: Set<String>,
                    deniedPermissions: Set<String>
                ) {
                    if (!TextUtils.isEmpty(repository?.getToken())) {
                        launchActivity<BerandaActivity>()
                        finish()

//                val currentUser = mAuth?.currentUser
//                updateUI(currentUser)
                    } else {
                        launchActivity<LoginActivity>()
//                repository?.saveToken("Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiY2FiZGY3Mzk5ZjNlMjhkZDBjZTc4MTI2MTNlZWYzZDA2MjNlYjZhNGNjZDg5MGJlM2UyYzM2Nzc1MmVkMDNkYmFmMjg2M2I5OGYzMDNkMzEiLCJpYXQiOjE2MDUwODY4ODYsIm5iZiI6MTYwNTA4Njg4NiwiZXhwIjoxNjM2NjIyODg2LCJzdWIiOiIxMiIsInNjb3BlcyI6WyJjdXN0b21lciJdfQ.n7fyJVRJnQnMDbWwSCwD5JL035EUnRtKJFUyeMHaAPg44kfIE2lNdctU60X5sE97svltndRgBTnaNq33MgvPiydc4AGPllvIBkfp5mu-0D8wN3DdNW_dnxEbrXzhSBUTPqjWcJXAe4zr-oE14tsrkfYl7BumwWwEqOuQhPNGaNNr45vZ_sznaSQ4Ntpn3ecuokfXjitmat6G8BpAxnawkfbxT_yUJGTdiA6nE938LJMJQn88v324CcsoZBBNOXOUb0_X71cK65QvV83as7j4qEc6dJ1y14SOKwHS3vk5nmkVM6IX8JgDWL2DThUcndFMPrGAsvRD6J7eBhLv-ouZ2zUztPThM2OUmrD8doKZgZFwlEQ9ZvSpxKSC4ChWv89-BB0rGr-M73MFpFdGHeGSzMZuVQ7reWx7MYW_djG0UTDwMerAgmV040h9JTxE95CJtRNvUYD0zl4UDwdBb--gX9e-nwHppdQiKBrZLtcB-4DGLntXl0ybXg7YuCHdRdnAnp6Ix2NoMyAa0LFSR9SFAO4NL8_UxV8vw1IcMhmrAAS5QdcfXXIlmuJOjxFb7CvEDoyX1qRrejd7RurLQDRgiI-G7pXaiz-pSWamj4YCgsUEjhhyCEYsZ21xZG7YVMmyup3J5KJe0SQ36PJbzhShNYTlOV2nJ7gzfPHBTZyrBRo")

                        finish()

                    }
                }

                override fun onShowPermissionRationale(permissions: Set<String>): Boolean {
                    AlertDialog.Builder(this@SplashScreenActivity, R.style.AlertDialogTheme)
                        .setMessage("Aplikasi Meminta Izin")
                        .setPositiveButton("OK") { _, _ ->
                            smsAndStoragePermissionHandler.retryRequestDeniedPermission()
                        }
                        .setNegativeButton("Cancel") { dialog, _ ->
                            smsAndStoragePermissionHandler.cancel()
                            dialog.dismiss()
                        }
                        .show()



                    return true // don't want to show any rationale, just return false here
                }

                override fun onShowSettingRationale(permissions: Set<String>): Boolean {
                    AlertDialog.Builder(this@SplashScreenActivity, R.style.AlertDialogTheme)
                        .setMessage("Masuk ke pengaturan lalu pilih izin aplikasi")
                        .setPositiveButton("Settings") { _, _ ->
                            smsAndStoragePermissionHandler.requestPermissionInSetting()
                        }
                        .setNegativeButton("Cancel") { dialog, _ ->
                            smsAndStoragePermissionHandler.cancel()
                            dialog.cancel()
                        }
                        .show()
                    return true
                }
            })


        handler = Handler()
        handler.postDelayed({
            handleRequestPermission()


        }, 2000)
    }

    fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            Log.e("hasil", user.displayName.toString())
            launchActivity<BerandaActivity>()
            finish()

        } else {
            launchActivity<LoginActivity>()
            finish()
        }
    }

    private fun handleRequestPermission() {
        smsAndStoragePermissionHandler.requestPermission()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        smsAndStoragePermissionHandler.onRequestPermissionsResult(
            requestCode, permissions,
            grantResults
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        smsAndStoragePermissionHandler.onActivityResult(requestCode)
    }
//    fun login(email: String, uid: String) {
//        repository!!.login(email, uid, object : LoginResponse.LoginResponseCallback {
//            override fun onSuccess(loginResponse: LoginResponse) {
//                if (loginResponse.status == true) {
//                    val token = loginResponse.data!!.token
//                    repository!!.saveToken(token)
//                    Log.e("hasil", repository?.getToken().toString())
//                    if (loginResponse.data!!.lengkap.equals("YA")) {
//                        launchActivity<DPDashboardActivity>()
//                        finish()
//                    } else {
//                        launchActivity<DPSignUpStepsActivity> { }
//                        finish()
//                    }
//                } else {
//                    launchActivity<DPWalkThroughActivity>()
//                    finish()
//                }
//            }
//
//            override fun onFailure(message: String) {
//                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
//                launchActivity<DPWalkThroughActivity>()
//                finish()
//            }
//
//        })
//    }

}