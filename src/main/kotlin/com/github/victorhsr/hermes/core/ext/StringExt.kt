package com.github.victorhsr.hermes.core.ext

import java.util.*

fun String.myCapitalize(): String {
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}

fun String.uncapitalize(): String {
    return this.replaceFirstChar { it.lowercase(Locale.getDefault()) }
}