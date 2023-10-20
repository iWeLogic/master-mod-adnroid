package com.iwelogic.minecraft.mods.ui.rating

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.iwelogic.minecraft.mods.*
import com.iwelogic.minecraft.mods.databinding.DialogRatingBinding
import com.iwelogic.minecraft.mods.ui.base.BaseDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RatingDialog : BaseDialog<RatingViewModel>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: DialogRatingBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_rating, container, false)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this)[RatingViewModel::class.java]
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.showPlayMarketRating.observe(viewLifecycleOwner) {
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
    }
}
