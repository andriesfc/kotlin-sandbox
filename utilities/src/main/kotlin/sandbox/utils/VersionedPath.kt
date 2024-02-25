package sandbox.utils

import java.util.*


class VersionedPath(val path: String) {

    val artifactId: String?
    val version: Version?

    init {
        val fileName = path.substringAfterLast('/', "").takeUnless(String::isEmpty)

        if (fileName == null) {
            artifactId = null
            version = null
        } else {
            val extStart = fileName.lastIndexOf('.')
            if (extStart == -1) {
                artifactId = fileName
                version = null
            } else {
                val baseName = fileName.substring(0, extStart)
                val versionLocation = Version.locate(baseName)
                if (versionLocation == null) {
                    artifactId = baseName
                    version = null
                } else {
                    artifactId = baseName.substring(0, versionLocation.first)
                    // s.substring(0, regex.find(s).groups.first().range.first)
                    version = Version.of(baseName.substring(versionLocation))
                }
            }
        }

    }

    override fun equals(other: Any?): Boolean {
        return when {
            other === this -> true
            other == null || other.javaClass != javaClass -> false
            else -> (other as VersionedPath).let {
                path == other.path &&
                        artifactId == other.artifactId &&
                        version == other.version
            }
        }
    }

    override fun hashCode(): Int {
        return Objects.hash(
            path,
            artifactId,
            version
        )
    }

    override fun toString(): String = when {
        artifactId == null && version == null -> path
        else -> buildString {
            append('[')
            if (artifactId != null) append(artifactId)
            if (version != null) {
                if (isNotEmpty()) append(':')
                append(version)
            }
            append("] ")
            append(path)
        }
    }
}