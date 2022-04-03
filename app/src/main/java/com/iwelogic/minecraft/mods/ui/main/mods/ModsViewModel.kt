package com.iwelogic.minecraft.mods.ui.main.mods

import android.content.Context
import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.data.MultiMap
import com.iwelogic.minecraft.mods.data.Repository
import com.iwelogic.minecraft.mods.data.Result
import com.iwelogic.minecraft.mods.models.*
import com.iwelogic.minecraft.mods.ui.base.BaseViewModel
import com.iwelogic.minecraft.mods.ui.base.SingleLiveEvent
import com.iwelogic.minecraft.mods.utils.deepCopy
import com.iwelogic.minecraft.mods.utils.fromPxToDp
import com.iwelogic.minecraft.mods.utils.isTrue
import com.iwelogic.minecraft.mods.utils.readBoolean
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

open class ModsViewModel @AssistedInject constructor(@ApplicationContext applicationContext: Context, private val repository: Repository, @Assisted val type: Type) : BaseViewModel(applicationContext) {

    companion object {
        const val PAGE_SIZE = 32
    }

    private var job: Job? = null
    private val changeObserver: (Any) -> Unit = {
        onReload()
    }
    var recyclerState: Parcelable? = null
    val sort: MutableLiveData<Sort> = MutableLiveData(Sort.DATE)
    val mods: MutableLiveData<MutableList<Mod>> = MutableLiveData(ArrayList())
    val title: MutableLiveData<String> = MutableLiveData()
    val openMod: SingleLiveEvent<Mod> = SingleLiveEvent()
    val openSearch: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val openFavorite: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val openFilter: SingleLiveEvent<List<FilterValue>> = SingleLiveEvent()
    val spanCount: MutableLiveData<Int> = MutableLiveData(1)
    var finished = false
    private val filters: MutableLiveData<List<FilterValue>> = MutableLiveData(ArrayList())

    val onSelectSort: (Sort) -> Unit = {
        sort.postValue(it)
    }

    val onClick: (Mod) -> Unit = {
        showInterstitial.invoke {
            openMod.invoke(it)
        }
    }

    val onScroll: (Int) -> Unit = {
        if ((mods.value?.size ?: 0) < it + 5)
            load()
    }

    init {
        filters.value = Filter.getFiltersByCategory(type.id).map { FilterValue(it, true) }
        load()
        title.postValue(applicationContext.getString(type.title))
        sort.observeForever(changeObserver)
        filters.observeForever(changeObserver)
    }

    fun onClickFilter() {
        openFilter.invoke(filters.value.deepCopy() ?: ArrayList())
    }

    fun onClickSearch() {
        openSearch.invoke(true)
    }

    fun onClickFavorite() {
        openFavorite.invoke(true)
    }

    fun setNewFilters(newFilters: List<FilterValue>) {
        for (i in newFilters.indices) {
            if (newFilters[i].value != filters.value?.get(i)?.value) {
                filters.postValue(newFilters)
            }
        }
    }

    private fun load() {
        if (!job?.isActive.isTrue() && mods.value?.none { it.type == Type.PROGRESS }.isTrue() && !finished) {
            job = viewModelScope.launch {
                val queries: MultiMap<String, Any> = MultiMap()
                queries["property"] = "id"
                queries["property"] = "installs"
                queries["property"] = "likes"
                queries["property"] = "objectId"
                if (type != Type.SKINS) {
                    queries["property"] = "title"
                    queries["property"] = "description"
                    queries["property"] = "fileSize"
                    queries["property"] = "countImages"
                    queries["property"] = "version"
                }
                queries["pageSize"] = PAGE_SIZE
                queries["sortBy"] = sort.value?.query ?: ""
                queries["where"] = Filter.getQuery(filters.value)
                queries["offset"] = mods.value?.size ?: 0
                repository.getMods(type.id, queries).catch {
                    showProgressInList(false)
                    progress.postValue(false)
                    error.postValue(it.message)
                }.collect { result ->
                    when (result) {
                        is Result.Loading -> {
                            error.postValue(null)
                            if (mods.value.isNullOrEmpty()) progress.postValue(true)
                            else showProgressInList(true)
                        }
                        is Result.Finish -> {
                            showProgressInList(false)
                            progress.postValue(false)
                        }
                        is Result.Success -> {
                            val data = result.data?.toMutableList()?.onEach { it.type = type } ?: ArrayList()
                            if (context.get()?.readBoolean(Advertisement.BANNER_IN_LIST.id).isTrue() && !context.get()?.resources?.getBoolean(R.bool.isTablet).isTrue()) {
                                if (data.size > 4)
                                    data.add(4, Mod(id = (0..999999).random(), type = Type.AD))
                                if (data.size > 19)
                                    data.add(19, Mod(id = (0..999999).random(), type = Type.AD))
                            }
                            mods.value?.addAll(data)
                            mods.postValue(mods.value)
                            if (data.size < PAGE_SIZE) finished = true
                        }
                        is Result.Error -> error.postValue(result.message)
                    }
                }
            }
        }
    }

    private fun showProgressInList(status: Boolean) {
        if (status) mods.value?.add(Mod(type = Type.PROGRESS))
        else mods.value?.removeAll { it.type == Type.PROGRESS }
        mods.postValue(mods.value)
    }

    override fun onReload() {
        job?.cancel()
        showProgressInList(false)
        progress.postValue(false)
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

    fun reloadScreenSize(widthDp: Int?) {
        widthDp?.fromPxToDp(context.get())?.let {
            val span = if (it > 700) 2 else 1
            spanCount.postValue(span * type.spanCount)
        }
    }
}

class AddonsViewModel @AssistedInject constructor(@ApplicationContext applicationContext: Context, repository: Repository, @Assisted type: Type) : ModsViewModel(applicationContext, repository, type) {
    companion object {
        fun provideFactory(assistedFactory: AddonsViewModelFactory, type: Type): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return assistedFactory.create(type) as T
            }
        }
    }
}

class MapsViewModel @AssistedInject constructor(@ApplicationContext applicationContext: Context, repository: Repository, @Assisted type: Type) : ModsViewModel(applicationContext, repository, type) {
    companion object {
        fun provideFactory(assistedFactory: MapsViewModelFactory, type: Type): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return assistedFactory.create(type) as T
            }
        }
    }
}

class TexturesViewModel @AssistedInject constructor(@ApplicationContext applicationContext: Context, repository: Repository, @Assisted type: Type) : ModsViewModel(applicationContext, repository, type) {
    companion object {
        fun provideFactory(assistedFactory: TexturesViewModelFactory, type: Type): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return assistedFactory.create(type) as T
            }
        }
    }
}

class SeedsViewModel @AssistedInject constructor(@ApplicationContext applicationContext: Context, repository: Repository, @Assisted type: Type) : ModsViewModel(applicationContext, repository, type) {
    companion object {
        fun provideFactory(assistedFactory: SeedsViewModelFactory, type: Type): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return assistedFactory.create(type) as T
            }
        }
    }
}

class SkinsViewModel @AssistedInject constructor(@ApplicationContext applicationContext: Context, repository: Repository, @Assisted type: Type) : ModsViewModel(applicationContext, repository, type) {
    companion object {
        fun provideFactory(assistedFactory: SkinsViewModelFactory, type: Type): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return assistedFactory.create(type) as T
            }
        }
    }
}

@AssistedFactory
interface AddonsViewModelFactory {
    fun create(type: Type): AddonsViewModel
}

@AssistedFactory
interface MapsViewModelFactory {
    fun create(type: Type): MapsViewModel
}

@AssistedFactory
interface TexturesViewModelFactory {
    fun create(type: Type): TexturesViewModel
}

@AssistedFactory
interface SeedsViewModelFactory {
    fun create(type: Type): SeedsViewModel
}

@AssistedFactory
interface SkinsViewModelFactory {
    fun create(type: Type): SkinsViewModel
}



