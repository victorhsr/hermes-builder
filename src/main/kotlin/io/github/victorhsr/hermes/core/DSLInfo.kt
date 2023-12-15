package io.github.victorhsr.hermes.core

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
}