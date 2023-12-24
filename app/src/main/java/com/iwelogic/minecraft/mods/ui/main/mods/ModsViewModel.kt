package com.iwelogic.minecraft.mods.ui.main.mods

import android.content.Context
import android.os.Parcelable
import androidx.lifecycle.*
import com.iwelogic.minecraft.mods.data.*
import com.iwelogic.minecraft.mods.models.*
import com.iwelogic.minecraft.mods.ui.base.*
import com.iwelogic.minecraft.mods.utils.*
import dagger.assisted.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch

open class ModsViewModel @AssistedInject constructor(
    @ApplicationContext applicationContext: Context,
    private val repository: Repository,
    @Assisted val type: Type
) : BaseViewModel(applicationContext) {

    private val changeObserver: (Any) -> Unit = {
        onReload()
    }
    var recyclerState: Parcelable? = null
    val sort: MutableLiveData<Sort> = MutableLiveData(Sort.RANDOM)
    val mods: MutableLiveData<MutableList<Mod>> = MutableLiveData(ArrayList())
    val title: MutableLiveData<String> = MutableLiveData()
    val openMod: SingleLiveEvent<Mod> = SingleLiveEvent()
    val openSearch: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val openFavorite: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val openFilter: SingleLiveEvent<List<FilterValue>> = SingleLiveEvent()
    val openSettings: SingleLiveEvent<Unit> = SingleLiveEvent()
    val spanCount: MutableLiveData<Int> = MutableLiveData(1)
    var finished = false
    private val filters: MutableLiveData<List<FilterValue>> = MutableLiveData(ArrayList())

    val onSelectSort: (Sort) -> Unit = {
        sort.value = it
        load()
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

    init {
        filters.value = Filter.getFiltersByCategory(type.id).map { FilterValue(it, true) }
        title.postValue(applicationContext.getString(type.title))
        sort.observeForever(changeObserver)
        filters.observeForever(changeObserver)
        load()
    }

    fun load(){
        viewModelScope.launch {
            mods.value = ArrayList()
            repository.getMods(type, sort.value ?: Sort.RANDOM, filters.value ?: listOf()).collect { result ->
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

    fun onClickFilter() {
        openFilter.invoke(filters.value.deepCopy() ?: ArrayList())
    }

    fun onClickSettings() {
        openSettings.invoke(Unit)
    }

    fun onClickSearch() {
        if (context.get()?.readBoolean(Advertisement.INTERSTITIAL_OPEN_SEARCH.id).isTrue()) {
            showInterstitial.invoke {
                context.get().logEvent(Advertisement.INTERSTITIAL_OPEN_SEARCH.id)
                openSearch.invoke(true)
            }
        } else {
            openSearch.invoke(true)
        }
    }

    fun onClickFavorite() {
        if (context.get()?.readBoolean(Advertisement.INTERSTITIAL_OPEN_FAVORITE.id).isTrue()) {
            showInterstitial.invoke {
                context.get().logEvent(Advertisement.INTERSTITIAL_OPEN_FAVORITE.id)
                openFavorite.invoke(true)
            }
        } else {
            openFavorite.invoke(true)
        }
    }

    fun setNewFilters(newFilters: List<FilterValue>) {
        for (i in newFilters.indices) {
            if (newFilters[i].value != filters.value?.get(i)?.value) {
                filters.value = newFilters
                load()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        sort.removeObserver(changeObserver)
        filters.removeObserver(changeObserver)
    }

    fun reloadScreenSize(widthDp: Int?) {
        widthDp?.fromPxToDp(context.get())?.let {
            val span = if (it > 680) 2 else 1
            spanCount.postValue(span * type.spanCount)
        }
    }
}

class AddonsViewModel @AssistedInject constructor(
    @ApplicationContext applicationContext: Context,
    repository: Repository,
    @Assisted type: Type
) : ModsViewModel(applicationContext, repository, type) {
    companion object {
        fun provideFactory(
            assistedFactory: AddonsViewModelFactory,
            type: Type
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return assistedFactory.create(type) as T
            }
        }
    }
}

class MapsViewModel @AssistedInject constructor(
    @ApplicationContext applicationContext: Context,
    repository: Repository,
    @Assisted type: Type
) : ModsViewModel(applicationContext, repository, type) {
    companion object {
        fun provideFactory(
            assistedFactory: MapsViewModelFactory,
            type: Type
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return assistedFactory.create(type) as T
            }
        }
    }
}

class TexturesViewModel @AssistedInject constructor(
    @ApplicationContext applicationContext: Context,
    repository: Repository,
    @Assisted type: Type
) : ModsViewModel(applicationContext, repository, type) {
    companion object {
        fun provideFactory(
            assistedFactory: TexturesViewModelFactory,
            type: Type
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return assistedFactory.create(type) as T
            }
        }
    }
}

class SeedsViewModel @AssistedInject constructor(
    @ApplicationContext applicationContext: Context,
    repository: Repository,
    @Assisted type: Type
) : ModsViewModel(applicationContext, repository, type) {
    companion object {
        fun provideFactory(
            assistedFactory: SeedsViewModelFactory,
            type: Type
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return assistedFactory.create(type) as T
            }
        }
    }
}

class SkinsViewModel @AssistedInject constructor(
    @ApplicationContext applicationContext: Context,
    repository: Repository,
    @Assisted type: Type
) : ModsViewModel(applicationContext, repository, type) {
    companion object {
        fun provideFactory(
            assistedFactory: SkinsViewModelFactory,
            type: Type
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
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



