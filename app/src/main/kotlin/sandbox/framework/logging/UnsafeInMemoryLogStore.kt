package sandbox.framework.logging

/** A very in memory store which is not multithread safe. */
class UnsafeInMemoryLogStore : LogRecordStore {

    private var open = true
    private val records = ArrayList<LogRecord>()

    override fun last(): LogRecord? = records.lastOrNull()

    override fun count(): Long = records.size.toLong()

    override fun appender(): LogRecordAppender {
        check(open) { "UnsafeMemoryLogStore is not open" }
        return AppenderImpl(::isOpen) {
            check(open) { "UnsafeMemoryLogStore is not open" }
            records.addLast(it)
        }
    }

    override fun open() {
        if (open) return
        open = true
    }

    override val isOpen: Boolean
        get() = open

    override fun drop(): Long {
        check(open) { "UnsafeMemoryLogStore is not open" }
        val dropped = records.size.toLong()
        records.clear()
        return dropped
    }

    override fun close() {
        if (!open) return
        drop()
        open = false
    }

    private class AppenderImpl(isOpen: () -> Boolean, send: (LogRecord) -> Unit) : LogRecordAppender {

        private var isOpen: (() -> Boolean)? = isOpen
        private var appendOp: ((LogRecord) -> Unit)? = send


        override fun append(record: LogRecord) {
            val emit = appendOp
            check(emit != null) { "Appender has been closed." }
            runCatching { emit(record) }.onFailure { t ->
                val closedUpStream = t is IllegalStateException && isOpen.let { check -> check != null && check() }
                if (closedUpStream) close() else throw t
            }
        }

        override fun close() {
            isOpen = null
            appendOp = null
        }

    }

}