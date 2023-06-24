package com.nmihalek.dogs.feature.common.presentation

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@HiltAndroidApp
class DogsApp: Application() {

    @OptIn(FlowPreview::class, DelicateCoroutinesApi::class)
    companion object {
        private val debounceState = MutableStateFlow { }

        const val DEBOUNCE_INTERVAL_MS = 80L

        init {
            GlobalScope.launch(Dispatchers.Main) {
                // IMPORTANT: Make sure to import kotlinx.coroutines.flow.collect
                debounceState
                    .debounce(DEBOUNCE_INTERVAL_MS)
                    .collect { onClick ->
                        onClick.invoke()
                    }
            }
        }

        fun debounceClicks(onClick: () -> Unit) {
            debounceState.value = onClick
        }
    }
}