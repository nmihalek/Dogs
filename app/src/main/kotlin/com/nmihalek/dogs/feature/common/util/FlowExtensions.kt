package com.nmihalek.dogs.feature.common.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun <T, R> Flow<List<T>>.mapList(converter: (T) -> R): Flow<List<R>> =
    map { originalList ->
        originalList.map(converter)
    }
