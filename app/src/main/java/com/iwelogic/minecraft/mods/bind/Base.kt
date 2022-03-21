package com.iwelogic.minecraft.mods.bind

import android.text.Editable
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.util.TypedValue
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
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

    @BindingAdapter("query")
    @JvmStatic
    fun setOnQuery(searchView: SearchView, query: String) {
        searchView.setQuery(query, false)
        val txtSearch = searchView.findViewById(androidx.appcompat.R.id.search_src_text) as EditText
        txtSearch.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
        txtSearch.setTextColor(ContextCompat.getColor(searchView.context, R.color.white))
        txtSearch.setHintTextColor(ContextCompat.getColor(searchView.context, R.color.white50))
        val searchClose = searchView.findViewById(androidx.appcompat.R.id.search_close_btn) as ImageView
        searchClose.setImageResource(R.drawable.clear)
    }

    @BindingAdapter("image")
    @JvmStatic
    fun setImage(view: ImageView, image: String?) {
        val radius = (view.tag ?: "0").toString().toInt()
        image?.let {
            Glide.with(view.context).load(image).transform(
                when (view.scaleType) {
                    ImageView.ScaleType.CENTER_CROP -> if (radius == 0) CenterCrop() else MultiTransformation(CenterCrop(), RoundedCorners(radius.dp(view.context)))
                    ImageView.ScaleType.CENTER_INSIDE -> if (radius == 0) CenterInside() else MultiTransformation(CenterInside(), RoundedCorners(radius.dp(view.context)))
                    ImageView.ScaleType.FIT_CENTER -> if (radius == 0) FitCenter() else MultiTransformation(FitCenter(), RoundedCorners(radius.dp(view.context)))
                    else -> CircleCrop()
                }
            ).into(view)
        }
    }

    @BindingAdapter("image")
    @JvmStatic
    fun setImage(view: ImageView, image: Int?) {
        val radius = (view.tag ?: "0").toString().toInt()
        image?.let {
            Glide.with(view.context).load(image).transform(
                when (view.scaleType) {
                    ImageView.ScaleType.CENTER_CROP -> if (radius == 0) CenterCrop() else MultiTransformation(CenterCrop(), RoundedCorners(radius.dp(view.context)))
                    ImageView.ScaleType.CENTER_INSIDE -> if (radius == 0) CenterInside() else MultiTransformation(CenterInside(), RoundedCorners(radius.dp(view.context)))
                    ImageView.ScaleType.FIT_CENTER -> if (radius == 0) FitCenter() else MultiTransformation(FitCenter(), RoundedCorners(radius.dp(view.context)))
                    else -> CircleCrop()
                }
            ).into(view)
        }
    }

    @BindingAdapter("html")
    @JvmStatic
    fun setHtml(view: TextView, html: String?) {
        view.text = HtmlCompat.fromHtml(html ?: "", HtmlCompat.FROM_HTML_MODE_LEGACY)
        view.movementMethod = LinkMovementMethod.getInstance()
    }

/*    @BindingAdapter("error")
    @JvmStatic
    fun setError(view: EditText, error: String?) {
        error?.let {
            val icon = ContextCompat.getDrawable(view.context, R.drawable.myerror)
            icon!!.bounds = Rect(0, 0, Objects.requireNonNull(icon).intrinsicWidth, icon.intrinsicHeight)
            view.requestFocus()
            view.setError(error, icon)
        } ?: run {
            view.setError(null, null)
        }
    }*/

    @BindingAdapter("onTextChangedListener")
    @JvmStatic
    fun onTextChanged(view: EditText, onTextChangedListener: (String) -> Unit) {
        view.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
                onTextChangedListener.invoke(text.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })
    }

}