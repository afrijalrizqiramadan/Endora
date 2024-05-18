package com.asyabab.endora.ui.activity


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.asyabab.endora.R
import com.asyabab.endora.base.BaseActivity
import com.asyabab.endora.data.models.user.changephoto.ChangePhotoResponse
import com.asyabab.endora.data.models.general.GeneralResponse
import com.asyabab.endora.data.models.login.Data
import com.asyabab.endora.utils.ImagePickerActivity
import com.asyabab.endora.utils.launchActivity
import com.asyabab.endora.utils.loadImageFromResources
import com.asyabab.endora.utils.onClick
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_rincianprofil.*
import java.io.File
import java.io.IOException


class RincianProfilActivity : BaseActivity() {
    val REQUEST_IMAGE = 100
    private val TAG: String =RincianProfilActivity::class.java.simpleName
    var dataProfile = Data()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rincianprofil)

        btBack.onClick {
            finish()
        }
        btUbahKataSandi.onClick {
            launchActivity<UbahKataSandi2Activity>()
        }
        btUbahEmail.onClick {
            launchActivity<UbahProfilActivity>()
        }

        iconProfil.onClick {
showOption(context)
        }
        val gson = Gson()

        dataProfile = gson.fromJson(repository?.getProfile(), Data::class.java)
        tvUsername.text = dataProfile.username
        tvEmail.text = dataProfile.email
        dataProfile.image?.let { iconProfil.loadImageFromResources(this, it) }

        ImagePickerActivity.clearCache(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                val uri = data?.getParcelableExtra<Uri>("path")
                try {
                    // You can update this bitmap to your server
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                    repository?.getToken()?.let {
                        if (uri != null) {
                            setGambarProfil(File(uri.path), it)
                        }
                    }
                    // loading profile image from local cache
                    loadProfile(uri.toString())
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }



    fun setGambarProfil(image: File, auth: String) {
        repository?.setGambarProfil(
            image,
            auth,
            object : ChangePhotoResponse.ChangePhotoResponseCallback {
                override fun onSuccess(changePhotoResponse: ChangePhotoResponse) {
                    Log.d("Ubah", "signInsuccess")
                    loading?.dismiss()
                    if (changePhotoResponse.status == true) {
                        tvNotifikasi.visibility=View.VISIBLE
                        tvNotifikasi.text = "Berhasil Dirubah"
                        val gson = Gson()
                        val json: String = gson.toJson(changePhotoResponse.data)
                        repository?.saveProfile(json)
                        Log.d("Losgin", json)
                    } else {
                        Toast.makeText(this@RincianProfilActivity, "Gagal", Toast.LENGTH_SHORT)
                            .show()

                    }
                }

                override fun onFailure(message: String) {
                    Log.d("Login", message)
                    loading?.dismiss()
                    Toast.makeText(applicationContext, "Server Sedang Error", Toast.LENGTH_LONG).show()
                }

            })
    }

    private fun onProfileImageClick() {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        showImagePickerOptions()
                    }
                    if (report.isAnyPermissionPermanentlyDenied) {
                        showSettingsDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    private fun showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(
            this,
            object : ImagePickerActivity.PickerOptionListener {
                override fun onTakeCameraSelected() {
                    launchCameraIntent()
                }

                override fun onChooseGallerySelected() {
                    launchGalleryIntent()
                }
            })
    }

    private fun launchCameraIntent() {
        val intent = Intent(this@RincianProfilActivity, ImagePickerActivity::class.java)
        intent.putExtra(
            ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
            ImagePickerActivity.REQUEST_IMAGE_CAPTURE
        )

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true)
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1)

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000)
        startActivityForResult(intent, REQUEST_IMAGE)
    }

    private fun launchGalleryIntent() {
        val intent = Intent(this@RincianProfilActivity, ImagePickerActivity::class.java)
        intent.putExtra(
            ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
            ImagePickerActivity.REQUEST_GALLERY_IMAGE
        )

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true)
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1)
        startActivityForResult(intent, REQUEST_IMAGE)
    }

    private fun loadProfile(url: String) {
        Log.d(TAG,
            "Image cache path: $url"
        )
        Glide.with(this).load(url)
            .into(iconProfil)
        iconProfil.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent))
    }

    private fun showSettingsDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@RincianProfilActivity)
        builder.setTitle("Izin Perangkat")
        builder.setMessage("Izin Perangkat")
        builder.setPositiveButton("Pergi ke Pengaturan") { dialog, which ->
            dialog.cancel()
            openSettings()
        }
        builder.setNegativeButton(
            getString(android.R.string.cancel)
        ) { dialog, which -> dialog.cancel() }
        builder.show()
    }

    // navigating user to app settings
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

    fun showOption(
        context: Context
    ) {
        // setup the alert builder
        val builder =
            AlertDialog.Builder(context)
        builder.setTitle("Pilihan")

        // add a list
        val animals = arrayOf("Ganti","Hapus"
        )
        builder.setItems(
            animals
        ) { dialog: DialogInterface?, which: Int ->
            when (which) {
                0 -> onProfileImageClick()
                1 -> setHapusGambarProfil()
            }
        }

        // create and show the alert dialog
        val dialog = builder.create()
        dialog.show()
    }

    fun setHapusGambarProfil() {
        repository!!.setHapusGambarProfil(
            repository?.getToken()!!,
            object : GeneralResponse.GeneralResponseCallback {
                override fun onSuccess(generalResponse: GeneralResponse) {
                    if (generalResponse.status == true) {
                        Toast.makeText(applicationContext, "Berhasil Dihapus", Toast.LENGTH_LONG).show()

                    }
                }

                override fun onFailure(message: String) {
                    Toast.makeText(applicationContext, "Server Sedang Error", Toast.LENGTH_LONG).show()
                }

            })
    }
}