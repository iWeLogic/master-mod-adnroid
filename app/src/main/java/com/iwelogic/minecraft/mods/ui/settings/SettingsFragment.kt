package com.iwelogic.minecraft.mods.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.ump.UserMessagingPlatform
import com.iwelogic.minecraft.mods.*
import com.iwelogic.minecraft.mods.databinding.FragmentSettingsBinding
import com.iwelogic.minecraft.mods.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingsFragment : BaseFragment<SettingsViewModel>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentSettingsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.share.observe(viewLifecycleOwner) {
            val sendIntent = Intent()
            sendIntent.setAction(Intent.ACTION_SEND)
            sendIntent.putExtra(
                Intent.EXTRA_TEXT,
                "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID
            )
            sendIntent.setType("text/plain")
            startActivity(sendIntent)
        }

        viewModel.rate.observe(viewLifecycleOwner) {
            val uri: Uri = Uri.parse("market://details?id=${BuildConfig.APPLICATION_ID}")
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            goToMarket.setPackage("com.android.vending")
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            runCatching {
                startActivity(goToMarket)
            }.onFailure {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}")))
            }
        }

        viewModel.openGDPR.observe(viewLifecycleOwner) {
            UserMessagingPlatform.showPrivacyOptionsForm(requireActivity()) { formError ->
                formError?.let {
                    // Handle the error.
                }
            }
        }
    }
}
