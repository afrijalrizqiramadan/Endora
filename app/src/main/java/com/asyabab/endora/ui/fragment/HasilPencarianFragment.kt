package com.asyabab.endora.ui.fragment

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.asyabab.endora.R
import com.asyabab.endora.base.BaseFragment
import com.asyabab.endora.data.models.general.GeneralResponse
import com.asyabab.endora.data.models.item.search.Result
import com.asyabab.endora.data.models.item.search.SearchResponse
import com.asyabab.endora.ui.activity.DetailProdukActivity
import com.asyabab.endora.utils.RecyclerViewAdapter
import com.asyabab.endora.utils.launchActivity
import com.asyabab.endora.utils.loadImageFromResources
import com.asyabab.endora.utils.onClick
import kotlinx.android.synthetic.main.fragment_hasilpencarian.*
import kotlinx.android.synthetic.main.fragment_hasilpencarian.view.*
import kotlinx.android.synthetic.main.popup_filter.*
import kotlinx.android.synthetic.main.popup_filter.radioBtn1
import kotlinx.android.synthetic.main.popup_urutkan.*
import kotlinx.android.synthetic.main.rv_produk.*
import kotlinx.android.synthetic.main.rv_produk.view.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.math.max


class HasilPencarianFragment : BaseFragment() {

    var result = ""
    var token = ""
    private var searchAdapter: RecyclerViewAdapter<Result> = RecyclerViewAdapter(
        R.layout.rv_produk,
        onBind = { view, data, position ->
            view.tvNama.text = data.name
            if (data.promo?.isEmpty()!!) {
                view.tvKeteranganPromo.visibility = View.INVISIBLE
                view.tvKeterangan.visibility = View.INVISIBLE
                view.tvHarga.text = data.price?.convertRupiah()

            } else {
                var hasil = data.price!! * data.promo!![0].discount!! / 100
                view.tvKeterangan.visibility = View.VISIBLE
                view.tvKeteranganPromo.visibility = View.VISIBLE
                if (hasil < data.promo!![0].maximum!!) {
                    view.tvHarga.text = (data.price!! - hasil).convertRupiah()
                } else {
                    view.tvHarga.text = (data.price!! - data.promo!![0].maximum!!).convertRupiah()

                }
                view.tvKeteranganPromo.text = data.price?.convertRupiah()
                view.tvKeterangan.text = " Diskon " + data.promo!![0].discount!! + "% "

            }
//            view.tvKeterangan.text = data.description
//            data.brandId?.let { view.tvGambar.loadImageFromResources(this, it) }
            if (data.isFavorite!!) {
                context?.let {
                    view.btIconFavorit.loadImageFromResources(
                        it,
                        R.drawable.icon_lovered
                    )
                }
            } else {
                context?.let {
                    view.btIconFavorit.loadImageFromResources(
                        it,
                        R.drawable.icon_lovewhite
                    )
                }

            }
            view.btIconFavorit.onClick {
                repository?.getToken()?.let { setFavorit(data.id.toString(), it) }
            }
            data.images?.get(0)?.name?.let { view.tvGambar.loadImageFromResources(context, it) }
            view.onClick {
                requireActivity().launchActivity<DetailProdukActivity> {
                    putExtra("data", data.id.toString())
                }
            }
        })

    fun Any.convertRupiah(): String {
        val df = DecimalFormat("#,###,##0")


        val strFormat = df.format(this)
        var bilangan = "Rp " + strFormat
        return bilangan
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        result = requireArguments().getString("uid").toString()
//        item = .getSerializableExtra("data") as Item


        val root = inflater.inflate(R.layout.fragment_hasilpencarian, container, false)

        val layoutManager = GridLayoutManager(context, 2)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        root.rvHasilPencarian.layoutManager = layoutManager
        root.rvHasilPencarian.adapter = searchAdapter
        token = repository?.getToken().toString()
        setSearch(result, "", "", "", token)


        root.btFilter.onClick {
            popUpFilter()
        }
        root.btUrutkan.onClick {
            popUpUrutkan()
        }
        return root
    }

    fun popUpFilter() {
        val dialog = context?.let { Dialog(it) }

        dialog?.apply {
            setContentView(R.layout.popup_filter)
            window?.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            btAturUlang.onClick {
                inputMin.setText("")
                inputMax.setText("")
            }
            btTerapkan.onClick {
                setSearch(result, "", inputMin.text.toString(), inputMax.text.toString(), token)
                dismiss()
            }
            show()
        }
    }

    fun popUpUrutkan() {
        val dialog = context?.let { Dialog(it) }

        dialog?.apply {
            setContentView(R.layout.popup_urutkan)
            window?.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            radioGroup?.setOnCheckedChangeListener { group, checkedId ->
                if (R.id.radioBtn1 == checkedId) {
                    searchAdapter.items.sortBy { it.price }
                    searchAdapter.notifyDataSetChanged()
                    dismiss()
                } else if(R.id.radioBtn2 == checkedId){
                    searchAdapter.items.sortByDescending { it.price }
                    searchAdapter.notifyDataSetChanged()
                    dismiss()

                }
//                else if(R.id.radioBtn3 == checkedId){
//                    searchAdapter.items.sortBy { it.price }
//                    searchAdapter.notifyDataSetChanged()
//                    dismiss()
//
//                }else if(R.id.radioBtn4 == checkedId){
//                    searchAdapter.items.sortBy { it.price }
//                    searchAdapter.notifyDataSetChanged()
//                    dismiss()
//
//                }
                else if(R.id.radioBtn5 == checkedId){
                    searchAdapter.items.sortBy { it.name }
                    searchAdapter.notifyDataSetChanged()
                    dismiss()

                }else {
                    searchAdapter.items.sortByDescending { it.name }
                    searchAdapter.notifyDataSetChanged()
                    dismiss()

                }
            }


            show()
        }
    }


    fun setSearch(
        search: String,
        order: String,
        pricemin: String,
        pricemax: String,
        auth: String
    ) {
        repository!!.setSearch(
            search,
            order,
            pricemin,
            pricemax,
            repository?.getToken()!!,
            object : SearchResponse.SearchResponseCallback {
                override fun onSuccess(searchResponse: SearchResponse) {
                    if (searchResponse.status == true) {
//                        loadFragment(mHasilPencarianFragment)
                        if (searchResponse.data?.results?.size.toString() != "0") {
                            panelList.visibility = View.VISIBLE
                            panelKosong.visibility = View.GONE

                            tvJumlahProduk.text =
                                "Ditemukan " + searchResponse.data!!.results?.size.toString() + " Hasil"
                            searchAdapter.clearItems()
                            searchResponse.data!!.results?.let { searchAdapter.addItems(it) }
                        } else {
                            panelKosong.visibility = View.VISIBLE
                            panelList.visibility = View.GONE

                        }

                    } else {
                        panelKosong.visibility = View.VISIBLE
                        panelList.visibility = View.GONE

                    }
                }

                override fun onFailure(message: String) {
                    Log.e("Hasil", "Gagal Memuat" + message)

                    Toast.makeText(context, "Gagal Memuat" + message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    fun setFavorit(
        id: String,
        auth: String
    ) {
        repository!!.setFavorit(
            id,
            repository?.getToken()!!,
            object : GeneralResponse.GeneralResponseCallback {
                override fun onSuccess(generalResponse: GeneralResponse) {
                    if (generalResponse.status == true) {
                        if (generalResponse.message == "Item Favorit Berhasil Dihapus!!") {
                            Toast.makeText(context, generalResponse.message, Toast.LENGTH_SHORT)
                                .show()
                            btIconFavorit.setImageResource(R.drawable.icon_lovewhite)
                        } else {
                            Toast.makeText(context, generalResponse.message, Toast.LENGTH_SHORT)
                                .show()
                            btIconFavorit.setImageResource(R.drawable.icon_lovered)
                        }
                    } else {
                        Toast.makeText(context, "Gagal Menambahkan", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(message: String) {
                    Log.e("Hasil", "Gagal Memuat" + message)

                    Toast.makeText(context, "Gagal Memuat" + message, Toast.LENGTH_SHORT).show()
                }

            })
    }
}