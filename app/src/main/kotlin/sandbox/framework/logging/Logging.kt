package sandbox.framework.logging

sealed class Level(private val priority: Int, val label: String) : Comparable<Level> {

    companion object {

        private val all: List<Level> = listOf(INFO, WARNING, DEBUG, ERROR, ASSERT, SILENT).sorted()

        @JvmStatic
        fun values(): List<Level> = all

        @JvmStatic
        fun valueOf(value: String): Level {
            return when (value) {
                "INFO", INFO.label -> INFO
                "WARNING", WARNING.label -> WARNING
                "DEBUG", DEBUG.label -> DEBUG
                "ERROR", ERROR.label -> ERROR
                "ASSERT", ASSERT.label -> ASSERT
                "SILENT", SILENT.label, "" -> SILENT
                else -> throw IllegalArgumentException("No object sandbox.framework.logging.Level.$value")
            }
        }
    }

    override fun compareTo(other: Level): Int = priority.compareTo(other.priority)

    final override fun toString() = label

    data object INFO : Level(0, "info")
    data object WARNING : Level(1, "warn")
    data object DEBUG : Level(2, "debug")
    data object ERROR : Level(3, "erro")
    data object ASSERT : Level(4, "assert")
    data object SILENT : Level(5, "silent")
}

