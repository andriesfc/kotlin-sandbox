package sandbox.framework.logging

interface LogRecordStore : java.io.Closeable {
    fun last(): LogRecord?
    fun count(): Long
    fun appender(): LogRecordAppender
    fun open()
    val isOpen: Boolean
    fun drop(): Long
}