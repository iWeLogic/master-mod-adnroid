package com.iwelogic.minecraft.mods.ui.base

import com.iwelogic.minecraft.mods.manager.AdUnit

data class AdDataHolder(
    val adUnit: AdUnit,
    val callback: () -> Unit
)
