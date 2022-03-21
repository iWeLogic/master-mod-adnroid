package com.iwelogic.minecraft.mods.ui.base

import androidx.fragment.app.Fragment

open class BaseFragment<VM : BaseViewModel> : Fragment() {

    lateinit var viewModel: VM
}