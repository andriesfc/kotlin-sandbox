package sandbox.redi.tools.feedback

import sandbox.framework.utils.appendStacktrace
import java.util.*

class FeedbackToStdOut(private val operation: String, private val failFast: () -> Boolean) : Feedback {

    override fun equals(other: Any?): Boolean {
        return when {
            other === this -> true
            other == null -> false
            else -> (other as? FeedbackToStdOut)?.let { operation == it.operation && failFast == it.failFast } ?: false
        }
    }

    override fun hashCode(): Int = Objects.hash(operation, failFast)

    override fun started() {
        info { "Started $operation" }
    }

    override fun error(message: () -> String) {
        System.err.println("[ERROR]: ${message()}")
    }

    override fun error(cause: Throwable, message: () -> String) {
        System.err.print(buildString {
            appendLine("[ERROR]: ${message()}")
            appendStacktrace(cause)
            appendLine()
        })
        if (failFast()) throw cause
    }

    override fun info(message: () -> String) {
        println("[INFO]: ${message()}")
    }

    override fun warn(message: () -> String) {
        println("[WARNING]: ${message()}")
    }

    override fun trace(message: () -> String) {
        println("[TRACE]: ${message()}")
    }

    override fun completedOk() {
        info { "Completed" }
    }

    override fun failed(cause: Throwable) {
        error(cause) { "Operation Failed!" }
    }

}