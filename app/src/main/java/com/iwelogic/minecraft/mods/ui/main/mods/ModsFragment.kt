package com.iwelogic.minecraft.mods.ui.main.mods

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.databinding.FragmentModsBinding
import com.iwelogic.minecraft.mods.models.FilterValue
import com.iwelogic.minecraft.mods.ui.base.BaseFragment
import com.iwelogic.minecraft.mods.ui.base.CellType
import com.iwelogic.minecraft.mods.ui.main.MainFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ModsFragment : BaseFragment<ModsViewModel>() {

    @Inject
    lateinit var viewModelFactory: ModsViewModelFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentModsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_mods, container, false)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this, ModsViewModel.provideFactory(viewModelFactory, ModsFragmentArgs.fromBundle(requireArguments()).category))[ModsViewModel::class.java]
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.openMod.observe(viewLifecycleOwner) {
            if (it.cellType == CellType.SKINS) {
                if (parentFragment?.parentFragment?.findNavController()?.currentDestination?.id == R.id.mainFragment) {
                    parentFragment?.parentFragment?.findNavController()?.navigate(MainFragmentDirections.actionMainFragmentToDetailsSkinFragment(it))
                }
            } else {
                if (parentFragment?.parentFragment?.findNavController()?.currentDestination?.id == R.id.mainFragment) {
                    parentFragment?.parentFragment?.findNavController()?.navigate(MainFragmentDirections.actionMainFragmentToDetailsFragment(it))
                }
            }
        }
        viewModel.openFavorite.observe(viewLifecycleOwner) {
            if (parentFragment?.parentFragment?.findNavController()?.currentDestination?.id == R.id.mainFragment) {
                parentFragment?.parentFragment?.findNavController()?.navigate(MainFragmentDirections.actionMainFragmentToFavoriteFragment())
            }
        }
        viewModel.openSearch.observe(viewLifecycleOwner) {
            if (parentFragment?.parentFragment?.findNavController()?.currentDestination?.id == R.id.mainFragment) {
                parentFragment?.parentFragment?.findNavController()?.navigate(MainFragmentDirections.actionMainFragmentToSearchFragment())
            }
        }
        viewModel.openFilter.observe(viewLifecycleOwner) {
            if (parentFragment?.parentFragment?.findNavController()?.currentDestination?.id == R.id.mainFragment) {
                parentFragment?.parentFragment?.findNavController()?.navigate(MainFragmentDirections.actionMainFragmentToFilterDialog("filter_" + viewModel.category, it.toTypedArray()))
            }
        }
        parentFragment?.parentFragment?.setFragmentResultListener("filter_" + viewModel.category) { _, bundle ->
            viewModel.setNewFilters(bundle.getParcelableArray("value")?.map { it as FilterValue } ?: ArrayList())
        }
    }
}
