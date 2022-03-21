package com.iwelogic.minecraft.mods.ui.base.storage.permission_two

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.databinding.DialogPermissionTwoBinding
import com.iwelogic.minecraft.mods.ui.base.BaseDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PermissionTwoDialog : BaseDialog<PermissionTwoViewModel>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: DialogPermissionTwoBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_permission_two, container, false)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this).get(PermissionTwoViewModel::class.java)
        binding.viewModel = viewModel
        binding.settings.setOnClickListener {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data =  Uri.fromParts("package", context?.packageName, null)
            startActivity(intent)
            dismiss()
        }
        return binding.root
    }
}