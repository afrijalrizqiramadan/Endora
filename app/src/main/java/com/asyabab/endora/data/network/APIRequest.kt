package com.asyabab.endora.data.network

import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.asyabab.endora.data.models.billboard.detailbillboard.GetDetailBillboard
import com.asyabab.endora.data.models.billboard.jelajah.JelajahResponse
import com.asyabab.endora.data.models.brand.detailbrand.DetailBrandResponse
import com.asyabab.endora.data.models.brand.getbrand.GetBrandResponse
import com.asyabab.endora.data.models.cart.addkeranjang.SetKeranjangResponse
import com.asyabab.endora.data.models.cart.keranjang.KeranjangResponse
import com.asyabab.endora.data.models.checkout.CheckoutResponse
import com.asyabab.endora.data.models.courier.CourierResponse
import com.asyabab.endora.data.models.favorit.Favoritresponse
import com.asyabab.endora.data.models.general.GeneralResponse
import com.asyabab.endora.data.models.home.HomeResponse
import com.asyabab.endora.data.models.item.detailitem.DetailItemResponse
import com.asyabab.endora.data.models.item.search.SearchResponse
import com.asyabab.endora.data.models.kategori.detailkategori.DetailKategoriResponse
import com.asyabab.endora.data.models.kategori.listkategori.ListKategoriResponse
import com.asyabab.endora.data.models.login.LoginResponse
import com.asyabab.endora.data.models.login.LoginResponse2
import com.asyabab.endora.data.models.lokasi.getlocation.GetLocationResponse
import com.asyabab.endora.data.models.lokasi.getmain.GetMainResponse
import com.asyabab.endora.data.models.lokasi.tambahalamat.TambahAlamatResponse
import com.asyabab.endora.data.models.offer.detailoffer.DetailOfferResponse
import com.asyabab.endora.data.models.offer.getoffer.GetOfferResponse
import com.asyabab.endora.data.models.ongkir.OngkirResponse
import com.asyabab.endora.data.models.payment.getdetailpembelian.GetDetailPembelianResponse
import com.asyabab.endora.data.models.payment.getpembelian.GetPembelianResponse
import com.asyabab.endora.data.models.payment.setsubmission.SetSubmissionResponse
import com.asyabab.endora.data.models.rajaongkir.getkecamatan.GetKecamatanResponse
import com.asyabab.endora.data.models.rajaongkir.getkota.GetKotaResponse
import com.asyabab.endora.data.models.rajaongkir.getprovinsi.GetProvinsiResponse
import com.asyabab.endora.data.models.register.RegisterResponse
import com.asyabab.endora.data.models.register.RegisterResponse2
import com.asyabab.endora.data.models.user.changephoto.ChangePhotoResponse
import com.asyabab.endora.data.models.user.getsearch.GetSearchResponse
import com.asyabab.endora.data.models.user.ubahpassword.UbahPasswordResponse
import com.asyabab.endora.data.models.user.ubahprofil.UbahProfilResponse
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import org.json.JSONObject
import java.io.File


/**
 * Created by Silva on 05/26/2020.
 */

class APIRequest {

    private val BASE_URL = "https://dev.endoracare.com/api/"

    private val TAG = "hasil"
    private val gson = GsonBuilder().create()
    private var requestUrl = ""

    fun login(
        username: String,
        password: String,
        device_id: String,
        token_fcm: String,
        type: String,
        token_scm: String,
        callback: LoginResponse.LoginResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/login"

        AndroidNetworking.post(requestUrl)
            .addBodyParameter("username", username)
            .addBodyParameter("password", password)
            .addBodyParameter("device_id", device_id)
            .addBodyParameter("token_fcm", token_fcm)
            .addBodyParameter("type", type)
            .addBodyParameter("token_socmed", token_scm)

            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")
                    try {
                        val response1 =
                            gson.fromJson(response.toString(), LoginResponse::class.java)
                        if (response1 != null) {
                            callback.onSuccess(response1)
                        } else {
                            callback.onFailure("Cannot get Object")
                        }
                    } catch (e: JsonParseException) {
                        callback.onFailure("Kondisi2")

                    }

                }

                override fun onError(anError: ANError) {
                    callback.onFailure("Kondisi2")
                    Log.e(TAG, "respon login ${anError.errorBody}")
                }
            })
    }

    fun login2(
        username: String,
        password: String,
        device_id: String,
        token_fcm: String,
        type: String,
        token_scm: String,
        callback: LoginResponse2.LoginResponse2Callback
    ) {
        requestUrl = BASE_URL + "customer/login"

        AndroidNetworking.post(requestUrl)
            .addBodyParameter("username", username)
            .addBodyParameter("password", password)
            .addBodyParameter("device_id", device_id)
            .addBodyParameter("token_fcm", token_fcm)
            .addBodyParameter("type", type)
            .addBodyParameter("token_socmed", token_scm)

            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 = gson.fromJson(response.toString(), LoginResponse2::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Akun Username Belum Terdaftar!")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }

    fun register(
        username: String,
        email: String,
        password: String,
        token_fcm: String,
        device_id: String,
        token_scm: String,
        type: String,
        callback: RegisterResponse.RegisterResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/register"

        AndroidNetworking.post(requestUrl)
            .addBodyParameter("username", username)
            .addBodyParameter("email", email)
            .addBodyParameter("password", password)
            .addBodyParameter("token_fcm", token_fcm)
            .addBodyParameter("device_id", device_id)
            .addBodyParameter("token_socmed", token_scm)
            .addBodyParameter("type", type)

            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")
//                    try {
                    val response1 =
                        gson.fromJson(response.toString(), RegisterResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
//                    } catch (e: JsonParseException) {
//                        callback.onFailure("Kondisi2")
//
//                    }

                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }

    fun register2(
        username: String,
        email: String,
        password: String,
        token_fcm: String,
        device_id: String,
        token_scm: String,
        type: String,
        callback: RegisterResponse2.RegisterResponse2Callback
    ) {
        requestUrl = BASE_URL + "customer/register"

        AndroidNetworking.post(requestUrl)
            .addBodyParameter("username", username)
            .addBodyParameter("email", email)
            .addBodyParameter("password", password)
            .addBodyParameter("token_fcm", token_fcm)
            .addBodyParameter("device_id", device_id)
            .addBodyParameter("token_socmed", token_scm)
            .addBodyParameter("type", type)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")
                    try {
                        val response1 =
                            gson.fromJson(response.toString(), RegisterResponse2::class.java)
                        if (response1 != null) {
                            callback.onSuccess(response1)
                        } else {
                            callback.onFailure("Cannot get Object")
                        }
                    } catch (e: JsonParseException) {
                        callback.onFailure("Cannot get Object")


                    }

                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }

    fun setFavorit(
        itemId: String,
        auth: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/favourite/state_favourite"

        AndroidNetworking.post(requestUrl)
            .addBodyParameter("item_id", itemId)
            .addHeaders("Authorization", auth)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 = gson.fromJson(response.toString(), GeneralResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }

    fun setUlasan(
        itemId: String,
        rating: String,
        review: String,
        auth: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/payment/review"

        AndroidNetworking.post(requestUrl)
            .addBodyParameter("payment_id", itemId)
            .addBodyParameter("rating", rating)
            .addBodyParameter("review", review)
            .addHeaders("Authorization", auth)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 = gson.fromJson(response.toString(), GeneralResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }

    fun setGambarProfil(
        image: File,
        auth: String,
        callback: ChangePhotoResponse.ChangePhotoResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/user/change_photo"
        AndroidNetworking.upload(requestUrl)
            .addMultipartFile("image", image) //.addMultipartParameter("key","value")
            .addHeaders("Authorization", auth)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 =
                        gson.fromJson(response.toString(), ChangePhotoResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
//        AndroidNetworking.upload(requestUrl)
//            .addBodyParameter("image", image)
//            .addHeaders("Authorization", auth)
//            .setPriority(Priority.MEDIUM)
//            .build()
//            .getAsJSONObject(object : JSONObjectRequestListener {
//                override fun onResponse(response: JSONObject) {
//                    Log.e(TAG, "onResponse: $response")
//
//                    val response1 = gson.fromJson(response.toString(), GeneralResponse::class.java)
//                    if (response1 != null) {
//                        callback.onSuccess(response1)
//                    } else {
//                        callback.onFailure("Cannot get Object")
//                    }
//                }
//
//                override fun onError(anError: ANError) {
//                    callback.onFailure(anError.errorBody)
//                }
//            })
    }

    fun setUbahPassword(
        password: String,
        newpassword: String,
        newpassword2: String,
        auth: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/user/change_password"

        AndroidNetworking.post(requestUrl)
            .addBodyParameter("password", password)
            .addBodyParameter("new_password", newpassword)
            .addBodyParameter("new_password_confirmation", newpassword2)
            .addHeaders("Authorization", auth)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    try {
                        Log.e(TAG, "onResponse: $response")

                        val response1 =
                            gson.fromJson(response.toString(), GeneralResponse::class.java)
                        if (response1 != null) {
                            callback.onSuccess(response1)
                        } else {
                            callback.onFailure("Cannot get Object")
                        }
                    } catch (e: JsonParseException) {
                        callback.onFailure("Kondisi2")

                    }

                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }

    fun setUbahPassword2(
        password: String,
        newpassword: String,
        newpassword2: String,
        auth: String,
        callback: UbahPasswordResponse.UbahPasswordResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/user/change_password"

        AndroidNetworking.post(requestUrl)
            .addBodyParameter("password", password)
            .addBodyParameter("new_password", newpassword)
            .addBodyParameter("new_password_confirmation", newpassword2)
            .addHeaders("Authorization", auth)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 =
                        gson.fromJson(response.toString(), UbahPasswordResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Akun Username Belum Terdaftar!")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }

    fun setUpdateAlamat(
        id: String,
        kategori: String,
        receiver_name: String,
        phone: String,
        address: String,
        district: String,
        districtid: String,
        city: String,
        province: String,
        postal_code: String,
        latitude: String,
        longitude: String,
        auth: String,
        callback: TambahAlamatResponse.TambahAlamatResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/location/update_location"

        AndroidNetworking.post(requestUrl)
            .addBodyParameter("category", kategori)
            .addBodyParameter("receiver_name", receiver_name)
            .addBodyParameter("phone", phone)
            .addBodyParameter("address", address)
            .addBodyParameter("district", district)
            .addBodyParameter("subdistrict_id", districtid)
            .addBodyParameter("city", city)
            .addBodyParameter("province", province)
            .addBodyParameter("postal_code", postal_code)
            .addBodyParameter("latitude", latitude)
            .addBodyParameter("longitude", longitude)
            .addBodyParameter("id", id)
            .addHeaders("Authorization", auth)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    try {
                        Log.e(TAG, "onResponse: $response")

                        val response1 =
                            gson.fromJson(response.toString(), TambahAlamatResponse::class.java)
                        if (response1 != null) {
                            callback.onSuccess(response1)
                        } else {
                            callback.onFailure("Cannot get Object")
                        }
                    } catch (exception: IllegalStateException) {
                        Log.e(TAG, "w: $response")

                    } catch (exception: JsonSyntaxException) {
                        Log.e(TAG, "a: $response")
                    }

                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }

    fun setTambahAlamat(
        kategori: String,
        receiver_name: String,
        phone: String,
        address: String,
        district: String,
        districtid: String,
        city: String,
        province: String,
        postal_code: String,
        latitude: String,
        longitude: String,
        auth: String,
        callback: TambahAlamatResponse.TambahAlamatResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/location/add_location"

        AndroidNetworking.post(requestUrl)
            .addBodyParameter("category", kategori)
            .addBodyParameter("receiver_name", receiver_name)
            .addBodyParameter("phone", phone)
            .addBodyParameter("address", address)
            .addBodyParameter("district", district)
            .addBodyParameter("subdistrict_id", districtid)
            .addBodyParameter("city", city)
            .addBodyParameter("province", province)
            .addBodyParameter("postal_code", postal_code)
            .addBodyParameter("latitude", latitude)
            .addBodyParameter("longitude", longitude)
            .addHeaders("Authorization", auth)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    try {
                        Log.e(TAG, "onResponse: $response")

                        val response1 =
                            gson.fromJson(response.toString(), TambahAlamatResponse::class.java)
                        if (response1 != null) {
                            callback.onSuccess(response1)
                        } else {
                            callback.onFailure("Cannot get Object")
                        }
                    } catch (exception: IllegalStateException) {
                        Log.e(TAG, "w: $response")

                    } catch (exception: JsonSyntaxException) {
                        Log.e(TAG, "a: $response")
                    }

                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }

    fun setUbahProfil(
        username: String,
        email: String,
        password: String,
        auth: String,
        callback: UbahProfilResponse.UbahProfilResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/user/change_profile"

        AndroidNetworking.post(requestUrl)
            .addBodyParameter("username", username)
            .addBodyParameter("email", email)
            .addBodyParameter("password", password)
            .addHeaders("Authorization", auth)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 =
                        gson.fromJson(response.toString(), UbahProfilResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }


    fun setHapusAkun(
        auth: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/user/delete_account"

        AndroidNetworking.get(requestUrl)
            .addHeaders("Authorization", auth)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 = gson.fromJson(response.toString(), GeneralResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }

    fun setLupaPassword(
        email: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/forgot_password"

        AndroidNetworking.post(requestUrl)
            .addBodyParameter("email", email)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 = gson.fromJson(response.toString(), GeneralResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }

    fun setHapusGambarProfil(
        auth: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/user/remove_photo"

        AndroidNetworking.post(requestUrl)
            .addHeaders("Authorization", auth)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 = gson.fromJson(response.toString(), GeneralResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }

    fun setHapusPencarian(
        auth: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/user/clear_search"

        AndroidNetworking.get(requestUrl)
            .addHeaders("Authorization", auth)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 = gson.fromJson(response.toString(), GeneralResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }


    fun setMain(
        id: String,
        auth: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/location/is_main"

        AndroidNetworking.post(requestUrl)
            .addBodyParameter("id", id)
            .addHeaders("Authorization", auth)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 = gson.fromJson(response.toString(), GeneralResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }

    fun lupaSandi(
        email: String,
        auth: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/user/forgot_password"

        AndroidNetworking.post(requestUrl)
            .addBodyParameter("email", email)
            .addHeaders("Authorization", auth)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 = gson.fromJson(response.toString(), GeneralResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }

    fun setTambahKeranjang(
        id: String,
        qty: String,
        varian: String,
        auth: String,
        callback: SetKeranjangResponse.SetKeranjangResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/cart/add_item"

        AndroidNetworking.post(requestUrl)
            .addBodyParameter("item_id", id)
            .addBodyParameter("qty", qty)
            .addBodyParameter("variant", varian)
            .addHeaders("Authorization", auth)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 =
                        gson.fromJson(response.toString(), SetKeranjangResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }

    fun setConfirmation(
        id: String,
        latitude: String,
        longitude: String,
//        image: File,
        auth: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/payment/confirmation_item"

        AndroidNetworking.upload(requestUrl)
            .addMultipartParameter("payment_id", id)
//            .addMultipartFile("image", image) //.addMultipartParameter("key","value")
            .addMultipartParameter("latitude", latitude)
            .addMultipartParameter("longitude", longitude)
            .addHeaders("Authorization", auth)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 = gson.fromJson(response.toString(), GeneralResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }

    fun setSubmission(
        id: String,
        auth: String,
        callback: SetSubmissionResponse.SetSubmissionResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/payment/set_submission"

        AndroidNetworking.post(requestUrl)
            .addBodyParameter("id", id)
            .addHeaders("Authorization", auth)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 =
                        gson.fromJson(response.toString(), SetSubmissionResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }

    fun setCanceled(
        id: String,
        reason: String,
        auth: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/payment/canceled"

        AndroidNetworking.post(requestUrl)
            .addBodyParameter("id", id)
            .addBodyParameter("reason", reason)
            .addHeaders("Authorization", auth)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 = gson.fromJson(response.toString(), GeneralResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }

    fun setUpdateJumlah(
        id: String,
        item_id: String,
        qty: String,
        auth: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/cart/update_qty"

        AndroidNetworking.post(requestUrl)
            .addBodyParameter("id", id)
            .addBodyParameter("item_id", item_id)
            .addBodyParameter("qty", qty)
            .addHeaders("Authorization", auth)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 = gson.fromJson(response.toString(), GeneralResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }

    fun setSearch(
        search: String,
        order: String,
        pricemin: String,
        pricemax: String,
        auth: String,
        callback: SearchResponse.SearchResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/item/search"

        AndroidNetworking.get(requestUrl)
            .addQueryParameter("search", search)
            .addQueryParameter("order", order)
            .addQueryParameter("filter_price_min", pricemin)
            .addQueryParameter("filter_price_max", pricemax)
            .addHeaders("Authorization", auth)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 = gson.fromJson(response.toString(), SearchResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }

    fun hapusLocation(
        id: String,
        auth: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/location/delete_location"

        AndroidNetworking.get(requestUrl)
            .addQueryParameter("id", id)
            .addHeaders("Authorization", auth)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 = gson.fromJson(response.toString(), GeneralResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }


    fun getFavorit(
        auth: String,
        callback: Favoritresponse.FavoritResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/favourite/get_list"

        AndroidNetworking.get(requestUrl)
            .addHeaders(
                "Authorization", auth
            )
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 = gson.fromJson(response.toString(), Favoritresponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }

    fun getProvinsi(
        auth: String,
        callback: GetProvinsiResponse.GetProvinsiResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/rajaongkir/get_provinces"

        AndroidNetworking.get(requestUrl)
            .addHeaders(
                "Authorization", auth
            )
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 =
                        gson.fromJson(response.toString(), GetProvinsiResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }

    fun getKota(
        id: String,
        auth: String,
        callback: GetKotaResponse.GetKotaResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/rajaongkir/get_cities_by_province"

        AndroidNetworking.get(requestUrl)

            .addQueryParameter("id", id)
            .addHeaders(
                "Authorization", auth
            )

            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 = gson.fromJson(response.toString(), GetKotaResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }

    fun getKecamatan(
        id: String,
        auth: String,
        callback: GetKecamatanResponse.GetKecamatanResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/rajaongkir/get_subdistrict_by_city"

        AndroidNetworking.get(requestUrl)
            .addQueryParameter("city_id", id)
            .addHeaders(
                "Authorization", auth
            )
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 =
                        gson.fromJson(response.toString(), GetKecamatanResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }

    fun getPembelian(
        auth: String,
        callback: GetPembelianResponse.GetPembelianResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/payment/get_list"

        AndroidNetworking.get(requestUrl)
            .addHeaders(
                "Authorization", auth
            )
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")
                    if (response != null) {
                        val response1 =
                            gson.fromJson(response.toString(), GetPembelianResponse::class.java)
                        if (response1 != null) {
                            callback.onSuccess(response1)
                        } else {
                            callback.onFailure("Cannot get Object")

                        }
                    } else {
                        callback.onFailure("Cannot get Object")
                        Log.e(TAG, "onResrr: $response")

                    }

                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }

    fun getLocation(
        auth: String,
        callback: GetLocationResponse.GetLocationResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/location/get_list"

        AndroidNetworking.get(requestUrl)
            .addHeaders(
                "Authorization", auth
            )
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 =
                        gson.fromJson(response.toString(), GetLocationResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }

    fun getMain(
        auth: String,
        callback: GetMainResponse.GetMainResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/location/get_main"

        AndroidNetworking.get(requestUrl)
            .addHeaders(
                "Authorization", auth
            )
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 =
                        gson.fromJson(response.toString(), GetMainResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }
    fun getDetailPembelian(
        id: String,
        auth: String,
        callback: GetDetailPembelianResponse.GetDetailPembelianResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/payment/detail"

        AndroidNetworking.get(requestUrl)
            .addQueryParameter("id", id)
            .addHeaders(
                "Authorization", auth
            )
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 =
                        gson.fromJson(response.toString(), GetDetailPembelianResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }

    fun getDetailBillboard(
        id: String,
        auth: String,
        callback: GetDetailBillboard.GetDetailBillboardResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/billboard/detail"

        AndroidNetworking.get(requestUrl)
            .addQueryParameter("id", id)
            .addHeaders(
                "Authorization", auth
            )
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 =
                        gson.fromJson(response.toString(), GetDetailBillboard::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }

    fun getOffer(
        auth: String,
        callback: GetOfferResponse.GetOfferCallback
    ) {
        requestUrl = BASE_URL + "customer/offer/get_list"

        AndroidNetworking.get(requestUrl)
            .addHeaders(
                "Authorization", auth
            )
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 =
                        gson.fromJson(response.toString(), GetOfferResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                    Log.e(TAG, "onError: $anError.errorBody")

                }
            })
    }

    fun getDetailOffer(
        id: String,
        auth: String,
        callback: DetailOfferResponse.DetailOfferResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/offer/detail"

        AndroidNetworking.get(requestUrl)
            .addQueryParameter("id", id)
            .addHeaders(
                "Authorization", auth
            )
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 =
                        gson.fromJson(response.toString(), DetailOfferResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }

    fun getKeranjang(
        auth: String,
        callback: KeranjangResponse.KeranjangResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/cart/get_list"

        AndroidNetworking.get(requestUrl)
            .addHeaders(
                "Authorization", auth
            )
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 =
                        gson.fromJson(response.toString(), KeranjangResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                    Log.e(TAG, "onError: $anError.errorBody")

                }
            })
    }

    fun deleteKeranjang(
        itemId: String,
        auth: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/cart/delete_item"

        AndroidNetworking.get(requestUrl)
            .addQueryParameter("id", itemId)
            .addHeaders("Authorization", auth)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 = gson.fromJson(response.toString(), GeneralResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }

    fun getSearch(
        auth: String,
        callback: GetSearchResponse.GetSearchResponeCallback
    ) {
        requestUrl = BASE_URL + "customer/user/get_search"

        AndroidNetworking.get(requestUrl)
            .addHeaders(
                "Authorization", auth
            )
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 =
                        gson.fromJson(response.toString(), GetSearchResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)

                }
            })
    }

    fun getBrand(
        auth: String,
        callback: GetBrandResponse.GetBrandResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/brand/get_list"

        AndroidNetworking.get(requestUrl)
            .addHeaders(
                "Authorization", auth
            )
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 =
                        gson.fromJson(response.toString(), GetBrandResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                    Log.e(TAG, "onError: $anError.errorBody")

                }
            })
    }

    fun getListKategori(
        auth: String,
        callback: ListKategoriResponse.ListKategoriResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/category/get_list"

        AndroidNetworking.get(requestUrl)
            .addHeaders(
                "Authorization", auth
            )
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 =
                        gson.fromJson(response.toString(), ListKategoriResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                    Log.e(TAG, "onError: $anError.errorBody")

                }
            })
    }

    fun getDetailKategori(
        id: String,
        auth: String,
        callback: DetailKategoriResponse.DetailKategoriResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/category/detail"

        AndroidNetworking.get(requestUrl)
            .addQueryParameter("id", id)
            .addHeaders(
                "Authorization", auth
            )
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 =
                        gson.fromJson(response.toString(), DetailKategoriResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)

                }
            })
    }

    fun getDetailBrand(
        id: String,
        auth: String,
        callback: DetailBrandResponse.DetailBrandResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/brand/detail"

        AndroidNetworking.get(requestUrl)
            .addQueryParameter("id", id)
            .addHeaders(
                "Authorization", auth
            )
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 =
                        gson.fromJson(response.toString(), DetailBrandResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)

                }
            })
    }

    fun getDetailItem(
        id: String,
        auth: String,
        callback: DetailItemResponse.DetailItemResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/item/detail"

        AndroidNetworking.get(requestUrl)
            .addQueryParameter("id", id)
            .addHeaders(
                "Authorization", auth
            )
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 =
                        gson.fromJson(response.toString(), DetailItemResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)

                }
            })
    }

    fun getRincianPesanan(
        id: String,
        auth: String,
        callback: GetDetailPembelianResponse.GetDetailPembelianResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/payment/detail"

        AndroidNetworking.get(requestUrl)
            .addQueryParameter("id", id)
            .addHeaders(
                "Authorization", auth
            )
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 =
                        gson.fromJson(response.toString(), GetDetailPembelianResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)

                }
            })
    }

    fun getJelajah(
        auth: String,
        callback: JelajahResponse.JelajahResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/billboard/get_list"

        AndroidNetworking.get(requestUrl)
            .addHeaders(
                "Authorization", auth
            )
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 =
                        gson.fromJson(response.toString(), JelajahResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    Log.e(TAG, "onerrResponse")

                    callback.onFailure(anError.errorBody)

                }
            })
    }

    fun getHome(
        auth: String,
        callback: HomeResponse.HomeResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/home"

        AndroidNetworking.get(requestUrl)
            .addHeaders(
                "Authorization", auth
            )
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 =
                        gson.fromJson(response.toString(), HomeResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                    Log.e(TAG, "onError: ${anError.errorDetail}")

                }
            })
    }

    fun getCourier(
        auth: String,
        callback: CourierResponse.CourierResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/rajaongkir/get_couriers"

        AndroidNetworking.get(requestUrl)
            .addHeaders("Authorization", auth)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e("$TAG 11", "onResponse: $response")

                    val response1 =
                        gson.fromJson(response.toString(), CourierResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                    Log.e(TAG, "onError: $anError.errorBody")

                }
            })
    }


    fun cekOngkir(
        auth: String,
        origin: String,
        destination: String,
        weight: String,
        courier: String,
        service: String,
        callback: OngkirResponse.OngkirResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/rajaongkir/check_ongkir"

        AndroidNetworking.post(requestUrl)
            .addHeaders("Authorization", auth)
            .addBodyParameter("origin", origin)
            .addBodyParameter("destination", destination)
            .addBodyParameter("weight", weight)
            .addBodyParameter("courier", courier)
            .addBodyParameter("service", service)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e("$TAG 11", "onResponse: $response")

                    val response1 =
                        gson.fromJson(response.toString(), OngkirResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                    Log.e(TAG, "onError: $anError.errorBody")

                }
            })
    }

    fun checkout(
        auth: String, jsonObject: JSONObject,
        callback: CheckoutResponse.CheckoutResponseCallback
    ) {
        requestUrl = BASE_URL + "customer/payment/checkout"

        Log.e("hasil", jsonObject.toString())

        AndroidNetworking.post(requestUrl)
            .addHeaders("Authorization", auth)
            .addJSONObjectBody(jsonObject)
            .setTag("halo")
            .setPriority(Priority.MEDIUM)
            .setTag("test")
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e("$TAG 11", "onResponse cekout: $response")

                    val response1 =
                        gson.fromJson(response.toString(), CheckoutResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                    Log.e(TAG, "onError: $anError.errorBody")

                }
            })
    }


    fun simulatePayment(
        auth: String, jsonObject: JSONObject,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        requestUrl = "https://api.xendit.co/pool_virtual_accounts/simulate_payment "

        Log.e("hasil", jsonObject.toString())

        AndroidNetworking.post(requestUrl)
            .addHeaders("Authorization", auth)
            .addJSONObjectBody(jsonObject)
            .setTag("halo")
            .setPriority(Priority.MEDIUM)
            .setTag("test")
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e("$TAG 11", "onResponse cekout: $response")

                    val response1 =
                        gson.fromJson(response.toString(), GeneralResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                    Log.e(TAG, "onError: $anError.errorBody")

                }
            })
    }
}

