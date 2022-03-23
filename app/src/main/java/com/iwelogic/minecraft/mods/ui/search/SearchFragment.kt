package com.iwelogic.minecraft.mods.ui.search

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.databinding.FragmentSearchBinding
import com.iwelogic.minecraft.mods.models.Category
import com.iwelogic.minecraft.mods.ui.base.BaseFragment
import com.iwelogic.minecraft.mods.ui.favorite.FavoriteFragmentDirections
import com.iwelogic.minecraft.mods.utils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class SearchFragment : BaseFragment<SearchViewModel>() {

    private var resultVoiceSearch: ActivityResultLauncher<Intent>? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentSearchBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        resultVoiceSearch = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.query.postValue(result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull())
            }
        }

        viewModel.openMod.observe(viewLifecycleOwner) {
            if (findNavController().currentDestination?.id == R.id.searchFragment) {
                if (it.category == Category.SKINS.id) {
                    findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToDetailsSkinFragment(it))
                } else {
                    findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToDetailsFragment(it))
                }
            }
        }
        viewModel.openVoiceSearch.observe(viewLifecycleOwner){
            try {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                resultVoiceSearch?.launch(intent)
            } catch (e: Exception) {
                Log.w("myLog", "onViewCreated: " + e.message)
                /*context?.let { context ->
                    MessageDialog().apply {
                        arguments = Bundle().apply {
                            putString("title", context.getString(R.string.voice_recognition_error_title))
                            putString("body", context.getString(R.string.voice_recognition_error_body))
                        }
                    }.show(childFragmentManager, "MessageDialog")
                }*/
            }
        }

        viewModel.hideKeyboard.observe(viewLifecycleOwner){
            activity?.hideKeyboard(true)
        }
    }
}
