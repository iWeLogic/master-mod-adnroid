package com.iwelogic.minecraft.mods.ui.detail_skin

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.databinding.FragmentDetailsSkinBinding
import com.iwelogic.minecraft.mods.ui.base.Const
import com.iwelogic.minecraft.mods.ui.base_details.BaseDetailsFragment
import com.iwelogic.minecraft.mods.ui.base_details.permission.PermissionDialog
import com.iwelogic.minecraft.mods.ui.base_details.permission_two.PermissionTwoDialog
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
        viewModel = ViewModelProvider(this).get(DetailsSkinViewModel::class.java)
        viewModel.item.value = DetailsSkinFragmentArgs.fromBundle(requireArguments()).data
        Log.w("myLog", "onCreateView: " + viewModel.item.value)
        viewModel.checkIsFileExist()
        binding.viewModel = viewModel
        return binding.root
    }

    /*  override fun checkPermissionAction(action: () -> Unit) {
       permissionAction = action
       activity?.let {
           if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(it, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
               requestPermissionStorage?.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
           } else {
               action.invoke()
           }
       }
   }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshAd(view)

        setFragmentResultListener("provide") { _, _ ->
            permissionAction?.let {
                //    checkPermissionAction(it)
            }
        }

        requestPermissionStorage = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                permissionAction?.invoke()
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    PermissionDialog().show(childFragmentManager, "PermissionDialog")
                } else {
                    PermissionTwoDialog().show(childFragmentManager, "PermissionTwoDialog")
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
             //   NoMinecraftDialog().show(childFragmentManager, "NoMinecraftDialog")
            }
        }

        viewModel.openInstallMinecraft.observe(viewLifecycleOwner) {
        //    NoMinecraftDialog().show(childFragmentManager, "NoMinecraftDialog")
        }

        viewModel.openMessageDialog.observe(viewLifecycleOwner) {
            /*MessageDialog().apply {
                arguments = Bundle().apply {
                    putString("title", title)
                    putString("body", body)
                }
            }.show(childFragmentManager, "MessageDialog")*/
        }

        viewModel.openCheckPermission.observe(viewLifecycleOwner) {

        }
    }
}
