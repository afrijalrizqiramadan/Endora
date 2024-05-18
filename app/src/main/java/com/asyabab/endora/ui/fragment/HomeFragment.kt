package com.asyabab.endora.ui.fragment

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.asyabab.endora.R
import com.asyabab.endora.base.BaseFragment
import com.asyabab.endora.data.adapter.SliderAdapterExample
import com.asyabab.endora.data.models.SliderItem
import com.asyabab.endora.data.models.cart.keranjang.KeranjangResponse
import com.asyabab.endora.data.models.general.GeneralResponse
import com.asyabab.endora.data.models.home.*
import com.asyabab.endora.data.models.view.HomeViewModel
import com.asyabab.endora.ui.activity.DetailOfferActivity
import com.asyabab.endora.ui.activity.DetailProdukActivity
import com.asyabab.endora.ui.activity.JelajahActivity
import com.asyabab.endora.utils.*
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.activity_beranda.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.rv_kategori.view.*
import kotlinx.android.synthetic.main.rv_offerhigh.view.*
import kotlinx.android.synthetic.main.rv_produk.view.*


class HomeFragment : BaseFragment() {

    private lateinit var homeViewModel: HomeViewModel
    var sliderView: SliderView? = null
    var adapter: SliderAdapterExample? = null
    val sliderItemList: MutableList<SliderItem> =
        ArrayList()
    val sliderItem = SliderItem()

    private var offerHighAdapter: RecyclerViewAdapter<OfferHigh> = RecyclerViewAdapter(
        R.layout.rv_offerhigh,
        onBind = { view, data, position ->
            data.cover?.let {
                view.tvOfferHighGambar.loadImageFromResources(
                    context,
                    it
                )
            }
            view.titleOffer.text=data.name

            view.onClick {
                requireActivity().launchActivity<DetailOfferActivity> {
                    putExtra("data", data.id)
                    putExtra("nama", data.name)
                }

            }
        })
    private var itemAdapter: RecyclerViewAdapter<Item> = RecyclerViewAdapter(
        R.layout.rv_produk,
        onBind = { view, data, position ->
            view.tvNama.text = data.name
            view.tvHarga.text = data.price.toString()
//            view.tvKeterangan.text = data.description
//            data.brandId?.let { view.tvGambar.loadImageFromResources(this, it) }
            if (data.favorite == false) {
                context?.let {
                    view.btIconFavorit.loadImageFromResources(
                        it,
                        R.drawable.icon_lovewhite
                    )
                }
            } else {
                context?.let {
                    view.btIconFavorit.loadImageFromResources(
                        it,
                        R.drawable.icon_lovered
                    )
                }
            }

            view.btIconFavorit.onClick {
                repository!!.setFavorit(
                    data.id.toString(),
                    repository?.getToken()!!,
                    object : GeneralResponse.GeneralResponseCallback {
                        override fun onSuccess(generalResponse: GeneralResponse) {
                            if (generalResponse.status == true) {
                                if (generalResponse.message == "Item Favorit Berhasil Dihapus!!") {
                                    Toast.makeText(
                                        context,
                                        generalResponse.message,
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                    context?.let {
                                        view.btIconFavorit.loadImageFromResources(
                                            it,
                                            R.drawable.icon_lovewhite
                                        )
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        generalResponse.message,
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                    view.btIconFavorit.loadImageFromResources(
                                        context,
                                        R.drawable.icon_lovered
                                    )
                                }
                            } else {
                                Toast.makeText(context, "Gagal Menambahkan", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }

                        override fun onFailure(message: String) {
                            Log.e("Hasil", "Gagal Memuat" + message)

                            Toast.makeText(context, "Gagal Memuat" + message, Toast.LENGTH_SHORT)
                                .show()
                        }

                    })
            }
            data.images?.get(0)?.name?.let { view.tvGambar.loadImageFromResources(context, it) }
            view.onClick {
                activity?.launchActivity<DetailProdukActivity> {
                    putExtra("data", data.id.toString())
                }
            }
        })

    private var kategoriAdapter: RecyclerViewAdapter<Category> = RecyclerViewAdapter(
        R.layout.rv_kategori,
        onBind = { view, data, position ->
            data.image?.let {
                view.tvKategoriGambar.loadImageFromResources(
                    context,
                    it
                )
            }
            view.tvKategoriNama.text = data.name

            view.onClick {
                val intent = Intent(context, JelajahActivity::class.java)
                intent.putExtra("string1", data.id.toString())
                intent.putExtra("string2", data.name.toString())
                startActivityForResult(intent, RESULT_OK);
            }
        })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
//        val textView: TextView = root.findViewById(R.id.text_home)
//        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        sliderView = root.findViewById(R.id.imageSlider)


        adapter = context?.let { SliderAdapterExample(it) }
        sliderView!!.setSliderAdapter(adapter!!)
        sliderView!!.setIndicatorAnimation(IndicatorAnimationType.WORM) //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!

        sliderView!!.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        sliderView!!.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_RIGHT
        sliderView!!.indicatorSelectedColor = Color.WHITE
        sliderView!!.indicatorUnselectedColor = Color.GRAY

        sliderView!!.setOnIndicatorClickListener {
            Log.i(
                "GGG",
                "onIndicatorClicked: " + sliderView!!.currentPagePosition
            )
        }


        root.rvOfferHigh.setVerticalLayout(true)
        root.rvOfferHigh.adapter = offerHighAdapter
        val layoutManager = GridLayoutManager(context, 2)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        root.rvItem.layoutManager = layoutManager
        root.rvItem.adapter = itemAdapter

        root.rvKategori.setHorizontalLayout(true)
        root.rvKategori.adapter = kategoriAdapter

        root.swipeResfresh.setOnRefreshListener(OnRefreshListener {
            getHome()
            getKeranjang()

            swipeResfresh.isRefreshing = false;

        })
//        renewItems()
        getHome()
        getKeranjang()

        return root
    }


    fun getHome() {
        repository!!.getHome(
            repository?.getToken()!!,
            object : HomeResponse.HomeResponseCallback {
                override fun onSuccess(homeResponse: HomeResponse) {
                    if (homeResponse.status == true) {
//                        if (keranjangResponse.data?.adaartikel == "ADA") {
//                       sliderAdapter.clearItems()
//                        sliderAdapter.addItems(homeResponse.data?.billboard!!)
                        adapter!!.renewItems((homeResponse.data?.billboard as MutableList<Billboard>?)!!)
                        offerHighAdapter.clearItems()
                        homeResponse.data!!.offerHigh?.let { offerHighAdapter.addItems(it) }
//                        itemAdapter.clearItems()
//                        homeResponse.data!!.item?.let { itemAdapter.addItems(it) }
//                        kategoriAdapter.clearItems()
//                        homeResponse.data!!.category?.let { kategoriAdapter.addItems(it) }
                        framemain.visibility = View.VISIBLE
                        frameshimmer.visibility = View.GONE
//                        } else {
//
//                        }
                    }
                }

                override fun onFailure(message: String) {
                    Toast.makeText(context, "Server Sedang Error", Toast.LENGTH_LONG).show()

                }

            })
    }


    fun getKeranjang() {
        repository!!.getKeranjang(
            repository?.getToken()!!,
            object : KeranjangResponse.KeranjangResponseCallback {
                override fun onSuccess(keranjangResponse: KeranjangResponse) {
                    if (keranjangResponse.status == true) {
                        repository!!.saveData(
                            "jumlahkeranjang",
                            keranjangResponse.data!!.size.toString()
                        )
                        activity?.tvIconNumber?.text = repository!!.getData("jumlahkeranjang")
                    }
                }

                override fun onFailure(message: String) {

                    Toast.makeText(context, "Server Sedang Error", Toast.LENGTH_LONG)
                        .show()


                }

            })
    }

}

