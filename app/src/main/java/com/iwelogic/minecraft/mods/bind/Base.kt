package com.iwelogic.minecraft.mods.bind

import android.text.method.LinkMovementMethod
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.*
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.utils.dp


object Base {

    @BindingAdapter("queryTextListener")
    @JvmStatic
    fun setOnQueryTextListener(searchView: SearchView, listener: SearchView.OnQueryTextListener) {
        searchView.setOnQueryTextListener(listener)
    }

    @BindingAdapter("adView")
    @JvmStatic
    fun setAdView(view: LinearLayout, adView: View?) {
        if (adView != null) {
            adView.parent?.let {
                (it as ViewGroup).removeView(adView)
            }
            view.removeAllViews()
            view.addView(adView)
        }
    }


    @BindingAdapter("query")
    @JvmStatic
    fun setOnQuery(searchView: SearchView, query: String) {
        searchView.setQuery(query, false)
        val txtSearch = searchView.findViewById(androidx.appcompat.R.id.search_src_text) as EditText
        txtSearch.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
        txtSearch.setTextColor(ContextCompat.getColor(searchView.context, R.color.title))
        txtSearch.setHintTextColor(ContextCompat.getColor(searchView.context, R.color.hintText))
        txtSearch.typeface = ResourcesCompat.getFont(searchView.context, R.font.minecraft_regular)
        val searchClose = searchView.findViewById(androidx.appcompat.R.id.search_close_btn) as ImageView
        searchClose.setImageResource(R.drawable.clear)
    }

    @BindingAdapter("image", "scaleType", "radius", requireAll = false)
    @JvmStatic
    fun setImage(view: ImageView, image: String?, scaleType: ScaleType?, radius: Int?) {
        Log.w("myLog", "Base_setImage: ${image}")
        val circularProgressDrawable = CircularProgressDrawable(view.context)
        circularProgressDrawable.strokeWidth = 6.dp(view.context).toFloat()
        circularProgressDrawable.centerRadius = 24.dp(view.context).toFloat()
        circularProgressDrawable.setColorSchemeColors(ContextCompat.getColor(view.context, R.color.title))
        circularProgressDrawable.start()
        image?.let {
            Glide.with(view.context).load(image).transform(
                if (radius != null && radius > 0) {
                    when (scaleType) {
                        ScaleType.CENTER_INSIDE -> MultiTransformation(CenterInside(), RoundedCorners(radius.dp(view.context)))
                        ScaleType.FIT_CENTER -> MultiTransformation(FitCenter(), RoundedCorners(radius.dp(view.context)))
                        ScaleType.CIRCLE_CROP -> MultiTransformation(CircleCrop(), RoundedCorners(radius.dp(view.context)))
                        else -> MultiTransformation(CenterCrop(), RoundedCorners(radius.dp(view.context)))
                    }
                } else {
                    when (scaleType) {
                        ScaleType.CENTER_INSIDE -> CenterInside()
                        ScaleType.FIT_CENTER -> FitCenter()
                        ScaleType.CIRCLE_CROP -> CircleCrop()
                        else -> CenterCrop()
                    }
                }
            ).placeholder(circularProgressDrawable).into(view)
        }
    }

    enum class ScaleType {
        CENTER_CROP,
        CENTER_INSIDE,
        FIT_CENTER,
        CIRCLE_CROP
    }

    @BindingAdapter("html")
    @JvmStatic
    fun setHtml(view: TextView, html: String?) {
        view.text = HtmlCompat.fromHtml(html ?: "", HtmlCompat.FROM_HTML_MODE_LEGACY)
        view.movementMethod = LinkMovementMethod.getInstance()
    }
}