package com.iwelogic.minecraft.mods.ui.base.storage

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResultListener
import com.iwelogic.minecraft.mods.ui.base.BaseFragment
import com.iwelogic.minecraft.mods.ui.base.storage.permission.PermissionDialog
import com.iwelogic.minecraft.mods.ui.base.storage.permission_two.PermissionTwoDialog


abstract class StorageFragment<VM : StorageViewModel> : BaseFragment<VM>() {

    private var requestPermissionStorage: ActivityResultLauncher<String>? = null
    private var permissionAction: (() -> Unit)? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
}