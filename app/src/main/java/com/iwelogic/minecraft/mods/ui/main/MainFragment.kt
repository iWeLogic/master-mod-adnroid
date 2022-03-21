package com.iwelogic.minecraft.mods.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.databinding.FragmentMainBinding
import com.iwelogic.minecraft.mods.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<MainViewModel>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentMainBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = (childFragmentManager.findFragmentById(R.id.bottomNavigationContainer) as NavHostFragment).navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            viewModel.title.postValue(
                when (destination.id) {
                    R.id.addonsFragment -> view.context.getString(R.string.addons)
                    R.id.mapsFragment -> view.context.getString(R.string.maps)
                    R.id.texturesFragment -> view.context.getString(R.string.textures)
                    else -> "asdas"
                }
            )
        }
        NavigationUI.setupWithNavController(view.findViewById<BottomNavigationView>(R.id.bottomNavigationView), navController)

    }
}
