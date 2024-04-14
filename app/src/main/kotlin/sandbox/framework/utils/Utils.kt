package sandbox.framework.utils

import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import java.net.URL
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.concurrent.TimeUnit
import kotlin.math.absoluteValue


fun TimeUnit.dt(startDateTime: LocalDateTime, endDateTime: LocalDateTime): Long {
    val dt = (startDateTime.toMillis() - endDateTime.toMillis()).absoluteValue
    return convert(dt, TimeUnit.MILLISECONDS)
}

fun LocalDateTime.toMillis(): Long = toInstant(ZoneOffset.UTC).toEpochMilli()

fun StringBuilder.appendStacktrace(t: Throwable): StringBuilder {
    append(StringWriter().use {
        PrintWriter(it).apply {
            t.printStackTrace(this)
            flush()
        }
        it.toString()
    })
    return this
}

fun <T> lazyPublication(initializer: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.PUBLICATION, initializer)

fun URL.toFileOrNull(): File? =
    takeIf { protocol == "file" }?.file?.let(::File)