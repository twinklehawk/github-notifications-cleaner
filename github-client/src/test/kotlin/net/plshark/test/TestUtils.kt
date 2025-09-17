package net.plshark.test

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking

/** Test utility methods. */
object TestUtils {
    fun doBlocking(block: suspend CoroutineScope.() -> Unit): Unit = runBlocking { block() }
}
