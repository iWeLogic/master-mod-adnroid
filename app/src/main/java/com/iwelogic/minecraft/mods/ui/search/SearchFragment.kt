package com.iwelogic.minecraft.mods.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.databinding.FragmentSearchBinding
import com.iwelogic.minecraft.mods.models.Category
import com.iwelogic.minecraft.mods.ui.base.BaseFragment
import com.iwelogic.minecraft.mods.ui.favorite.FavoriteFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment<SearchViewModel>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentSearchBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.openMod.observe(viewLifecycleOwner) {
            if (it.category == Category.SKINS.id) {
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
}
