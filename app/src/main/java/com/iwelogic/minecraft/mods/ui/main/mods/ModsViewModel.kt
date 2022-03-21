package com.iwelogic.minecraft.mods.ui.main.mods

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.iwelogic.minecraft.mods.data.Repository
import com.iwelogic.minecraft.mods.data.Result
import com.iwelogic.minecraft.mods.models.Mod
import com.iwelogic.minecraft.mods.ui.base.BaseViewModel
import com.iwelogic.minecraft.mods.ui.base.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ModsViewModel @Inject constructor(private val repository: Repository) : BaseViewModel() {

    val mods: MutableLiveData<MutableList<Mod>> = MutableLiveData(ArrayList())
    val openMod: SingleLiveEvent<Mod> = SingleLiveEvent()

    val onClick: (Mod) -> Unit = {
        openMod.invoke(it)
    }

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            repository.getMods((0..100).random()).catch {
                Log.w("myLog", "load2: " + it.message)
            }.collect { result ->
                when (result) {
                    is Result.Loading -> progress.postValue(true)
                    is Result.Finish -> progress.postValue(false)
                    is Result.Success -> {
                        Log.w("myLog", "load3: ")
                        mods.postValue(result.data?.toMutableList()?.onEach { it.category = "addons" })
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
