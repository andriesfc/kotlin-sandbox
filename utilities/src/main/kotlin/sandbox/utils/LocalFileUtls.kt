@file:JvmName("LocalFileUtils")

package sandbox.utils

val hostIsDosLike: Boolean
    get() = System.getProperty("os.name").lowercase()
        .let { osName -> "win" in osName || "nt" in osName || "dos" in osName }

fun standarizePath(userPath: String): String {
    return if (hostIsDosLike)
        userPath.replace('\\', '/')
    else
        userPath.replace('/', '\\')
}