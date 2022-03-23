package com.iwelogic.minecraft.mods.ui.privacy_policy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.databinding.FragmentPrivacyPolicyBinding
import com.iwelogic.minecraft.mods.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrivacyPolicyFragment : BaseFragment<PrivacyPolicyViewModel>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentPrivacyPolicyBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_privacy_policy, container, false)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this).get(PrivacyPolicyViewModel::class.java)
        binding.viewModel = viewModel
        return binding.root
    }
}
