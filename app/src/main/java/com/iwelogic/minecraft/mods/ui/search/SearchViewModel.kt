package com.iwelogic.minecraft.mods.ui.search

import android.util.Log
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.iwelogic.minecraft.mods.data.MultiMap
import com.iwelogic.minecraft.mods.data.Repository
import com.iwelogic.minecraft.mods.data.Result
import com.iwelogic.minecraft.mods.models.Category
import com.iwelogic.minecraft.mods.models.Mod
import com.iwelogic.minecraft.mods.ui.base.BaseViewModel
import com.iwelogic.minecraft.mods.ui.base.SingleLiveEvent
import com.iwelogic.minecraft.mods.ui.main.mods.ModsViewModel
import com.iwelogic.minecraft.mods.utils.isTrue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: Repository) : BaseViewModel() {

    val title: MutableLiveData<String> = MutableLiveData("")
    val mods: MutableLiveData<MutableList<Mod>> = MutableLiveData(ArrayList())
    var category: MutableLiveData<Category> = MutableLiveData(Category.ADDONS)
    var query: MutableLiveData<String> = MutableLiveData("")
    val openMod: SingleLiveEvent<Mod> = SingleLiveEvent()
    private var jobRefresh: Job? = null
    private var job: Job? = null
    var finished = false
    private val changeObserver: (Any) -> Unit = {
        reload()
    }

    init {
        category.observeForever(changeObserver)
        query.observeForever {
            jobRefresh?.cancel()
            jobRefresh = viewModelScope.launch {
                delay(500)
                reload()
            }
        }
    }

    var listener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(newText: String?): Boolean {
            if (!newText.isNullOrEmpty() && newText.length > 2) {
                //  navigator?.hideKeyboard()
            }
            query.postValue(newText)
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            query.postValue(newText)
            return true
        }
    }

    fun onSelectCategory(category: Category) {
        this.category.postValue(category)
    }

    fun onClickMic() {
        //navigator?.openVoiceSearchDialog()
    }

    val onScroll: (Int) -> Unit = {
        if ((mods.value?.size ?: 0) < it + 3)
            query.value?.let { query ->
                if (query.length > 2) load()
            }
    }

    val onClick: (Mod) -> Unit = {
        openMod.invoke(it)
    }

    private fun load() {
        if (!job?.isActive.isTrue() && mods.value?.none { it.category == ModsViewModel.ERROR || it.category == ModsViewModel.PROGRESS }.isTrue() && !finished) {
            job = viewModelScope.launch {
                val queries: MultiMap<String, Any> = MultiMap()
                queries["property"] = "id"
                queries["property"] = "installs"
                queries["property"] = "likes"
                queries["property"] = "title"
                queries["property"] = "description"
                queries["property"] = "fileSize"
                queries["property"] = "countImages"
                queries["property"] = "version"
                queries["property"] = "objectId"
                queries["pageSize"] = ModsViewModel.PAGE_SIZE
                queries["where"] = "title LIKE '%${query.value}%'"
                queries["offset"] = mods.value?.size ?: 0

                repository.getMods(category.value?.id ?: "", queries).catch {
                    Log.w("myLog", "load2: " + it.message)
                }.collect { result ->
                    when (result) {
                        is Result.Loading -> showProgress(true)
                        is Result.Finish -> showProgress(false)
                        is Result.Success -> {
                            val data = result.data?.toMutableList()?.onEach { it.category = category.value?.id } ?: ArrayList()
                            mods.value?.addAll(data)
                            mods.postValue(mods.value)
                            if (data.size < ModsViewModel.PAGE_SIZE) finished = true
                        }
                        is Result.Error -> {
                            Log.w("myLog", "load4: " + result.message)
                            /*  when (result.code) {
                              Result.Error.Code.NOT_CONFIRMED -> warning.postValue(result.message)
                              Result.Error.Code.WRONG_EMAIL_OR_PASSWORD -> passwordError.postValue(result.message)
                              else -> warning.postValue(result.message)
                          }*/
                        }
                    }
                }
            }
        }
    }

    private fun showProgress(status: Boolean) {
        if (status) mods.value?.add(Mod(category = ModsViewModel.PROGRESS))
        else mods.value?.removeAll { it.category == ModsViewModel.PROGRESS }
        mods.postValue(mods.value)
    }

    override fun reload() {
        job?.cancel()
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

    override fun onCleared() {
        super.onCleared()
        category.removeObserver(changeObserver)
    }
}
