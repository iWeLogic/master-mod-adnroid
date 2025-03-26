package com.iwelogic.minecraft.mods.ui.search

import android.app.*
import android.content.*
import android.content.res.*
import android.os.*
import android.speech.*
import android.view.*
import android.widget.*
import androidx.activity.result.*
import androidx.activity.result.contract.*
import androidx.appcompat.widget.SearchView
import androidx.databinding.*
import androidx.lifecycle.*
import androidx.navigation.fragment.*
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.databinding.*
import com.iwelogic.minecraft.mods.models.*
import com.iwelogic.minecraft.mods.ui.base.*
import com.iwelogic.minecraft.mods.utils.*
import dagger.hilt.android.*
import java.util.*


@AndroidEntryPoint
class SearchFragment : BaseFragment<SearchViewModel>() {

    private var resultVoiceSearch: ActivityResultLauncher<Intent>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentSearchBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        viewModel.reloadScreenSize(context?.resources?.displayMetrics?.widthPixels)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchView = view.findViewById<SearchView>(R.id.search)
        searchView.setIconifiedByDefault(false)
        val icon = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_mag_icon)
        icon.layoutParams = LinearLayout.LayoutParams(0, 0)

        resultVoiceSearch = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.query.postValue(result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull())
            }
        }

        viewModel.openMod.observe(viewLifecycleOwner) {
            if (findNavController().currentDestination?.id == R.id.searchFragment) {
                if (it.type == Type.SKINS) {
                    findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToDetailsSkinFragment(it))
                } else {
                    findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToDetailsFragment(it))
                }
            }
        }
        viewModel.openVoiceSearch.observe(viewLifecycleOwner) {
            try {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                resultVoiceSearch?.launch(intent)
            } catch (e: Exception) {
                context?.let { context ->
                    viewModel.showDialog.invoke(
                        DialogData(
                            title = context.getString(R.string.voice_recognition_error_title),
                            message = context.getString(R.string.voice_recognition_error_body),
                            buttonRightTitle = context.getString(R.string.ok)
                        )
                    )
                }
            }
        }

        viewModel.hideKeyboard.observe(viewLifecycleOwner) {
            activity?.hideKeyboard(true)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        viewModel.reloadScreenSize(context?.resources?.displayMetrics?.widthPixels)
    }
}
