package com.iwelogic.minecraft.mods.ui.main

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.ui.base.BaseViewModel
import com.iwelogic.minecraft.mods.ui.base.SingleLiveEvent
import com.iwelogic.minecraft.mods.utils.logEvent
import com.iwelogic.minecraft.mods.utils.readBoolean
import com.iwelogic.minecraft.mods.utils.writeBoolean
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(@ApplicationContext applicationContext: Context) : BaseViewModel(applicationContext) {

    companion object {
        const val INSTALLED = "installed"
        const val SHOWED_RATING = "showed_rating"
    }

    var selectedItemId = R.id.addons
    val showRatingDialog: SingleLiveEvent<Boolean> = SingleLiveEvent()

    init {
        viewModelScope.launch {
            delay(1500)
            if (applicationContext.readBoolean(INSTALLED)) {
                if (!applicationContext.readBoolean(SHOWED_RATING)) {
                    context.get().logEvent(SHOWED_RATING)
                    applicationContext.writeBoolean(SHOWED_RATING, true)
                }
                showRatingDialog.postValue(true)
            }

        }
    }
}
