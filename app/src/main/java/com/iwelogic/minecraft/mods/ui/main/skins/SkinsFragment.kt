package com.iwelogic.minecraft.mods.ui.main.skins

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.databinding.FragmentSkinsBinding
import com.iwelogic.minecraft.mods.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SkinsFragment : BaseFragment<SkinsViewModel>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentSkinsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_skins, container, false)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this)[SkinsViewModel::class.java]
        binding.viewModel = viewModel
        return binding.root
    }
}
