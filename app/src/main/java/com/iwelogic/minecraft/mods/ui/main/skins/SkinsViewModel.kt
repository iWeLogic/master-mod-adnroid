package com.iwelogic.minecraft.mods.ui.main.skins

import android.content.Context
import  com.iwelogic.minecraft.mods.data.Repository
import  com.iwelogic.minecraft.mods.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class SkinsViewModel @Inject constructor(private val repository: Repository) : BaseViewModel() {
}
