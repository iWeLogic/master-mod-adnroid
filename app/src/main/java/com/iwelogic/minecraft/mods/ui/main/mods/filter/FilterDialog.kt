package com.iwelogic.minecraft.mods.ui.main.mods.filter

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.databinding.DialogFilterBinding
import com.iwelogic.minecraft.mods.ui.base.BaseDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterDialog : BaseDialog<FilterViewModel>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: DialogFilterBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_filter, container, false)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this)[FilterViewModel::class.java]
        viewModel.reloadScreenSize(context?.resources?.displayMetrics?.widthPixels)
        viewModel.items.postValue(FilterDialogArgs.fromBundle(requireArguments()).data.toList())
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.apply.observe(viewLifecycleOwner) {
            setFragmentResult(FilterDialogArgs.fromBundle(requireArguments()).key, Bundle().apply { putParcelableArray("value", it.toTypedArray()) })
            dismiss()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        viewModel.reloadScreenSize(context?.resources?.displayMetrics?.widthPixels)
    }
}