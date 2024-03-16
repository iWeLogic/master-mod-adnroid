package com.iwelogic.minecraft.mods.ui.main.mods

import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.databinding.FragmentModsBinding
import com.iwelogic.minecraft.mods.models.*
import com.iwelogic.minecraft.mods.ui.base.BaseFragment
import com.iwelogic.minecraft.mods.ui.base.Const.VALUE
import com.iwelogic.minecraft.mods.ui.main.MainFragmentDirections
import com.iwelogic.minecraft.mods.utils.parcelableArray
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ModsFragment : BaseFragment<ModsViewModel>() {

    @Inject
    lateinit var addonsViewModelFactory: AddonsViewModelFactory

    @Inject
    lateinit var mapsViewModelFactory: MapsViewModelFactory

    @Inject
    lateinit var texturesViewModelFactory: TexturesViewModelFactory

    @Inject
    lateinit var seedsViewModelFactory: SeedsViewModelFactory

    @Inject
    lateinit var skinsViewModelFactory: SkinsViewModelFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentModsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_mods, container, false)
        binding.lifecycleOwner = this
        val type = ModsFragmentArgs.fromBundle(requireArguments()).type
        viewModel = when (type) {
            Type.ADDONS.id -> ViewModelProvider(requireActivity(), AddonsViewModel.provideFactory(addonsViewModelFactory, Type.getValueById(type)))[AddonsViewModel::class.java]
            Type.MAPS.id -> ViewModelProvider(requireActivity(), MapsViewModel.provideFactory(mapsViewModelFactory, Type.getValueById(type)))[MapsViewModel::class.java]
            Type.TEXTURES.id -> ViewModelProvider(requireActivity(), TexturesViewModel.provideFactory(texturesViewModelFactory, Type.getValueById(type)))[TexturesViewModel::class.java]
            Type.SEEDS.id -> ViewModelProvider(requireActivity(), SeedsViewModel.provideFactory(seedsViewModelFactory, Type.getValueById(type)))[SeedsViewModel::class.java]
            else -> ViewModelProvider(requireActivity(), SkinsViewModel.provideFactory(skinsViewModelFactory, Type.getValueById(type)))[SkinsViewModel::class.java]
        }
        binding.viewModel = viewModel
        viewModel.reloadScreenSize(context?.resources?.displayMetrics?.widthPixels)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.openMod.observe(viewLifecycleOwner) {
            if (it.type == Type.SKINS) {
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
                parentFragment?.parentFragment?.findNavController()?.navigate(MainFragmentDirections.actionMainFragmentToFilterDialog("filter_" + viewModel.type.id, it.toTypedArray()))
            }
        }
        viewModel.openSettings.observe(viewLifecycleOwner) {
            if (parentFragment?.parentFragment?.findNavController()?.currentDestination?.id == R.id.mainFragment) {
                parentFragment?.parentFragment?.findNavController()?.navigate(MainFragmentDirections.actionMainFragmentToSettingsFragment())
            }
        }
        parentFragment?.parentFragment?.setFragmentResultListener("filter_" + viewModel.type.id) { _, bundle ->
            viewModel.setNewFilters(bundle.parcelableArray<FilterValue>(VALUE) ?: ArrayList())
        }
        viewModel.recyclerState?.let {
            view.findViewById<RecyclerView>(R.id.list)?.layoutManager?.onRestoreInstanceState(it)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        view?.findViewById<RecyclerView>(R.id.list)?.layoutManager?.onSaveInstanceState()?.let {
            viewModel.recyclerState = it
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        viewModel.reloadScreenSize(context?.resources?.displayMetrics?.widthPixels)
    }
}
