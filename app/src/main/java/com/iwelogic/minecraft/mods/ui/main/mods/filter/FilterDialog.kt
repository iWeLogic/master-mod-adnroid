package com.iwelogic.minecraft.mods.ui.main.mods.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.databinding.DialogFilterBinding
import com.iwelogic.minecraft.mods.ui.base.BaseDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterDialog : BaseDialog<FilterViewModel>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: DialogFilterBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_filter, container, false)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this).get(FilterViewModel::class.java)
        binding.viewModel = viewModel
        return binding.root
    }
}