package com.iwelogic.minecraft.mods.ui.main

import androidx.lifecycle.MutableLiveData
import com.iwelogic.minecraft.mods.data.Repository
import com.iwelogic.minecraft.mods.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository) : BaseViewModel() {

    val title: MutableLiveData<String> = MutableLiveData()
}
