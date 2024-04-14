package sandbox.framework.logging

import java.time.OffsetDateTime

data class LogRecordData(
    override val datetime: OffsetDateTime,
    override val level: Level,
    override val message: String,
    override val throwable: Throwable?,
    override val id: String,
) : LogRecord