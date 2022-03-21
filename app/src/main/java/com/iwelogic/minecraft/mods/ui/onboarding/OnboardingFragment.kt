package com.iwelogic.minecraft.mods.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.databinding.FragmentOnboardingBinding
import com.iwelogic.minecraft.mods.ui.MainActivity
import com.iwelogic.minecraft.mods.ui.base.BaseFragment
import com.iwelogic.minecraft.mods.utils.openUrl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingFragment : BaseFragment<OnboardingModel>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentOnboardingBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_onboarding, container, false)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this)[OnboardingModel::class.java]
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val seekBar = view.findViewById<SeekBar>(R.id.seekbar)
        val left = view.findViewById<View>(R.id.left)
        val right = view.findViewById<View>(R.id.right)
        val value = view.findViewById<TextView>(R.id.value)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                value.text = if (progress == 63) "65+" else (progress + 3).toString()
                viewModel.age.postValue(progress + 3)
                left.layoutParams = LinearLayout.LayoutParams(0, 0, progress.toFloat())
                right.layoutParams = LinearLayout.LayoutParams(0, 0, 63f - progress.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        viewModel.openMain.observe(viewLifecycleOwner) {
            (activity as MainActivity).openMain()
        }

        viewModel.openUrl.observe(viewLifecycleOwner) {
            (activity as MainActivity).openUrl(it)
        }
    }
}
