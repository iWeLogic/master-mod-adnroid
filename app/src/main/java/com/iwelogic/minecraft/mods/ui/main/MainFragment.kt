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
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.itemIconTintList = null
        val navController = (childFragmentManager.findFragmentById(R.id.bottomNavigationContainer) as NavHostFragment).navController
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
    }


    /*fun showRatingDialog() {

        activity?.let { activity ->
            val manager = ReviewManagerFactory.create(activity)
            val request = manager.requestReviewFlow()
            request.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val reviewInfo = task.result
                    manager.launchReviewFlow(activity, reviewInfo)
                }
            }
        }
    }*/
}
