package sandbox.utils

import java.util.*

data class Version(
    val major: Int,
    val minor: Int = 0,
    val micro: Int = 0,
    val qualifier: Qualifer? = null,
) : Comparable<Version> {

    class Qualifer(
        val value: String,
        val prefix: Prefix,
    ) {
        override fun equals(other: Any?): Boolean {
            return when {
                other === this -> true
                other == null || other.javaClass != javaClass -> false
                else -> (value == (other as Qualifer).value) && (prefix == other.prefix)
            }
        }

        override fun toString() = value
        val namePart: String
            get() = "${prefix.value}$value"

        override fun hashCode(): Int {
            return Objects.hash(
                value,
                prefix
            )
        }

        enum class Prefix(internal val value: Char) {
            DASH('-'),
            UNDERSCORE('_');

            companion object {
                @JvmStatic
                fun of(char: Char): Prefix = entries
                    .firstOrNull { it.value == char }
                    ?: throw IllegalArgumentException("Invalid qualifier prefix: $char")
            }
        }

    }


    override fun compareTo(other: Version): Int = naturalOrdering.compare(this, other)

    override fun toString(): String = "$major.$minor.$micro${qualifier?.let { qualifier.namePart } ?: ""}"

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
         * Using a modified version of a regex
         * [here](https://gist.github.com/jhorsman/62eeea161a13b80e39f5249281e17c39?permalink_comment_id=2918033#gistcomment-2918033).
         *
         * The following modifations were made:
         * 1. The first group excludes the dash (`-`) character, which sometimes feature as part of filename.
         * 2. The qualifier pattern extends now to the end of the string. (it is up to the caller to clean
         *    up/prepare the string by removing non-relavant text.).
         */
        private val regex =
            Regex(
                """[-_]?(0|[1-9]\d*)(?:\.(0|[1-9]\d*)(?:\.(0|[1-9]\d*))?(?:[-_](\w[\w.\-]*))?)?"""
            )

        fun of(versionString: String): Version? {
            val m = regex.find(versionString) ?: return null
            return Version(
                major = m.groups[1]?.value?.toIntOrNull() ?: 0,
                minor = m.groups[2]?.value?.toIntOrNull() ?: 0,
                micro = m.groups[3]?.value?.toIntOrNull() ?: 0,
                qualifier = m.groups[4]?.let { g ->
                    val value = versionString.substring(g.range.first)
                    val prefix: Qualifer.Prefix = Qualifer.Prefix.of(versionString[g.range.first - 1])
                    Qualifer(value, prefix)
                }
            )
        }

        /**
         * Locates a possoble version values within a string.
         *
         * @param string
         * @return Either a range, or `null` if none could be found.
         */
        fun locate(string: String): IntRange? {
            val m = regex.find(string)?.groups?.first() ?: return null
            return m.range
        }

    }
}

