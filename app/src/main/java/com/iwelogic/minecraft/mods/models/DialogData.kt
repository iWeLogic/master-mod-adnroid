package com.iwelogic.minecraft.mods.models

data class DialogData(
    val title: String? = null,
    val message: String? = null,
    val buttonLeftTitle: String? = null,
    val buttonRightTitle: String? = null,
    val onClickLeft: (() -> Unit)? = null,
    val onClickRight: (() -> Unit)? = null
)