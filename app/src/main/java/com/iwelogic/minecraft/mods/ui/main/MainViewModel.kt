package com.iwelogic.minecraft.mods.ui.main

import android.content.Context
import com.iwelogic.minecraft.mods.data.Repository
import com.iwelogic.minecraft.mods.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository, @ApplicationContext applicationContext: Context) : BaseViewModel(applicationContext) {

}
