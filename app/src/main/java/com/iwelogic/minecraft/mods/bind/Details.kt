package com.iwelogic.minecraft.mods.bind

import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.LayerDrawable
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.ui.base.SliderAdapter
import com.iwelogic.minecraft.mods.views.CollapseTextView
import com.iwelogic.minecraft.mods.models.Mod
import com.smarteist.autoimageslider.SliderView

object Details {

    @BindingAdapter("images")
    @JvmStatic
    fun setImage(imageSlider: SliderView, item: Mod?) {
        item?.let {
            if (imageSlider.sliderAdapter == null) {
                val sliderAdapter = SliderAdapter()
                sliderAdapter.renewItems(item.getImages())
                imageSlider.setSliderAdapter(sliderAdapter)
            }
            imageSlider.startAutoCycle()
            imageSlider.setCurrentPageListener {
                imageSlider.let {
                    it.stopAutoCycle()
                    it.startAutoCycle()
                }
            }
        }
    }

    @BindingAdapter("progress")
    @JvmStatic
    fun setProgress(view: ImageView, progress: Int) {
        Log.w("myLog", "setProgress: BUTTON" + progress)
        val mClipDrawable = (view.background as LayerDrawable).findDrawableByLayerId(R.id.clip_drawable) as ClipDrawable
        mClipDrawable.level = progress
        view.isEnabled = progress != 0 || progress != 10000
    }

    @BindingAdapter("body")
    @JvmStatic
    fun setCollapseView(view: CollapseTextView, body: String) {
        view.setBody(body)
    }
}