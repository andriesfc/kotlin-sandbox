package sandbox.framework.logging

import java.time.OffsetDateTime

interface LogRecord : Comparable<LogRecord> {

    val level: Level
    val message: String
    val throwable: Throwable?
    val datetime: OffsetDateTime
    val id: String

    override fun compareTo(other: LogRecord): Int = datetime.compareTo(other.datetime)
}