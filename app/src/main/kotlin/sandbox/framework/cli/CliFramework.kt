package sandbox.framework.cli

interface CliCommand : Runnable {
    override fun run() = Unit
}

