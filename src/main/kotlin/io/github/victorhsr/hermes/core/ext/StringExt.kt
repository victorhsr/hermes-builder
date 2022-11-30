import java.util.*

fun String.myCapitalize(): String {
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}

fun String.myDecapitalize(): String {
    return this.replaceFirstChar { it.lowercase(Locale.getDefault()) }
}