package com.iwelogic.minecraft.mods.ui.main.mods

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.data.Repository
import com.iwelogic.minecraft.mods.data.Result
import com.iwelogic.minecraft.mods.models.Mod
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
    }
    val sort: MutableLiveData<String> = MutableLiveData("default")
    val mods: MutableLiveData<MutableList<Mod>> = MutableLiveData(ArrayList())
    val title: MutableLiveData<String> = MutableLiveData()
    val openMod: SingleLiveEvent<Mod> = SingleLiveEvent()
    val openFilter: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val spanCount: MutableLiveData<Int> = MutableLiveData(1)
    var job: Job? = null
    var finished = false

    val onSelectSort: (String) -> Unit = {
        sort.postValue(it)
    }

    /*fun checkSort() {
        val savedSort = context.get()?.readString("sort")
        if (savedSort != sort.value) {
            sort.postValue(savedSort)
        }
    }*/

    fun onClickFilter(){
        openFilter.invoke(true)
    }

    val onClick: (Mod) -> Unit = {
        openMod.invoke(it)
    }

    val onScroll: (Int) -> Unit = {
        if ((mods.value?.size ?: 0) < it + 5)
            load()
    }

    init {
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
    }

    private fun load() {
        if (!job?.isActive.isTrue() && mods.value?.none { it.category == ERROR || it.category == PROGRESS }.isTrue() && !finished) {
            job = viewModelScope.launch {
                repository.getMods(category, mods.value?.size ?: 0).catch {
                    Log.w("myLog", "load2: " + it.message)
                }.collect { result ->
                    when (result) {
                        is Result.Loading -> showProgress(true)
                        is Result.Finish -> showProgress(false)
                        is Result.Success -> {
                            val data = result.data?.toMutableList()?.onEach { it.category = category } ?: ArrayList()
                            mods.value?.addAll(data)
                            mods.postValue(mods.value)
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
}

@AssistedFactory
interface ModsViewModelFactory {
    fun create(type: String): ModsViewModel
}