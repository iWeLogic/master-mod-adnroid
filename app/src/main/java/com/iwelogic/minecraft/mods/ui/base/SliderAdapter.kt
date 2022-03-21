package com.iwelogic.minecraft.mods.ui.base

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.iwelogic.minecraft.mods.R
import com.smarteist.autoimageslider.SliderViewAdapter
import java.util.*

class SliderAdapter : SliderViewAdapter<SliderAdapter.ImageViewHolder>() {

    private var images: List<String> = ArrayList()

    fun renewItems(images: List<String>) {
        this.images = images
        notifyDataSetChanged()
    }

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup): ImageViewHolder {
        return ImageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_image, null))
    }

    override fun onBindViewHolder(viewHolder: ImageViewHolder, position: Int) {
        Glide.with(viewHolder.view.context).load(images[position]).into(viewHolder.view.findViewById(R.id.image))
    }

    override fun getCount(): Int {
        return images.size
    }

    inner class ImageViewHolder(val view: View) : SliderViewAdapter.ViewHolder(view)
}