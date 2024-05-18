package com.asyabab.endora.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.asyabab.endora.R
import com.asyabab.endora.base.BaseFragment
import com.asyabab.endora.data.models.user.getsearch.Data
import com.asyabab.endora.data.models.user.getsearch.GetSearchResponse
import com.asyabab.endora.utils.*
import kotlinx.android.synthetic.main.fragment_focus.view.*
import kotlinx.android.synthetic.main.rv_getsearch.view.*


class FocusFragment : BaseFragment() {

    private var getSearchAdapter: RecyclerViewAdapter<Data> = RecyclerViewAdapter(
        R.layout.rv_getsearch,
        onBind = { view, data, position ->
            view.tvNamaSearch.text = data.search
            view.onClick {
                val textview = requireActivity().findViewById<View>(R.id.inputSearch) as TextView
                textview.text = data.search.toString()
//                Log.d("Tss",data.search?.get(position).toString())
            }
        })


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_focus, container, false)

        root.rvGetSearch.setVerticalLayout(true)
        root.rvGetSearch.adapter = getSearchAdapter

        getSearch()

        return root
    }

    private fun getSearch() {
        repository!!.getSearch(
            repository?.getToken()!!,
            object : GetSearchResponse.GetSearchResponeCallback {
                override fun onSuccess(getSearchResponse: GetSearchResponse) {
                    if (getSearchResponse.status == true) {
                        getSearchAdapter.clearItems()
                        getSearchResponse.data?.let { getSearchAdapter.addItems(it) }
                    }
                }

                override fun onFailure(message: String) {
                }

            })
    }
}