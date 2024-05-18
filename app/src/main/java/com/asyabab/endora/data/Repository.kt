package com.asyabab.endora.data


import android.content.Context
import com.asyabab.endora.data.database.Database
import com.asyabab.endora.data.local.SharedPrefHelper
import com.asyabab.endora.data.models.billboard.detailbillboard.GetDetailBillboard
import com.asyabab.endora.data.models.billboard.jelajah.JelajahResponse
import com.asyabab.endora.data.models.brand.detailbrand.DetailBrandResponse
import com.asyabab.endora.data.models.brand.getbrand.GetBrandResponse
import com.asyabab.endora.data.models.cart.addkeranjang.SetKeranjangResponse
import com.asyabab.endora.data.models.cart.keranjang.Data
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
import com.asyabab.endora.data.models.user.profile.Profile
import com.asyabab.endora.data.models.user.ubahpassword.UbahPasswordResponse
import com.asyabab.endora.data.models.user.ubahprofil.UbahProfilResponse
import com.asyabab.endora.data.network.APIRequest
import org.json.JSONObject
import java.io.File
import java.util.*

class Repository(private val mContext: Context) {

    private val apiRequest: APIRequest = APIRequest()
    private val database = Database(mContext)

    private val prefs: SharedPrefHelper = SharedPrefHelper(mContext)

    private var mProfile = Profile()
    private var city = ArrayList<String>()
    private var spesialis = ArrayList<String>()

    private var product = ArrayList<Data>()

    fun saveProduct(productItem: ArrayList<Data>) {
        product = productItem
    }

    fun getProduvt(): ArrayList<Data> {
        return product
    }

    fun login(
        username: String,
        password: String,
        device_id: String,
        token_fcm: String,
        type: String,
        token_scm: String,

        callback: LoginResponse.LoginResponseCallback
    ) {
        apiRequest.login(username, password, device_id, token_fcm, type, token_scm, callback)
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
        apiRequest.login2(username, password, device_id, token_fcm, type, token_scm, callback)
    }

    fun setHapusAkun(
        auth: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        apiRequest.setHapusAkun(auth, callback)
    }

    fun setLupaPassword(
        email: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        apiRequest.setLupaPassword(email, callback)
    }

    fun setHapusPencarian(
        auth: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        apiRequest.setHapusPencarian(auth, callback)
    }


    fun getFavorit(auth: String, callback: Favoritresponse.FavoritResponseCallback) {
        apiRequest.getFavorit(auth, callback)
    }

    fun getOffer(auth: String, callback: GetOfferResponse.GetOfferCallback) {
        apiRequest.getOffer(auth, callback)
    }

    fun getDetailPembelian(
        id: String,
        auth: String,
        callback: GetDetailPembelianResponse.GetDetailPembelianResponseCallback
    ) {
        apiRequest.getDetailPembelian(id, auth, callback)
    }

    fun getDetailOffer(
        id: String,
        auth: String,
        callback: DetailOfferResponse.DetailOfferResponseCallback
    ) {
        apiRequest.getDetailOffer(id, auth, callback)
    }

    fun getDetailBillboard(
        id: String,
        auth: String,
        callback: GetDetailBillboard.GetDetailBillboardResponseCallback
    ) {
        apiRequest.getDetailBillboard(id, auth, callback)
    }

    fun getPembelian(auth: String, callback: GetPembelianResponse.GetPembelianResponseCallback) {
        apiRequest.getPembelian(auth, callback)
    }

    fun getSearch(
        auth: String,
        callback: GetSearchResponse.GetSearchResponeCallback
    ) {
        apiRequest.getSearch(auth, callback)
    }


    fun getBrand(auth: String, callback: GetBrandResponse.GetBrandResponseCallback) {
        apiRequest.getBrand(auth, callback)
    }

    fun getDetailBrand(
        id: String,
        auth: String,
        callback: DetailBrandResponse.DetailBrandResponseCallback
    ) {
        apiRequest.getDetailBrand(id, auth, callback)
    }

    fun getDetailKategori(
        id: String,
        auth: String,
        callback: DetailKategoriResponse.DetailKategoriResponseCallback
    ) {
        apiRequest.getDetailKategori(id, auth, callback)
    }

    fun getListKategori(auth: String, callback: ListKategoriResponse.ListKategoriResponseCallback) {
        apiRequest.getListKategori(auth, callback)
    }


    fun getDetailItem(
        id: String,
        auth: String,
        callback: DetailItemResponse.DetailItemResponseCallback
    ) {
        apiRequest.getDetailItem(id, auth, callback)
    }

    fun getJelajah(auth: String, callback: JelajahResponse.JelajahResponseCallback) {
        apiRequest.getJelajah(auth, callback)
    }

    fun getRincianPesanan(
        id: String,
        auth: String,
        callback: GetDetailPembelianResponse.GetDetailPembelianResponseCallback
    ) {
        apiRequest.getRincianPesanan(id, auth, callback)
    }

    fun getKeranjang(
        auth: String,
        callback: KeranjangResponse.KeranjangResponseCallback
    ) {
        apiRequest.getKeranjang(auth, callback)
    }

    fun deleteKeranjang(
        itemId: String,
        auth: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        apiRequest.deleteKeranjang(itemId, auth, callback)
    }

    fun getLocation(
        auth: String,
        callback: GetLocationResponse.GetLocationResponseCallback
    ) {
        apiRequest.getLocation(auth, callback)
    }

    fun getMain(
        auth: String,
        callback: GetMainResponse.GetMainResponseCallback
    ) {
        apiRequest.getMain(auth, callback)
    }

    fun getHome(
        auth: String,
        callback: HomeResponse.HomeResponseCallback
    ) {
        apiRequest.getHome(auth, callback)
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
        apiRequest.register(
            username,
            email,
            password,
            token_fcm,
            device_id,
            token_scm,
            type,
            callback
        )
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
        apiRequest.register2(
            username,
            email,
            password,
            token_fcm,
            device_id,
            token_scm,
            type,
            callback
        )
    }

    fun setGambarProfil(
        image: File,
        auth: String,
        callback: ChangePhotoResponse.ChangePhotoResponseCallback
    ) {
        apiRequest.setGambarProfil(image, auth, callback)
    }

    fun setHapusGambarProfil(
        auth: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        apiRequest.setHapusGambarProfil(auth, callback)
    }

    fun getToken(): String? {
        return prefs.getString(SharedPrefHelper.ACCES_TOKEN)
    }

    fun saveToken(token: String?) {
        prefs.putString(SharedPrefHelper.ACCES_TOKEN, token!!)
    }

    fun getLokasi(): String? {
        return prefs.getString(SharedPrefHelper.ACCES_LOKASI)
    }

    fun saveLokasiKeterangan(token: String?) {
        prefs.putString(SharedPrefHelper.ACCES_LOKASIKETERANGAN, token!!)
    }

    fun getLokasiKeterangan(): String? {
        return prefs.getString(SharedPrefHelper.ACCES_LOKASIKETERANGAN)
    }

    fun saveLokasi(token: String?) {
        prefs.putString(SharedPrefHelper.ACCES_LOKASI, token!!)
    }

    fun savePembelian(data: com.asyabab.endora.data.models.payment.getpembelian.Data) {
        prefs.putPembelian(SharedPrefHelper.ACCES_PEMBELIAN, data!!)
    }

    fun getPembelian(): String? {
        return prefs.getString(SharedPrefHelper.ACCES_PEMBELIAN)
    }

    fun setFirstTimeLaunch(isFirstTime: Boolean) {
        prefs.setFirstTimeLaunch(isFirstTime)
    }

    fun setFavorit(
        itemId: String,
        auth: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        apiRequest.setFavorit(itemId, auth, callback)
    }

    fun setUlasan(
        itemId: String,
        rating: String,
        review: String,
        auth: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        apiRequest.setUlasan(itemId, rating, review, auth, callback)
    }

    fun setMain(
        id: String,
        auth: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        apiRequest.setMain(id, auth, callback)
    }

    fun lupaSandi(
        email: String,
        auth: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        apiRequest.lupaSandi(email, auth, callback)
    }

    fun setTambahKeranjang(
        id: String,
        qty: String,
        varian: String,
        auth: String,
        callback: SetKeranjangResponse.SetKeranjangResponseCallback
    ) {
        apiRequest.setTambahKeranjang(id, qty, varian, auth, callback)
    }

    fun setConfirmation(
        id: String,
        latitude: String,
        longitude: String,
//        image: File,
        auth: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        apiRequest.setConfirmation(id, latitude, longitude, auth, callback)
    }

    fun setCanceled(
        id: String,
        reason: String,
        auth: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        apiRequest.setCanceled(id, reason, auth, callback)
    }

    fun setSubmission(
        id: String,
        auth: String,
        callback: SetSubmissionResponse.SetSubmissionResponseCallback
    ) {
        apiRequest.setSubmission(id, auth, callback)
    }

    fun setUpdateJumlah(
        id: String,
        item_id: String,
        qty: String,
        auth: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        apiRequest.setUpdateJumlah(id, item_id, qty, auth, callback)
    }

    fun hapusLocation(
        id: String,
        auth: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        apiRequest.hapusLocation(id, auth, callback)
    }

    fun setUbahPasswrod(
        password: String,
        newpassword: String,
        newpassword2: String,
        auth: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        apiRequest.setUbahPassword(password, newpassword, newpassword2, auth, callback)
    }

    fun setUbahPasswrod2(
        password: String,
        newpassword: String,
        newpassword2: String,
        auth: String,
        callback: UbahPasswordResponse.UbahPasswordResponseCallback
    ) {
        apiRequest.setUbahPassword2(password, newpassword, newpassword2, auth, callback)
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
        apiRequest.setTambahAlamat(
            kategori,
            receiver_name,
            phone,
            address,
            district,
            districtid,
            city,
            province,
            postal_code,
            latitude,
            longitude,
            auth,
            callback
        )
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
        apiRequest.setUpdateAlamat(
            id,
            kategori,
            receiver_name,
            phone,
            address,
            district,
            districtid,
            city,
            province,
            postal_code,
            latitude,
            longitude,
            auth,
            callback
        )
    }

    fun setSearch(
        search: String,
        order: String,
        pricemin: String,
        pricemax: String,
        auth: String,
        callback: SearchResponse.SearchResponseCallback
    ) {
        apiRequest.setSearch(search, order, pricemin, pricemax, auth, callback)
    }

    fun setUbahProfil(
        username: String,
        email: String,
        password: String,
        auth: String,
        callback: UbahProfilResponse.UbahProfilResponseCallback
    ) {
        apiRequest.setUbahProfil(username, email, password, auth, callback)
    }

    fun saveProfile(token: String?) {
        prefs.putString(SharedPrefHelper.ACCES_PROFILE, token!!)
    }

    fun saveData(param: String, isi: String?) {
        prefs.putString(param, isi!!)
    }

    fun getData(param: String): String? {
        return prefs.getString(param)
    }

    fun getProfile(): String? {
        return prefs.getString(SharedPrefHelper.ACCES_PROFILE)
    }

    fun getKota(id: String, auth: String, callback: GetKotaResponse.GetKotaResponseCallback) {
        apiRequest.getKota(id, auth, callback)
    }

    fun getKecamatan(
        id: String,
        auth: String,
        callback: GetKecamatanResponse.GetKecamatanResponseCallback
    ) {
        apiRequest.getKecamatan(id, auth, callback)
    }

    fun getProvinsi(
        auth: String,
        callback: GetProvinsiResponse.GetProvinsiResponseCallback
    ) {
        apiRequest.getProvinsi(auth, callback)
    }

    fun getCourier(
        auth: String,
        callback: CourierResponse.CourierResponseCallback
    ) {
        apiRequest.getCourier(auth, callback)
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
        apiRequest.cekOngkir(auth, origin, destination, weight, courier, service, callback)
    }

    fun checkout(
        auth: String, jsonObject: JSONObject,
        callback: CheckoutResponse.CheckoutResponseCallback
    ) {
        apiRequest.checkout(auth, jsonObject, callback)
    }

    fun simulatePayment(
        auth: String, jsonObject: JSONObject,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        apiRequest.simulatePayment(auth, jsonObject, callback)
    }

    private var mCheckout = CheckoutResponse()
    fun saveCheckoutResponse(checkoutResponse: CheckoutResponse) {
        mCheckout = checkoutResponse
    }

    fun getCheckoutResponse(): CheckoutResponse {
        return mCheckout
    }

}