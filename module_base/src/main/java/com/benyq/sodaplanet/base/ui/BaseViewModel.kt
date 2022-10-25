package com.benyq.sodaplanet.base.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.benyq.sodaplanet.base.coroutine.Coroutine
import com.orhanobut.logger.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext

open class BaseViewModel(application: Application) : AndroidViewModel(application) {

    val context: Context by lazy { this.getApplication<Application>() }

    fun <T> execute(
        scope: CoroutineScope = viewModelScope,
        context: CoroutineContext = Dispatchers.IO,
        block: suspend CoroutineScope.() -> T
    ): Coroutine<T> {
        return Coroutine.async(scope, context) { block() }
    }

    fun <R> submit(
        scope: CoroutineScope = viewModelScope,
        context: CoroutineContext = Dispatchers.IO,
        block: suspend CoroutineScope.() -> Deferred<R>
    ): Coroutine<R> {
        return Coroutine.async(scope, context) { block().await() }
    }

}

class LogContinuation<T>(private val continuation: Continuation<T>): Continuation<T> by continuation {
    override fun resumeWith(result: Result<T>) {
        Logger.d("LogContinuation before resumeWith: ${result.getOrNull()}")
        continuation.resumeWith(result)
        Logger.d("LogContinuation after resumeWith.")
    }
}

class RetryCallback(val callback: suspend CoroutineScope.() -> Unit): AbstractCoroutineContextElement(RetryCallback) {
    companion object Key: CoroutineContext.Key<RetryCallback>
}