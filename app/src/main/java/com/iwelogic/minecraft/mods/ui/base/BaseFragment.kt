package com.iwelogic.minecraft.mods.ui.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

open class BaseFragment<VM : BaseViewModel> : Fragment() {

    lateinit var viewModel: VM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.close.observe(viewLifecycleOwner) {
            activity?.onBackPressed()
        }
    }
}