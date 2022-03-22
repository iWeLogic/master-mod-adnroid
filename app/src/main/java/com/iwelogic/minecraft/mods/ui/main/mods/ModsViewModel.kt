package com.iwelogic.minecraft.mods.ui.main.mods

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.data.MultiMap
import com.iwelogic.minecraft.mods.data.Repository
import com.iwelogic.minecraft.mods.data.Result
import com.iwelogic.minecraft.mods.models.Filter
import com.iwelogic.minecraft.mods.models.FilterValue
import com.iwelogic.minecraft.mods.models.Mod
import com.iwelogic.minecraft.mods.models.Sort
import com.iwelogic.minecraft.mods.ui.base.BaseViewModel
import com.iwelogic.minecraft.mods.ui.base.SingleLiveEvent
import com.iwelogic.minecraft.mods.utils.isTrue
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ModsViewModel @AssistedInject constructor(@ApplicationContext context: Context, private val repository: Repository, @Assisted val category: String) : BaseViewModel() {

    companion object {
        fun provideFactory(assistedFactory: ModsViewModelFactory, category: String): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return assistedFactory.create(category) as T
            }
        }

        const val PROGRESS = "progress"
        const val ERROR = "error"
        const val PAGE_SIZE = 30
    }

    private var job: Job? = null
    private val changeObserver: (Any) -> Unit = {
        reload()
    }
    val sort: MutableLiveData<Sort> = MutableLiveData(Sort.DEFAULT)
    val mods: MutableLiveData<MutableList<Mod>> = MutableLiveData(ArrayList())
    val title: MutableLiveData<String> = MutableLiveData()
    val openMod: SingleLiveEvent<Mod> = SingleLiveEvent()
    val openSearch: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val openFavorite: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val openFilter: SingleLiveEvent<List<FilterValue>> = SingleLiveEvent()
    val spanCount: MutableLiveData<Int> = MutableLiveData(1)
    var finished = false
    val filters: MutableLiveData<List<FilterValue>> = MutableLiveData(ArrayList())

    val onSelectSort: (Sort) -> Unit = {
        sort.postValue(it)
    }

    val onClick: (Mod) -> Unit = {
        openMod.invoke(it)
    }

    val onScroll: (Int) -> Unit = {
        if ((mods.value?.size ?: 0) < it + 5)
            load()
    }

    init {
        filters.value = Filter.getFiltersByCategory(category).map { FilterValue(it, true) }
        load()
        spanCount.postValue(if (category == "skins") 2 else 1)
        title.postValue(
            when (category) {
                "addons" -> context.getString(R.string.addons)
                "maps" -> context.getString(R.string.maps)
                "textures" -> context.getString(R.string.textures)
                "seeds" -> context.getString(R.string.seeds)
                else -> context.getString(R.string.skins)
            }
        )
        sort.observeForever(changeObserver)
        filters.observeForever(changeObserver)
    }

    fun onClickFilter() {
        openFilter.invoke(filters.value ?: ArrayList())
    }

    fun onClickSearch() {
        openSearch.invoke(true)
    }

    fun onClickFavorite() {
        openFavorite.invoke(true)
    }

    private fun load() {
        if (!job?.isActive.isTrue() && mods.value?.none { it.category == ERROR || it.category == PROGRESS }.isTrue() && !finished) {
            job = viewModelScope.launch {
                val queries: MultiMap<String, Any> = MultiMap()
                queries["property"] = "id"
                queries["property"] = "installs"
                queries["property"] = "likes"
                if (category != "skins") {
                    queries["property"] = "title"
                    queries["property"] = "description"
                    queries["property"] = "fileSize"
                    queries["property"] = "countImages"
                    queries["property"] = "version"
                    queries["property"] = "objectId"
                }
                queries["pageSize"] = PAGE_SIZE
                queries["sortBy"] = sort.value?.query ?: ""
                queries["where"] = Filter.getQuery(filters.value)
                queries["offset"] = mods.value?.size ?: 0

                repository.getMods(category, queries).catch {
                    Log.w("myLog", "load2: " + it.message)
                }.collect { result ->
                    when (result) {
                        is Result.Loading -> showProgress(true)
                        is Result.Finish -> showProgress(false)
                        is Result.Success -> {
                            val data = result.data?.toMutableList()?.onEach { it.category = category } ?: ArrayList()
                            mods.value?.addAll(data)
                            mods.postValue(mods.value)
                            if (data.size < PAGE_SIZE) finished = true
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
        if (status) mods.value?.add(Mod(category = PROGRESS))
        else mods.value?.removeAll { it.category == PROGRESS }
        mods.postValue(mods.value)
    }

    override fun reload() {
        job?.cancel()
        mods.value?.clear()
        mods.postValue(mods.value)
        finished = false
        load()
    }

    override fun onCleared() {
        super.onCleared()
        sort.removeObserver(changeObserver)
        filters.removeObserver(changeObserver)
    }
}

@AssistedFactory
interface ModsViewModelFactory {
    fun create(type: String): ModsViewModel
}