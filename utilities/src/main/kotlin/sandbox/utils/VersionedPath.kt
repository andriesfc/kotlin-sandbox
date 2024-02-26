package sandbox.utils

import java.io.File
import java.nio.file.Path
import java.util.*
import kotlin.io.path.Path


class VersionedPath(val path: String) {

    val artifactId: String?
    val version: Version?

    init {
        if (path.endsWith("/")) {
            artifactId = null
            version = null
        } else {

            val lastSeperatorIndex = path.lastIndexOf('/')

            val fileName = when {
                lastSeperatorIndex == -1 -> path
                lastSeperatorIndex < path.length - 1 -> path.substring(lastSeperatorIndex + 1)
                else -> null
            }

            if (fileName == null) {
                artifactId = null
                val versionLocation = Version.locate(path)
                version = versionLocation?.let { Version.of(path.substring(it)) }
            } else {

                val fileNameIsVersionMatch =
                    Version.locate(fileName)?.run { first == 0 && (last + 1) == fileName.length } == true

                if (fileNameIsVersionMatch) {
                    artifactId = null
                    version = Version.of(fileName)
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
                            version = Version.of(baseName.substring(versionLocation))
                        }
                    }
                }
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        return when {
            other === this -> true
            other == null || other.javaClass != javaClass -> false
            else -> (other as VersionedPath).let {
                (path == other.path) &&
                        (artifactId == other.artifactId) &&
                        (version == other.version)
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

    fun file(): File = File(path)
    fun path(): Path = Path(path)

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

    companion object {
    }
}