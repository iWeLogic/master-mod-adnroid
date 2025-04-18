package com.iwelogic.minecraft.mods.ui.rating

import android.content.Context
import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import com.google.firebase.analytics.FirebaseAnalytics
import com.iwelogic.minecraft.mods.ui.base.BaseViewModel
import com.iwelogic.minecraft.mods.ui.base.Const.KEY_DOWNLOADS_COUNTER
import com.iwelogic.minecraft.mods.ui.base.Const.KEY_MAYBE_LATER
import com.iwelogic.minecraft.mods.ui.base.Const.KEY_RATE_NOW
import com.iwelogic.minecraft.mods.ui.base.Const.RATING
import com.iwelogic.minecraft.mods.ui.base.SingleLiveEvent
import com.iwelogic.minecraft.mods.utils.writeBoolean
import com.iwelogic.minecraft.mods.utils.writeInteger
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


@HiltViewModel
class RatingViewModel @Inject constructor(@ApplicationContext applicationContext: Context) : BaseViewModel(applicationContext) {

    val rating: MutableLiveData<Float> = MutableLiveData(5.0f)
    val showPlayMarketRating: SingleLiveEvent<Boolean> = SingleLiveEvent()

    fun onClickRateNow() {
        context.get()?.let {
            it.writeBoolean(KEY_RATE_NOW, true)
            FirebaseAnalytics.getInstance(it).logEvent(KEY_RATE_NOW, bundleOf(RATING to rating.value))
        }
        if ((rating.value ?: 0.0f) > 3.9f)
            showPlayMarketRating.invoke(true)
        onClickClose()
    }

    fun onClickMaybeLater() {
        context.get()?.let {
            it.writeInteger(KEY_DOWNLOADS_COUNTER, -20)
            FirebaseAnalytics.getInstance(it).logEvent(KEY_MAYBE_LATER, bundleOf(RATING to rating.value))
        }
        onClickClose()
    }
}

