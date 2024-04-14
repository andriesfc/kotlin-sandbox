package sandbox.framework.logging

interface LogRecordAppender : java.io.Closeable {
    fun append(record: LogRecord)
}