package com.iwelogic.minecraft.mods.ui.main.mods

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.databinding.FragmentModsBinding
import com.iwelogic.minecraft.mods.ui.base.BaseFragment
import com.iwelogic.minecraft.mods.ui.main.MainFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ModsFragment : BaseFragment<ModsViewModel>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.w("myLog", "onCreateView: MODS")
        val binding: FragmentModsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_mods, container, false)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this)[ModsViewModel::class.java]
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.openMod.observe(viewLifecycleOwner){
            if(findNavController().currentDestination?.id != R.id.detailsFragment){
                parentFragment?.parentFragment?.findNavController()?.navigate(MainFragmentDirections.actionMainFragmentToDetailsFragment(it))
            }
        }
    }
}
