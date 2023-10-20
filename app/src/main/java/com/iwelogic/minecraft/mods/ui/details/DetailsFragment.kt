package com.iwelogic.minecraft.mods.ui.details

import android.content.*
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.databinding.FragmentDetailsBinding
import com.iwelogic.minecraft.mods.models.DialogData
import com.iwelogic.minecraft.mods.ui.base_details.BaseDetailsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : BaseDetailsFragment<DetailsViewModel>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentDetailsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this)[DetailsViewModel::class.java]
        viewModel.item.value = DetailsFragmentArgs.fromBundle(requireArguments()).data
        viewModel.checkIsFileExist()
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.install.observe(viewLifecycleOwner) { file ->
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(FileProvider.getUriForFile(requireContext(), requireContext().packageName + ".fileprovider", file), "application/octet-stream")
                intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                context?.let { context ->
                    viewModel.showDialog.invoke(
                        DialogData(
                            title = context.getString(R.string.minecraft_isnt_installed_title),
                            message = context.getString(R.string.minecraft_isnt_installed_body),
                            buttonRightTitle = context.getString(R.string.install),
                            buttonLeftTitle = context.getString(R.string.no),
                            onClickRight = {
                                try {
                                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.mojang.minecraftpe")))
                                } catch (e: ActivityNotFoundException) {
                                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.mojang.minecraftpe")))
                                }
                            }
                        )
                    )
                }
            }
        }

        viewModel.showRating.observe(viewLifecycleOwner) {
            runCatching {
                findNavController().navigate(DetailsFragmentDirections.actionDetailsFragmentToRatingDialog())
            }
        }
    }
}

