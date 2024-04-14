package sandbox.app

import picocli.CommandLine
import sandbox.app.cli.commands.Root
import sandbox.app.config.configCli
import kotlin.system.exitProcess


fun main(args: Array<String>) {
    exitProcess(CommandLine(Root()).configCli().execute(*args))
}

