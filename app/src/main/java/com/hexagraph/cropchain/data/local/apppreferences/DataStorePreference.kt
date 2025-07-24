package com.hexagraph.cropchain.data.local.apppreferences

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

interface DataStorePreference<T> {
    suspend fun get(): T = getFlow().first()

    fun getFlow(): Flow<T>

    suspend fun set(value: T)

    /** Update the value of the preference in atomic read-modify-write manner.*/
    suspend fun getAndUpdate(update: (T) -> T): Unit =
        throw NotImplementedError("getAndUpdate has not been implemented for this preference.")
}