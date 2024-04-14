package sandbox.redi.tools.feedback

interface Feedback {
    fun started()
    fun error(message: () -> String)
    fun error(cause: Throwable, message: () -> String)
    fun info(message: () -> String)
    fun warn(message: () -> String)
    fun trace(message: () -> String)
    fun completedOk()
    fun failed(cause: Throwable)
}

