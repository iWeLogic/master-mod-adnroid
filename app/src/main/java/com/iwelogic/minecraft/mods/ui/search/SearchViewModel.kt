package com.iwelogic.minecraft.mods.ui.search

import android.content.Context
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.iwelogic.minecraft.mods.data.Repository
import com.iwelogic.minecraft.mods.data.Result
import com.iwelogic.minecraft.mods.models.Advertisement
import com.iwelogic.minecraft.mods.models.Mod
import com.iwelogic.minecraft.mods.models.Type
import com.iwelogic.minecraft.mods.ui.base.BaseViewModel
import com.iwelogic.minecraft.mods.ui.base.SingleLiveEvent
import com.iwelogic.minecraft.mods.utils.fromPxToDp
import com.iwelogic.minecraft.mods.utils.isTrue
import com.iwelogic.minecraft.mods.utils.logEvent
import com.iwelogic.minecraft.mods.utils.readBoolean
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: Repository,
    @ApplicationContext applicationContext: Context
) : BaseViewModel(applicationContext) {

    val title: MutableLiveData<String> = MutableLiveData("")
    val mods: MutableLiveData<MutableList<Mod>> = MutableLiveData(ArrayList())
    var type: MutableLiveData<Type> = MutableLiveData(Type.ADDONS)
    var query: MutableLiveData<String?> = MutableLiveData("")
    val spanCount: MutableLiveData<Int> = MutableLiveData(1)
    val openMod: SingleLiveEvent<Mod> = SingleLiveEvent()
    val openVoiceSearch: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val hideKeyboard: SingleLiveEvent<Boolean> = SingleLiveEvent()
    private var jobRefresh: Job? = null
    private var job: Job? = null
    var finished = false
    private val changeObserver: (Any) -> Unit = {
        onReload()
    }

    init {
        type.observeForever(changeObserver)
        query.observeForever {
            jobRefresh?.cancel()
            jobRefresh = viewModelScope.launch {
                delay(500)
                onReload()
            }
        }
    }

    var listener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(newText: String?): Boolean {
            if (!newText.isNullOrEmpty() && newText.length > 2) hideKeyboard.postValue(true)
            query.postValue(newText)
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            query.postValue(newText)
            return true
        }
    }

    fun onSelectCategory(type: Type) {
        this.type.postValue(type)
    }

    fun onClickMic() {
        openVoiceSearch.invoke(true)
    }

    val onScroll: (Int) -> Unit = {
        if ((mods.value?.size ?: 0) < it + 3)
            query.value?.let { query ->
                if (query.length > 2) load()
            }
    }

    val onClick: (Mod) -> Unit = {
        if (context.get()?.readBoolean(Advertisement.INTERSTITIAL_OPEN_DETAILS.id).isTrue()) {
            showInterstitial.invoke {
                context.get().logEvent(Advertisement.INTERSTITIAL_OPEN_DETAILS.id)
                openMod.invoke(it)
            }
        } else {
            openMod.invoke(it)
        }
    }

    private fun load() {
        viewModelScope.launch {
            mods.value = ArrayList()
            repository.getMods(type.value ?: Type.ADDONS, query.value ?: "", listOf())
                .collect { result ->
                    when (result) {
                        is Result.Loading -> progress.postValue(true)
                        is Result.Finish -> progress.postValue(false)
                        is Result.Success -> {
                            mods.value = result.data?.toMutableList()
                        }
                        is Result.Error -> error.postValue(result.message)
                    }
                }
        }
    }


    override fun onReload() {
        job?.cancel()
        // showProgressInList(false)
        progress.postValue(false)
        mods.value?.clear()
        mods.postValue(mods.value)
        finished = false
        query.value.let {
            if (it.isNullOrEmpty() || it.length < 3) {
                mods.postValue(mods.value)
            } else {
                load()
            }
        }
    }

    fun reloadScreenSize(widthDp: Int?) {
        widthDp?.fromPxToDp(context.get())?.let {
            spanCount.postValue(if (it > 680) 2 else 1)
        }
    }

    override fun onCleared() {
        super.onCleared()
        type.removeObserver(changeObserver)
    }
}
