package com.iwelogic.minecraft.mods.ui.main.mods.filter

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.models.FilterValue
import com.iwelogic.minecraft.mods.ui.base.BaseViewModel
import com.iwelogic.minecraft.mods.ui.base.SingleLiveEvent
import com.iwelogic.minecraft.mods.utils.fromPxToDp
import com.iwelogic.minecraft.mods.utils.isTrue
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(@ApplicationContext applicationContext: Context) : BaseViewModel(applicationContext) {

    val items: MutableLiveData<List<FilterValue>> = MutableLiveData()
    val apply: SingleLiveEvent<List<FilterValue>> = SingleLiveEvent()
    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val spanCount: MutableLiveData<Int> = MutableLiveData(1)

    init {
        items.observeForever { list ->
            list.forEach { filterValue ->
                filterValue.valueChangeObserver = {
                    errorMessage.postValue("")
                }
            }
        }
    }

    fun onClickApply() {
        if (items.value?.none { it.value }.isTrue()) errorMessage.postValue(context.get()?.getString(R.string.you_should_select_at_least_one_category))
        else apply.invoke(items.value?.onEach { it.valueChangeObserver = null } ?: ArrayList())
    }

    fun reloadScreenSize(widthDp: Int?) {
        widthDp?.fromPxToDp(context.get())?.let {
            spanCount.postValue(if (it > 700) 2 else 1)
        }
    }
}