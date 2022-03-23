package com.iwelogic.minecraft.mods.ui.base_details

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.ui.base.BaseFragment
import com.iwelogic.minecraft.mods.ui.details.DetailsFragmentDirections
import com.iwelogic.minecraft.mods.utils.AddHelper


abstract class BaseDetailsFragment<VM : BaseDetailsViewModel> : BaseFragment<VM>() {

    var adView: NativeAdView? = null
    var currentNativeAd: NativeAd? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshAd(view)
        viewModel.openHelp.observe(viewLifecycleOwner) {
            if (findNavController().currentDestination?.id == R.id.detailsFragment) {
                findNavController().navigate(DetailsFragmentDirections.actionDetailsFragmentToHelpFragment())
            }
        }
    }

    protected fun refreshAd(view: View) {
        adView?.parent?.let {
            (it as ViewGroup).removeAllViews()
            view.findViewById<FrameLayout>(R.id.ad_frame)?.addView(adView)
        } ?: run {
            context?.let {
                val builder = AdLoader.Builder(it, it.getString(R.string.ad_native))
                builder.forNativeAd { nativeAd ->
                    activity?.let {
                        if (requireActivity().isDestroyed || requireActivity().isFinishing || requireActivity().isChangingConfigurations) {
                            nativeAd.destroy()
                            return@forNativeAd
                        }
                        currentNativeAd?.destroy()
                        currentNativeAd = nativeAd
                        if (adView == null) {
                            adView = layoutInflater.inflate(R.layout.layout_native_ad, view as FrameLayout, false) as NativeAdView
                            nativeAd.images.firstOrNull()?.let {
                                adView?.findViewById<ImageView>(R.id.imageAd)?.setImageDrawable(it.drawable)
                            }
                        }
                        AddHelper.populateUnifiedNativeAdView(nativeAd, adView!!)
                        adView?.parent?.let { parent ->
                            (parent as ViewGroup).removeAllViews()
                        }
                        view.findViewById<FrameLayout>(R.id.ad_frame)?.addView(adView)
                    }
                }

                builder.build().loadAd(AdRequest.Builder().build())
            }
        }
    }

    override fun onDestroy() {
        currentNativeAd?.destroy()
        super.onDestroy()
    }
}