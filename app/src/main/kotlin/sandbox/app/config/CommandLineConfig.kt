package sandbox.app.config

import picocli.CommandLine
import picocli.CommandLine.RunAll

fun CommandLine.configCli(): CommandLine {
    executionStrategy = RunAll()
    isAbbreviatedOptionsAllowed = true
    isAbbreviatedSubcommandsAllowed = true
    isExpandAtFiles = true
    isCaseInsensitiveEnumValuesAllowed = true
    isOptionsCaseInsensitive = true
    isPosixClusteredShortOptionsAllowed = true
    isUsageHelpAutoWidth = true
    isInterpolateVariables = true
    isOverwrittenOptionsAllowed = true
    return this
}