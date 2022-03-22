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
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

class ModsViewModel @AssistedInject constructor(@ApplicationContext context: Context, private val repository: Repository, @Assisted val category: String) : BaseViewModel() {

    companion object {
        fun provideFactory(assistedFactory: ModsViewModelFactory, category: String): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return assistedFactory.create(category) as T
            }
        }
    }

    val mods: MutableLiveData<MutableList<Mod>> = MutableLiveData(ArrayList())
    val title: MutableLiveData<String> = MutableLiveData()
    val openMod: SingleLiveEvent<Mod> = SingleLiveEvent()

    val onClick: (Mod) -> Unit = {
        openMod.invoke(it)
    }

    init {
        load()
        title.postValue(
            when (category) {
                "addons" -> context.getString(R.string.addons)
                "maps" -> context.getString(R.string.maps)
                "textures" -> context.getString(R.string.textures)
                else -> context.getString(R.string.seeds)
            }
        )
    }

    fun load() {
        viewModelScope.launch {
            repository.getMods(category, 0).catch {
                Log.w("myLog", "load2: " + it.message)
            }.collect { result ->
                when (result) {
                    is Result.Loading -> progress.postValue(true)
                    is Result.Finish -> progress.postValue(false)
                    is Result.Success -> {
                        Log.w("myLog", "load3: ")
                        mods.postValue(result.data?.toMutableList()?.onEach { it.category = category })
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

@AssistedFactory
interface ModsViewModelFactory {
    fun create(type: String): ModsViewModel
}