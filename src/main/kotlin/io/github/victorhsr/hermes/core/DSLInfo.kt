package io.github.victorhsr.hermes.core

import myCapitalize
import myDecapitalize

data class ClassInfo(
    val fullQualifiedName: String,
    val parameterName: String,
    val isRoot: Boolean,
    val attributes: List<AttributeInfo>
) {
    val packageName = this.fullQualifiedName.substring(0, this.fullQualifiedName.lastIndexOf("."))
    val simpleName = this.fullQualifiedName.removePrefix("$packageName.")
}

data class AttributeInfo(
    val name: String,
    val setterMethodName: String,
    val type: String,
    val wrapperClass: String,
    val hasOptions: Boolean,
    val buildMethodName: String
) {
    val wrapperClassParamName = this.wrapperClass.substring(this.wrapperClass.lastIndexOf(".") + 1).myDecapitalize()
    val wrapperClassSimpleName = this.wrapperClassParamName.myCapitalize()
    val className: String

    init {
        val lastDot = this.type.lastIndexOf(".")

        if (lastDot > 1) {
            className = this.type.substring(lastDot + 1)
        } else {
            className = this.type
        }
    }
}