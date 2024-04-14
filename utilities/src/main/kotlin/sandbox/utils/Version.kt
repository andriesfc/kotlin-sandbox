@file:Suppress("MemberVisibilityCanBePrivate")

package sandbox.utils

import java.util.*

data class Version(
    val major: Int,
    val minor: Int = 0,
    val micro: Int = 0,
    val qualifier: Qualifier? = null,
) : Comparable<Version> {

    constructor(
        major: Int,
        minor: Int,
        micro: Int,
        buildInfo: String,
    ) : this(major, minor, micro, Qualifier.parse(buildInfo))

    fun isRelease(): Boolean = qualifier == null || qualifier.indicator.release

    class Qualifier internal constructor(
        val name: String,
        val indicator: Indicator,
    ) {
        override fun equals(other: Any?): Boolean {
            return when {
                other === this -> true
                other == null || other.javaClass != javaClass -> false
                else -> (name == (other as Qualifier).name) && (indicator == other.indicator)
            }
        }

        override fun toString() = "${indicator.value}$name"
        override fun hashCode(): Int {
            return Objects.hash(
                name,
                indicator
            )
        }

        enum class Indicator(val value: Char, val release: Boolean) {
            Dash('-', false),
            Underscore('_', true),
            Plus('+', true);
        }

        companion object {
            fun parse(qualifierStr: String): Qualifier? {

                if (qualifierStr.isBlank())
                    return null

                require(qualifierStr.length > 1) {
                    "version qualifer must be 1 or more"
                }

                val ind = requireNotNull(Indicator.entries.firstOrNull { it.value == qualifierStr.first() }) {
                    "invalid build qualifer [$qualifierStr]. Note qualifier must start with any of the following: ${
                        Indicator.entries.joinToString { it.value.toString() }
                    }"
                }

                return Qualifier(qualifierStr.substring(1), ind)
            }
        }
    }

    override fun compareTo(other: Version): Int = Ordering.compare(this, other)

    override fun toString(): String = "$major.$minor.$micro${qualifier?.toString() ?: ""}"


    object Ordering : Comparator<Version> by compareBy(
        Version::major,
        Version::minor,
        Version::micro,
        Version::isRelease
    ) {
        val latestFirst: Comparator<Version> = compareBy(
            Version::major,
            Version::minor,
            Version::micro
        ).reversed().thenComparing(Version::isRelease)
    }

    companion object {

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
                """[-_]?(0|[1-9]\d*)(?:\.(0|[1-9]\d*)(?:\.(0|[1-9]\d*))?([-_+]\w[\w.\-]*)?)?"""
            )

        fun parse(versionString: String): Version? {
            val m = regex.find(versionString) ?: return null
            return Version(
                major = m.groups[1]?.value?.toIntOrNull() ?: 0,
                minor = m.groups[2]?.value?.toIntOrNull() ?: 0,
                micro = m.groups[3]?.value?.toIntOrNull() ?: 0,
                qualifier = m.groups[4]?.value?.run(Qualifier::parse)
            )
        }

        /**
         * Locates a possoble version values within a string.
         *
         * @param string
         * @return Either a range, or `null` if none could be found.
         */
        fun find(string: String): IntRange? {
            val m = regex.find(string)?.groups?.first() ?: return null
            return m.range
        }

    }
}
