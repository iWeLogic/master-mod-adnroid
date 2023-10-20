package com.iwelogic.minecraft.mods.ui.rating

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.databinding.DialogRatingBinding
import com.iwelogic.minecraft.mods.ui.base.BaseDialog
import dagger.hilt.android.AndroidEntryPoint

const val OPEN_REVIEW_KEY = "OPEN_REVIEW_KEY"

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
            setFragmentResult(OPEN_REVIEW_KEY, Bundle())
        }
    }
}
