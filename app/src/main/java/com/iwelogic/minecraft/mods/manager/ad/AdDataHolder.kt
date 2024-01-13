package com.iwelogic.minecraft.mods.manager.ad

data class AdDataHolder(
    val adUnit: AdUnit,
    val callback: () -> Unit
)
