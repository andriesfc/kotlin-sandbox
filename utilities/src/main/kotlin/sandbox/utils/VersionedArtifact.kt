@file:Suppress("LocalVariableName")

package sandbox.utils

import java.util.*

class VersionedArtifact(path: String) {

    val artifactId: String?
    val version: Version?
    val isRelease: Boolean

    val path: String = path.also {
        val (a, v) = extractArtifactVersionPair(path)
        this.artifactId = a
        this.version = v
        this.isRelease = version == null || version.isRelease()
    }

    override fun hashCode(): Int {
        return Objects.hash(
            path,
            artifactId,
            version,
            isRelease
        )
    }

    override fun equals(other: Any?): Boolean {

        if (other === this)
            return true

        if (other == null || other.javaClass != javaClass)
            return false

        other as VersionedArtifact

        return artifactId == other.artifactId &&
                version == other.version &&
                isRelease == other.isRelease &&
                path == other.path
    }


    override fun toString(): String = buildString {
        append('[')
        artifactId?.also(::append)
        append(':')
        version?.also(::append)
        append("]")
        append(path)
    }
}

private fun extractArtifactVersionPair(path: String): Pair<String?, Version?> {

    if (path.endsWith('/'))
        return None

    val filename = path.lastIndexOf('/').let { index ->
        when {
            index == -1 -> path
            index < path.length - 1 -> path.substring(index + 1)
            else -> null
        }
    }

    if (filename == null) {
        val artitactId = null
        val version = Version.find(path)?.let { Version.parse(path.substring(it)) }
        return when (version) {
            null -> None
            else -> Pair(artitactId, version)
        }
    }

    if (filename.isVersionOnly()) {
        val artifactId = null
        val version = Version.parse(filename)
        return Pair(artifactId, version)
    }

    val baseName = filename.substringBeforeLast('.')
    if (baseName.isEmpty())
        return None

    val versionLocation = Version.find(baseName)

    if (versionLocation.enclosesString(baseName)) {
        val version = Version.parse(baseName)
        return Pair(baseName, version)
    }

    if (versionLocation == null)
        return Pair(baseName, null)


    System.err.println(
        """
         versionLocation: [$versionLocation]
                baseName: [$baseName]
           versionString: [${baseName.substring(versionLocation!!)}]
    """.trimIndent()
    )

    val version = Version.parse(baseName.substring(versionLocation))
    val artifact = baseName.substring(0, versionLocation.first)

    return Pair(artifact, version)
}

private fun String.isVersionOnly(): Boolean = Version.find(this).enclosesString(this)
private fun IntRange?.enclosesString(string: String): Boolean =
    when (this) {
        null -> false
        else -> first == 0 && last + 1 == string.length
    }

private val None = Pair<String?, Version?>(null, null)