package sandbox.app.cli

import picocli.CommandLine

data object VersionProvider : CommandLine.IVersionProvider {
    override fun getVersion(): Array<String> = arrayOf(
        "sandbox-app-v0.0.0"
    )
}