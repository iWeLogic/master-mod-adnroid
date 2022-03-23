package com.iwelogic.minecraft.mods.ui.base

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.ui.MainActivity

open class BaseFragment<VM : BaseViewModel> : Fragment() {

    lateinit var viewModel: VM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.close.observe(viewLifecycleOwner) {
            activity?.onBackPressed()
        }

        viewModel.showInterstitial.observe(viewLifecycleOwner) {
            Log.w("myLog", "onViewCreated: xx")
            (activity as MainActivity).showInterstitialAd(it)
        }

        viewModel.showDialog.observe(this) { dialogData ->
            context?.let {
                val dialog = AlertDialog.Builder(it, R.style.CustomAlertDialog).create()
                val dialogView = layoutInflater.inflate(R.layout.dialog_warning, null)
                dialogView.findViewById<TextView>(R.id.title).text = dialogData.title
                dialogView.findViewById<TextView>(R.id.message).text = dialogData.message
                val buttonLeft = dialogView.findViewById<TextView>(R.id.buttonLeft)
                val buttonRight = dialogView.findViewById<TextView>(R.id.buttonRight)
                if (dialogData.buttonLeftTitle.isNullOrEmpty()) buttonLeft.visibility = View.INVISIBLE
                if (dialogData.buttonRightTitle.isNullOrEmpty()) buttonRight.visibility = View.INVISIBLE
                buttonLeft.text = dialogData.buttonLeftTitle
                buttonRight.text = dialogData.buttonRightTitle
                dialog.setView(dialogView)
                buttonLeft.setOnClickListener {
                    dialog.dismiss()
                    dialogData.onClickLeft?.invoke()
                }
                buttonRight.setOnClickListener {
                    dialog.dismiss()
                    dialogData.onClickRight?.invoke()
                }
                dialog.setCanceledOnTouchOutside(false)
                dialog.show()
            }
        }
    }
}