package com.iwelogic.minecraft.mods.ui.detail_skin

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.databinding.FragmentDetailsSkinBinding
import com.iwelogic.minecraft.mods.models.DialogData
import com.iwelogic.minecraft.mods.ui.base.Const
import com.iwelogic.minecraft.mods.ui.base_details.BaseDetailsFragment
import com.iwelogic.minecraft.mods.utils.writeBoolean
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class DetailsSkinFragment : BaseDetailsFragment<DetailsSkinViewModel>() {

    private var requestPermissionStorage: ActivityResultLauncher<String>? = null
    private var permissionAction: (() -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentDetailsSkinBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_details_skin, container, false)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this)[DetailsSkinViewModel::class.java]
        viewModel.item.value = DetailsSkinFragmentArgs.fromBundle(requireArguments()).data
        viewModel.checkIsFileExist()
        binding.viewModel = viewModel
        return binding.root
    }

    private fun checkPermissionAction() {
        activity?.let {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(it, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                requestPermissionStorage?.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            } else {
                permissionAction?.invoke()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshAd(view)

        setFragmentResultListener("provide") { _, _ ->
            permissionAction?.let {
                checkPermissionAction()
            }
        }

        requestPermissionStorage = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                permissionAction?.invoke()
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    DialogData(
                        title = context?.getString(R.string.permission_one_title),
                        message = context?.getString(R.string.permission_one_body),
                        buttonRightTitle = context?.getString(R.string.permit),
                        buttonLeftTitle = context?.getString(R.string.no),
                        onClickRight = {
                            checkPermissionAction()
                        }
                    )
                } else {
                    DialogData(
                        title = context?.getString(R.string.permission_one_title),
                        message = context?.getString(R.string.permission_two_body),
                        buttonRightTitle = context?.getString(R.string.permit),
                        buttonLeftTitle = context?.getString(R.string.no),
                        onClickRight = {
                            startActivity(Intent(Settings.ACTION_SETTINGS))
                        }
                    )
                }
            }
        }

        viewModel.openHelp.observe(viewLifecycleOwner) {
            if (findNavController().currentDestination?.id == R.id.detailsFragment) {
                findNavController().navigate(DetailsSkinFragmentDirections.actionDetailsSkinFragmentToHelpFragment())
            }
        }

        viewModel.openInstall.observe(viewLifecycleOwner) {
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(FileProvider.getUriForFile(requireContext(), requireContext().packageName + ".fileprovider", File(it)), "application/octet-stream")
                intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivity(intent)
                context?.writeBoolean(Const.STATUS_MOD_INSTALLED, true)
            } catch (e: Exception) {
                viewModel.showDialogNeedInstallMinecraft()
            }
        }

        viewModel.openIntent.observe(viewLifecycleOwner) {
            startActivity(it)
        }

        viewModel.openCheckPermission.observe(viewLifecycleOwner) {
            permissionAction = it
            checkPermissionAction()
        }
    }
}
