package com.iwelogic.minecraft.mods.ui.base_details

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.*
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.manager.FirebaseConfigManager
import com.iwelogic.minecraft.mods.manager.ad.AdUnit
import com.iwelogic.minecraft.mods.ui.base.BaseFragment
import com.iwelogic.minecraft.mods.ui.details.DetailsFragmentDirections
import com.iwelogic.minecraft.mods.utils.*
import javax.inject.Inject

abstract class BaseDetailsFragment<VM : BaseDetailsViewModel> : BaseFragment<VM>() {

    private var adView: NativeAdView? = null
    private var currentNativeAd: NativeAd? = null
    @Inject
    lateinit var firebaseConfigManager: FirebaseConfigManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (firebaseConfigManager.getAdUnitStatus(AdUnit.OPEN_DETAILS)) {
            refreshAd(view)
        }
        viewModel.openHelp.observe(viewLifecycleOwner) {
            if (findNavController().currentDestination?.id == R.id.detailsFragment) {
                findNavController().navigate(DetailsFragmentDirections.actionDetailsFragmentToHelpFragment())
            }
        }
    }

    private fun refreshAd(view: View) {
        Log.w("myLog", "refreshAd: 1")
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
                            nativeAd.images.firstOrNull()?.let { image ->
                                adView?.findViewById<ImageView>(R.id.imageAd)?.setImageDrawable(image.drawable)
                            }
                        }
                        AddHelper.populateUnifiedNativeAdView(nativeAd, adView!!, activity?.resources?.configuration?.orientation)
                        adView?.parent?.let { parent ->
                            (parent as ViewGroup).removeAllViews()
                        }
                        Log.w("myLog", "refreshAd: ")
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