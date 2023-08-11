package com.iwelogic.minecraft.mods.ui.details

import java.io.File

sealed class DetailsViewUiEvent {
    class InstallMod(val file: File) : DetailsViewUiEvent()
    object ShowRatingDialog : DetailsViewUiEvent()
}