package com.iwelogic.minecraft.mods.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.databinding.FragmentDetailsBinding
import com.iwelogic.minecraft.mods.models.Category
import com.iwelogic.minecraft.mods.ui.base.BaseFragment
import com.iwelogic.minecraft.mods.ui.favorite.FavoriteFragmentDirections
import com.iwelogic.minecraft.mods.utils.AddHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : BaseFragment<DetailsViewModel>() {

    var adView: NativeAdView? = null
    var currentNativeAd: NativeAd? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentDetailsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this)[DetailsViewModel::class.java]
        viewModel.item.value = DetailsFragmentArgs.fromBundle(requireArguments()).data
        viewModel.checkIsFileExist()
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshAd(view)
        viewModel.openHelp.observe(viewLifecycleOwner) {
            if (findNavController().currentDestination?.id == R.id.detailsFragment) {
                findNavController().navigate(DetailsFragmentDirections.actionDetailsFragmentToHelpFragment())
            }
        }
    }

/*    override fun install(filepath: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(FileProvider.getUriForFile(requireContext(), requireContext().packageName + ".fileprovider", File(filepath)), "application/octet-stream")
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(intent)
            context?.writeBoolean(STATUS_MOD_INSTALLED, true)
        } catch (e: ActivityNotFoundException) {
            //     NoMinecraftDialog().show(childFragmentManager, "NoMinecraftDialog")
        }
    }

    override fun openHelp(screen: String) {
        catchAll {
            //    findNavController().navigate(DetailsFragmentDirections.actionGlobalHelpFragment(screen))
        }
    }*/

/*    override fun openNoConnection() {
        NoInternetActionDialog().apply {
            onClickRetry = {
                this@DetailsFragment.viewModel.download()
            }
        }.show(childFragmentManager, "NoInternetDialog")
    }*/

    private fun refreshAd(view: View) {
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

                val adLoader = builder.withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {

                    }

                    override fun onAdLoaded() {
                        super.onAdLoaded()
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

/*    override fun close() {
        showInterstitialAd {
            super.close()
        }
    }*/
}

