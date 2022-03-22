package com.iwelogic.minecraft.mods.ui.search

import android.content.Context
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import  com.iwelogic.minecraft.mods.data.Repository
import com.iwelogic.minecraft.mods.models.Category
import com.iwelogic.minecraft.mods.models.Mod
import  com.iwelogic.minecraft.mods.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: Repository) : BaseViewModel() {

    val title: MutableLiveData<String> = MutableLiveData("")
    var page: Int = 0
    val items: MutableLiveData<MutableList<Mod>> = MutableLiveData(ArrayList())
    var category: MutableLiveData<Category> = MutableLiveData()
    var query: MutableLiveData<String> = MutableLiveData("")
    private var jobRefresh: Job? = null
    private var job: Job? = null

    init {
        category.observeForever {
            reload()
        }
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
        if ((items.value?.size ?: 0) < it + 3)
            query.value?.let { query ->
                if (query.length > 2) load()
            }
    }

    val onClick: (Mod) -> Unit = {
    }

    override fun reload() {
        job?.cancel()
        page = 0
        items.value?.clear()
        query.value.let {
            if (it.isNullOrEmpty() || it.length < 3) {
                items.postValue(items.value)
            } else {
                load()
            }
        }
    }

    private fun load() {
   /*     if (items.value?.none { it.id == Const.ERROR || it.id == Const.PROGRESS }.isTrue() && page != -1) {
            page++
            job = viewModelScope.launch {

            }
        }*/
    }

    private fun showProgress(status: Boolean) {
        /*if (status) items.value?.add(BaseItem(id = Const.PROGRESS))
        else items.value?.removeAll { it.id == Const.PROGRESS }
        items.postValue(items.value)*/
    }
}
