package io.github.victorhsr.hermes.core

data class ClassInfo(
    val fullQualifiedName: String,
    val packageName: String,
    val name: String,
    val parameterName: String,
    val isRoot: Boolean,
    val attributes: List<AttributeInfo>
)

data class AttributeInfo(
    val name: String,
    val setterMethodName: String,
    val type: Class<*>,
    val wrapperClass: Class<*>,
    val hasOptions: Boolean,
    val hasDefaultConstructor: Boolean,
    val isNativeClass: Boolean,
    val buildMethodName: String
)