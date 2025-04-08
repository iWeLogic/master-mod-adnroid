package com.iwelogic.minecraft.mods.bind

import android.annotation.*
import android.text.method.*
import android.view.*
import android.view.inputmethod.*
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.core.content.*
import androidx.core.text.*
import androidx.core.widget.*
import androidx.databinding.*
import androidx.swiperefreshlayout.widget.*
import com.bumptech.glide.*
import com.bumptech.glide.load.*
import com.bumptech.glide.load.resource.bitmap.*
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.utils.*


object Base {

    @SuppressLint("ClickableViewAccessibility")
    @BindingAdapter("queryTextListener")
    @JvmStatic
    fun setOnQueryTextListener(view: EditText, listener: SearchView.OnQueryTextListener) {
        view.addTextChangedListener {
            listener.onQueryTextChange(view.text.toString())
        }
        view.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP && !view.text.isNullOrEmpty()) {
                val drawableEnd = 2
                view.compoundDrawables[drawableEnd]?.let { drawable ->
                    val bounds = drawable.bounds
                    val x = event.rawX.toInt()
                    val right = view.right
                    if (x >= right - bounds.width() - view.paddingEnd) {
                        view.text.clear()
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }
        view.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_SEARCH ||
                (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                view.hideKeyboard(true)
                true
            } else {
                false
            }
        }
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
    fun setOnQuery(view: EditText, query: String) {
        if (query != view.text.toString())
            view.setText(query)
        view.addTextChangedListener {
            view.setCompoundDrawablesWithIntrinsicBounds(0, 0, if (view.text.isNullOrEmpty()) 0 else R.drawable.clear, 0)
        }
    }

    @BindingAdapter("image", "scaleType", "radius", requireAll = false)
    @JvmStatic
    fun setImage(view: ImageView, image: String?, scaleType: ScaleType?, radius: Int?) {
        val circularProgressDrawable = CircularProgressDrawable(view.context)
        circularProgressDrawable.strokeWidth = 6.dp(view.context).toFloat()
        circularProgressDrawable.centerRadius = 24.dp(view.context).toFloat()
        circularProgressDrawable.setColorSchemeColors(
            ContextCompat.getColor(
                view.context,
                R.color.title
            )
        )
        circularProgressDrawable.start()
        image?.let {
            Glide.with(view.context).load(image).transform(
                if (radius != null && radius > 0) {
                    when (scaleType) {
                        ScaleType.CENTER_INSIDE -> MultiTransformation(
                            CenterInside(),
                            RoundedCorners(radius.dp(view.context))
                        )
                        ScaleType.FIT_CENTER -> MultiTransformation(
                            FitCenter(),
                            RoundedCorners(radius.dp(view.context))
                        )
                        ScaleType.CIRCLE_CROP -> MultiTransformation(
                            CircleCrop(),
                            RoundedCorners(radius.dp(view.context))
                        )
                        else -> MultiTransformation(
                            CenterCrop(),
                            RoundedCorners(radius.dp(view.context))
                        )
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