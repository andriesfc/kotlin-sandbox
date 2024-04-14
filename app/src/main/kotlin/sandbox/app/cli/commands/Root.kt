package sandbox.app.cli.commands

import picocli.CommandLine.Command


@Command(
    name = "sandbox-app",
    descriptionHeading = "Sandbox App for demos and playthings",
    mixinStandardHelpOptions = true,
    sortOptions = true,
    versionProvider = sandbox.app.cli.VersionProvider::class,
    commandListHeading = "Commands",
    subcommands = [
        sandbox.app.cli.commands.redi.RediRoot::class
    ]
)
class Root()