package com.iwelogic.minecraft.mods.ui.main.mods

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.databinding.FragmentModsBinding
import com.iwelogic.minecraft.mods.ui.base.BaseFragment
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
            if (findNavController().currentDestination?.id != R.id.detailsFragment) {
                parentFragment?.parentFragment?.findNavController()?.navigate(MainFragmentDirections.actionMainFragmentToDetailsFragment(it))
            }
        }
    }
}
