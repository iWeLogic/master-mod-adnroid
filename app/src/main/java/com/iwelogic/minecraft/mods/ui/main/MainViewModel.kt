package com.iwelogic.minecraft.mods.ui.main

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.ui.base.BaseViewModel
import com.iwelogic.minecraft.mods.ui.base.SingleLiveEvent
import com.iwelogic.minecraft.mods.utils.readInteger
import com.iwelogic.minecraft.mods.utils.writeInteger
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(@ApplicationContext applicationContext: Context) : BaseViewModel(applicationContext) {

    companion object {
        const val COUNTER_STARTUPS = "counter_startups"
    }

    var selectedItemId = R.id.addons
    val showRatingDialog: SingleLiveEvent<Boolean> = SingleLiveEvent()

    init {
        viewModelScope.launch {
            delay(1000)
            var opens = applicationContext.readInteger(COUNTER_STARTUPS, 0)
            opens++
            if (opens > 4 && opens % 5 == 0) {
                showRatingDialog.postValue(true)
            }
            applicationContext.writeInteger(COUNTER_STARTUPS, opens)
        }
    }
}
