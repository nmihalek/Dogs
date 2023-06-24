package com.nmihalek.dogs.feature.common.presentation

import java.util.Locale

fun String.capitalise() = replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }