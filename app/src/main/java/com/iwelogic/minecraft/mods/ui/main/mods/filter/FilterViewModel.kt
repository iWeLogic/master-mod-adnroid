package com.iwelogic.minecraft.mods.ui.main.mods.filter

import android.content.Context
import androidx.lifecycle.MutableLiveData
import  com.iwelogic.minecraft.mods.data.Repository
import com.iwelogic.minecraft.mods.models.FilterValue
import  com.iwelogic.minecraft.mods.ui.base.BaseViewModel
import com.iwelogic.minecraft.mods.ui.base.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(private val repository: Repository, @ApplicationContext applicationContext: Context) : BaseViewModel() {

    val items: MutableLiveData<List<FilterValue>> = MutableLiveData()
    val apply: SingleLiveEvent<List<FilterValue>> = SingleLiveEvent()

    fun onClickOk() {
        apply.invoke(items.value ?: ArrayList())
    }
}