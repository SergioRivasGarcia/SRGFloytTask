package com.example.presentation.main.util

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * This returns a [Flow] wrapped in an [EventFlow] to be used for a single shot event
 */
fun <T : Any> Channel<T>.receiveAsEventFlow() = EventFlow(receiveAsFlow())

fun <T> Channel<T>.call(t: T? = null) {
    @Suppress("UNCHECKED_CAST")
    (trySend(t ?: Unit as T).isSuccess)
}
