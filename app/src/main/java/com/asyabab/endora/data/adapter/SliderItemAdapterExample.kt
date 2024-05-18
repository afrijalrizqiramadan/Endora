package com.asyabab.endora.data.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.asyabab.endora.R
import com.asyabab.endora.data.adapter.SliderAdapterExample.SliderAdapterVH
import com.asyabab.endora.data.models.SliderItem
import com.asyabab.endora.data.models.home.Billboard
import com.asyabab.endora.data.models.item.detailitem.Image
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter
import java.util.*

class SliderItemAdapterExample(private val context: Context) :
    SliderViewAdapter<SliderAdapterVH>() {
    private var mSliderItems: MutableList<Image> =
        ArrayList()

    fun renewItems(sliderItems: MutableList<Image>) {
        mSliderItems = sliderItems
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        mSliderItems.removeAt(position)
        notifyDataSetChanged()
    }

    fun addItem(sliderItem: Image) {
        mSliderItems.add(sliderItem)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterExample.SliderAdapterVH? {
        val inflate = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_slider_layout_item, null)
        return SliderAdapterExample.SliderAdapterVH(inflate)
    }


    override fun getCount(): Int {
        //slider view count could be dynamic size
        return mSliderItems.size
    }

    class SliderAdapterVH(itemView: View) :
        ViewHolder(itemView) {
        var itemVie: View
        var imageViewBackground: ImageView
        var imageGifContainer: ImageView
        var textViewDescription: TextView

        init {
            imageViewBackground =
                itemView.findViewById(R.id.iv_auto_image_slider)
            imageGifContainer =
                itemView.findViewById(R.id.iv_gif_container)
            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider)
            this.itemVie = itemView
        }
    }

    override fun onBindViewHolder(
        viewHolder: SliderAdapterExample.SliderAdapterVH?,
        position: Int
    ) {
        val sliderItem = mSliderItems[position]

        viewHolder?.itemView?.let {
            Glide.with(it)
                .load((sliderItem.name))
                .fitCenter()
                .into(viewHolder.imageViewBackground)
        }
    }
}
