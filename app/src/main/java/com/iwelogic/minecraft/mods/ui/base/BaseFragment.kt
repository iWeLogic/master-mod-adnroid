package com.iwelogic.minecraft.mods.ui.base

import android.app.AlertDialog
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.androidadvance.topsnackbar.TSnackbar
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.manager.AdManager
import javax.inject.Inject

open class BaseFragment<VM : BaseViewModel> : Fragment() {

    lateinit var viewModel: VM

    @Inject
    lateinit var adManager: AdManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.close.observe(viewLifecycleOwner) {
            activity?.onBackPressed()
        }

        viewModel.showInterstitial.observe(viewLifecycleOwner) {
            adManager.showInterstitialAd(it)
        }

        viewModel.showSnackBar.observe(viewLifecycleOwner) { msg ->
            val snackBar: TSnackbar = TSnackbar.make(view, msg, TSnackbar.LENGTH_LONG)
            snackBar.view.setBackgroundResource(R.color.green)
            val textView = snackBar.view.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
            textView.maxLines = 5
            textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
            textView.typeface = ResourcesCompat.getFont(view.context, R.font.minecraft_regular)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
            snackBar.view.findViewById<TextView>(com.androidadvance.topsnackbar.R.id.snackbar_text).setTextColor(ContextCompat.getColor(view.context, R.color.white))
            snackBar.show()
        }

        viewModel.showDialog.observe(viewLifecycleOwner) { dialogData ->
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