package com.iwelogic.minecraft.mods.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.databinding.FragmentOnboardingBinding
import com.iwelogic.minecraft.mods.ui.MainActivity
import com.iwelogic.minecraft.mods.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingFragment : BaseFragment<OnboardingViewModel>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentOnboardingBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_onboarding, container, false)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this)[OnboardingViewModel::class.java]
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.subscribeOnAgeChanges()
        viewModel.openMain.observe(viewLifecycleOwner) {
            (activity as MainActivity).openMain()
        }

        viewModel.openUrl.observe(viewLifecycleOwner) {
            if (findNavController().currentDestination?.id == R.id.onboardingFragment) {
                findNavController().navigate(OnboardingFragmentDirections.actionOnboardingFragmentToPrivacyPolicyFragment())
            }
        }
    }
}
