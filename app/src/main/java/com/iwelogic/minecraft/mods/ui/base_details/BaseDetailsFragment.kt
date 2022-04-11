package com.iwelogic.minecraft.mods.ui.base_details

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.google.firebase.analytics.FirebaseAnalytics
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.models.Advertisement
import com.iwelogic.minecraft.mods.ui.base.BaseFragment
import com.iwelogic.minecraft.mods.ui.details.DetailsFragmentDirections
import com.iwelogic.minecraft.mods.utils.AddHelper
import com.iwelogic.minecraft.mods.utils.isTrue
import com.iwelogic.minecraft.mods.utils.readBoolean

abstract class BaseDetailsFragment<VM : BaseDetailsViewModel> : BaseFragment<VM>() {

    private var adView: NativeAdView? = null
    private var currentNativeAd: NativeAd? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (context?.readBoolean(Advertisement.NATIVE_ON_DETAILS.id).isTrue()) {
            refreshAd(view)
        }
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
                            context?.let { FirebaseAnalytics.getInstance(it).logEvent("NativeAdShowAsPrevious", Bundle()) }
                            adView = layoutInflater.inflate(R.layout.layout_native_ad, view as FrameLayout, false) as NativeAdView
                            nativeAd.images.firstOrNull()?.let { image ->
                                adView?.findViewById<ImageView>(R.id.imageAd)?.setImageDrawable(image.drawable)
                            }
                        }
                        AddHelper.populateUnifiedNativeAdView(nativeAd, adView!!, activity?.resources?.configuration?.orientation)
                        adView?.parent?.let { parent ->
                            (parent as ViewGroup).removeAllViews()
                        }
                        view.findViewById<FrameLayout>(R.id.ad_frame)?.addView(adView)
                    }
                }
                val adLoader = builder.withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        super.onAdFailedToLoad(p0)
                        context?.let { FirebaseAnalytics.getInstance(it).logEvent("NativeAdFailedToLoad", Bundle()) }
                    }

                    override fun onAdLoaded() {
                        super.onAdLoaded()
                        context?.let { FirebaseAnalytics.getInstance(it).logEvent("NativeAdLoaded", Bundle()) }
                    }

                    override fun onAdImpression() {
                        super.onAdImpression()
                        context?.let { FirebaseAnalytics.getInstance(it).logEvent("NativeAdsShow", Bundle()) }
                    }
                }).build()
                adLoader.loadAd(AdRequest.Builder().build())
            }
        }
    }

    override fun onDestroy() {
        currentNativeAd?.destroy()
        super.onDestroy()
    }
}