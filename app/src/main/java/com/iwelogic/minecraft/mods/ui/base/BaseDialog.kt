package com.iwelogic.minecraft.mods.ui.base

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

open class BaseDialog<VM : BaseViewModel> : DialogFragment() {

    lateinit var viewModel: VM

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog?.window?.setBackgroundDrawable(null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.close.observe(viewLifecycleOwner) {
           dismiss()
        }
    }

    /*override fun showToast(msg: String?) {
        context?.let {
            Toast.makeText(it, msg, Toast.LENGTH_LONG).show()
        }
    }

    override fun showToast(msg: Int) {
        context?.let {
            Toast.makeText(it, msg, Toast.LENGTH_LONG).show()
        }
    }

    override fun showSnackBar(msg: String?, success: Boolean) {
        view?.let {
            val snackBar: Snackbar = Snackbar.make(it, msg ?: it.context.getString(R.string.something_went_wrong), Snackbar.LENGTH_LONG)
            val textView = snackBar.view.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
            textView.maxLines = 5
            textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
            // textView.typeface = ResourcesCompat.getFont(it.context, R.font.poppins_regular)
            snackBar.setTextColor(ContextCompat.getColor(it.context, R.color.white)).show()
        }
    }


    override fun showSnackBar(msg: Int, success: Boolean) {
        (showSnackBar(view?.context?.getString(msg), success))
    }

    override fun close() {
        dismiss()
    }

    override fun openLogin() {
        //   (activity as MainActivity).openLogin()
    }

    override fun openMain(isFirstLaunch: Boolean) {
        //    (activity as MainActivity).openMain(false, isFirstLaunch)
    }

    override fun showPopupWarning(message: String) {

    }

    override fun openOnboarding() {

    }

    override fun openNoConnection() {

    }

    override fun showMessageDialog(title: String?, body: String?) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.navigator = null
    }

    override fun showInterstitialAd(callback: (() -> Unit)?) {
        activity?.let {
            (it as MainActivity).showInterstitialAd(callback)
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        manager.beginTransaction().add(this, tag).commitAllowingStateLoss()
    }*/
}