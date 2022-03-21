package com.iwelogic.minecraft.mods.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

fun Long.timer(): Flow<Long> {
    val time = this
    return flow {
        for (i in time downTo 1 step 1) {
            emit(i)
            delay(1000)
        }
    }.flowOn(Dispatchers.Default)
}

fun Long.timerString(): Flow<String> {
    val time = this
    return flow {
        for (i in time downTo 1 step 1) {
            val hours = i / 3600
            val minutes = (i % 3600) / 60
            val seconds = i % 60
            emit(String.format("%02d:%02d:%02d", hours, minutes, seconds))
            delay(1000)
        }
    }.flowOn(Dispatchers.Default)
}