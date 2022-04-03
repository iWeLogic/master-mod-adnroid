package com.iwelogic.minecraft.mods.ui.favorite

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.databinding.FragmentFavoriteBinding
import com.iwelogic.minecraft.mods.ui.base.BaseFragment
import com.iwelogic.minecraft.mods.models.Type
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : BaseFragment<FavoriteViewModel>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentFavoriteBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite, container, false)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]
        binding.viewModel = viewModel
        viewModel.load()
        viewModel.reloadScreenSize(context?.resources?.displayMetrics?.widthPixels)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.openMod.observe(viewLifecycleOwner) {
            if (it.type == Type.SKINS) {
                if (findNavController().currentDestination?.id == R.id.favoriteFragment) {
                    findNavController().navigate(FavoriteFragmentDirections.actionFavoriteFragmentToDetailsSkinFragment(it))
                }
            } else {
                if (findNavController().currentDestination?.id == R.id.favoriteFragment) {
                    findNavController().navigate(FavoriteFragmentDirections.actionFavoriteFragmentToDetailsFragment(it))
                }
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        viewModel.reloadScreenSize(context?.resources?.displayMetrics?.widthPixels)
    }
}
