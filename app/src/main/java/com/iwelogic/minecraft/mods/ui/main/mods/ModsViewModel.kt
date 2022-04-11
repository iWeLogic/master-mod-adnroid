package com.iwelogic.minecraft.mods.ui.main.mods

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Parcelable
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.android.gms.ads.*
import com.google.firebase.analytics.FirebaseAnalytics
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.data.MultiMap
import com.iwelogic.minecraft.mods.data.Repository
import com.iwelogic.minecraft.mods.data.Result
import com.iwelogic.minecraft.mods.models.*
import com.iwelogic.minecraft.mods.ui.base.BaseViewModel
import com.iwelogic.minecraft.mods.ui.base.SingleLiveEvent
import com.iwelogic.minecraft.mods.utils.*
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
        if (context.get()?.readBoolean(Advertisement.INTERSTITIAL_OPEN_DETAILS.id).isTrue()) {
            showInterstitial.invoke {
                context.get()?.let { FirebaseAnalytics.getInstance(it).logEvent("INTERSTITIAL_OPEN_DETAILS", Bundle()) }
                openMod.invoke(it)
            }
        } else {
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
        if (context.get()?.readBoolean(Advertisement.INTERSTITIAL_OPEN_SEARCH.id).isTrue()) {
            showInterstitial.invoke {
                context.get()?.let { FirebaseAnalytics.getInstance(it).logEvent("INTERSTITIAL_OPEN_SEARCH", Bundle()) }
                openSearch.invoke(true)
            }
        } else {
            openSearch.invoke(true)
        }
    }

    fun onClickFavorite() {
        if (context.get()?.readBoolean(Advertisement.INTERSTITIAL_OPEN_FAVORITE.id).isTrue()) {
            showInterstitial.invoke {
                context.get()?.let { FirebaseAnalytics.getInstance(it).logEvent("INTERSTITIAL_OPEN_FAVORITE", Bundle()) }
                openFavorite.invoke(true)
            }
        } else {
            openFavorite.invoke(true)
        }
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
                queries["where"] = "status=true"
                queries["pageSize"] = PAGE_SIZE
                queries["sortBy"] = sort.value?.query ?: ""
                queries["where"] = Filter.getQuery(filters.value)
                queries["offset"] = mods.value?.size ?: 0
                repository.getMods(type.id, queries).catch {
                    error.postValue(it.message)
                    showProgress(false)
                }.collect { result ->
                    when (result) {
                        is Result.Loading -> {
                            error.postValue(null)
                            showProgress(true)
                        }
                        is Result.Finish -> showProgress(false)
                        is Result.Success -> {
                            val data = result.data?.toMutableList()?.onEach { it.type = type } ?: ArrayList()
                            if (context.get()?.readBoolean(Advertisement.BANNER_IN_LIST.id).isTrue() && !context.get()?.resources?.getBoolean(R.bool.isTablet).isTrue()) {
                                if (data.size > 4)
                                    data.add(4, Mod(id = (0..9999999).random(), type = Type.AD))
                                if (data.size > 19)
                                    data.add(19, Mod(id = (0..9999999).random(), type = Type.AD))
                            }
                            mods.value?.addAll(data)
                            mods.postValue(mods.value)
                            loadBanners()
                            if (data.size < PAGE_SIZE) finished = true
                        }
                        is Result.Error -> error.postValue(result.message)
                    }
                }
            }
        }
    }

    private fun loadBanners() {
        context.get()?.let { context ->
            mods.value?.filter { it.type == Type.AD }?.forEach {
                if (it.adView == null) {
                    it.adView = ProgressBar(context).apply {
                        indeterminateTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.title))
                        indeterminateTintMode = PorterDuff.Mode.SRC_ATOP
                    }
                    val adViewNew = AdView(context)
                    adViewNew.adUnitId = context.getString(R.string.ad_banner)
                    adViewNew.adSize = AdSize.MEDIUM_RECTANGLE
                    val adRequest = AdRequest.Builder().build()
                    adViewNew.adListener = object : AdListener() {
                        override fun onAdFailedToLoad(p0: LoadAdError) {
                            super.onAdFailedToLoad(p0)
                            catchAll {
                                it.adView = ImageView(context).apply { setImageResource(R.drawable.ad_placeholder) }
                            }
                        }

                        override fun onAdLoaded() {
                            super.onAdLoaded()
                            it.adView = adViewNew
                        }
                    }
                    adViewNew.loadAd(adRequest)

                }
            }
        }
    }

    private fun showProgress(status: Boolean) {
        if (status) {
            if (mods.value.isNullOrEmpty()) {
                progress.postValue(true)
            } else {
                mods.value?.add(Mod(type = Type.PROGRESS))
            }
        } else {
            progress.postValue(false)
            mods.value?.removeAll { it.type == Type.PROGRESS }
        }
        mods.postValue(mods.value)
    }

    override fun onReload() {
        job?.cancel()
        showProgress(false)
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
            val span = if (it > 680) 2 else 1
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



