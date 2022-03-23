package com.iwelogic.minecraft.mods.ui.base

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.iwelogic.minecraft.mods.ui.MainActivity

open class BaseFragment<VM : BaseViewModel> : Fragment() {

    lateinit var viewModel: VM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.close.observe(viewLifecycleOwner) {
            activity?.onBackPressed()
        }

        viewModel.showInterstitial.observe(viewLifecycleOwner) {
            Log.w("myLog", "onViewCreated: xx" )
            (activity as MainActivity).showInterstitialAd(it)
        }
    }
}