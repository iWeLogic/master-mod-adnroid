package com.iwelogic.minecraft.mods.views

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import androidx.core.widget.NestedScrollView


class SaveStateNestedScroll : NestedScrollView {

//    var previousY: Float = 0.0f
//    var previousX: Float = 0.0f

    constructor(context: Context) : super(context)

    constructor(context: Context, attributes: AttributeSet) : super(context, attributes)

    constructor(context: Context, attributes: AttributeSet, defStyleAttr: Int) : super(context, attributes, defStyleAttr)


    public override fun onSaveInstanceState(): Parcelable? {
        return super.onSaveInstanceState()
    }

    public override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)
    }

    /*override fun onTouchEvent(ev: MotionEvent?): Boolean {
        val deltaY = Math.abs(Math.abs(previousY) - Math.abs((ev?.y ?: 0.0f)))
        val deltaX = Math.abs(Math.abs(previousX) - Math.abs((ev?.x ?: 0.0f)))
        previousX = ev?.x ?: 0.0f
        previousY = ev?.y ?: 0.0f
        Log.w("myLog", "deltaY: " + deltaY)
        Log.w("myLog", "deltaX: " + deltaX)
        return if (deltaY > deltaX || ev?.action == 1 ) {
            Log.w("myLog", "onTouchEvent111: " + ev?.action)
            super.onTouchEvent(ev)
        } else {
            Log.w("myLog", "onTouchEvent222: " + ev?.action)
            true
        }
    }*/
}