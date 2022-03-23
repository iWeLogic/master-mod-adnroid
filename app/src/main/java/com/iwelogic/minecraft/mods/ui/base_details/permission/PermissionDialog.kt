package com.iwelogic.minecraft.mods.ui.base_details.permission

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.databinding.DialogPermissionBinding
import com.iwelogic.minecraft.mods.ui.base.BaseDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PermissionDialog : BaseDialog<PermissionViewModel>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: DialogPermissionBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_permission, container, false)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this).get(PermissionViewModel::class.java)
        binding.viewModel = viewModel
        binding.provide.setOnClickListener {
            parentFragment?.setFragmentResult("provide", Bundle())
            dismiss()
        }
        return binding.root
    }
}