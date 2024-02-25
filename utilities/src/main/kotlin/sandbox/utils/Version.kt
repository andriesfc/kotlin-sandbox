package sandbox.utils


data class Version(
    val major: Int,
    val minor: Int = 0,
    val micro: Int = 0,
    val qualifier: String? = null,
) : Comparable<Version> {

    override fun compareTo(other: Version): Int = naturalOrdering.compare(this, other)

    override fun toString(): String = "$major.$minor.$micro${qualifier?.let { "-$qualifier" } ?: ""}"

    companion object {

        val naturalOrdering = compareBy(
            Version::major,
            Version::minor,
            Version::micro
        )

        val latestFirsOrdering: Comparator<Version> = naturalOrdering.reversed()

        /**
         * Semantic version regular expression.
         *
         * Using a modified version of a regex [here](https://gist.github.com/jhorsman/62eeea161a13b80e39f5249281e17c39?permalink_comment_id=2918033#gistcomment-2918033).
         */
        private val regex =
            Regex("[-_]?(0|[1-9]\\d*)(?:\\.(0|[1-9]\\d*)(?:\\.(0|[1-9]\\d*))?(?:-(\\w[\\w.\\-_]*))?)?")

        fun of(versionString: String): Version? {
            val m = regex.find(versionString) ?: return null
            return Version(
                major = m.groups[1]?.value?.toIntOrNull() ?: 0,
                minor = m.groups[2]?.value?.toIntOrNull() ?: 0,
                micro = m.groups[3]?.value?.toIntOrNull() ?: 0,
                qualifier = m.groups[4]?.let { g ->
                    versionString.substring(g.range.first)
                }
            )
        }

        fun locate(s: String): IntRange? {
            val m = regex.find(s)?.groups?.first() ?: return null
            return m.range
        }
    }
}
