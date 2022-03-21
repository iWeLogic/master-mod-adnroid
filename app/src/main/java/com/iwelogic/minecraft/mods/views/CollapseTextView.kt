package com.iwelogic.minecraft.mods.views

import android.content.Context
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.text.HtmlCompat
import com.iwelogic.minecraft.mods.R
import java.util.regex.Pattern


class CollapseTextView : LinearLayout {

    var isExpand = false
    var isActiveAnimation = false

    constructor(context: Context?) : super(context) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.layout_collapse_text, this, true)
        val bodyView = findViewById<TextView>(R.id.body)
        findViewById<View>(R.id.collapseTitle).setOnClickListener {
            if (isActiveAnimation) return@setOnClickListener
            isExpand = !isExpand
            findViewById<ImageView>(R.id.icon).setImageResource(if (isExpand) R.drawable.collapse else R.drawable.expand)
            if (isExpand) {
                expandView(findViewById(R.id.body))
            } else {
                collapse(findViewById(R.id.body))
            }
        }

        val httpPattern = Pattern.compile("[a-z]+://[^ \\n]*")
        Linkify.addLinks( bodyView, httpPattern, "")
        Linkify.addLinks( bodyView, Pattern.compile("[0-9]?(\\+[0-9]+[\\- .]*)?(\\([0-9]+\\)[\\- .]*)?([0-9][0-9\\- .]+[0-9])"), "tel:", Linkify.sPhoneNumberMatchFilter, Linkify.sPhoneNumberTransformFilter)
        bodyView.movementMethod = LinkMovementMethod.getInstance()
    }

    fun setBody(text: String){
        findViewById<TextView>(R.id.body).text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
    private fun expandView(view: View) {
        isActiveAnimation = true
        view.visibility = VISIBLE
        val params = view.layoutParams as LayoutParams
        val width = this.width - params.leftMargin - params.rightMargin
        view.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
        val targetHeight = view.measuredHeight
        view.layoutParams.height = 0
        val anim: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, trans: Transformation?) {
                view.layoutParams.height = (targetHeight * interpolatedTime).toInt()
                view.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
            }

            override fun onAnimationEnd(animation: Animation) {
                isActiveAnimation = false
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        anim.duration = 500
        view.startAnimation(anim)
    }

    private fun collapse(view: View) {
        isActiveAnimation = true
        val params = view.layoutParams as LayoutParams
        val width = this.width - params.leftMargin - params.rightMargin
        view.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
        val targetHeight = view.measuredHeight
        view.layoutParams.height = 0
        val anim: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, trans: Transformation?) {
                view.layoutParams.height = targetHeight - (targetHeight * interpolatedTime).toInt()
                view.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                view.visibility = GONE
                isActiveAnimation = false
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        anim.duration = 500
        view.startAnimation(anim)
    }
}