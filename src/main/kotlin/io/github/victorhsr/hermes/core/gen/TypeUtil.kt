package io.github.victorhsr.hermes.core.gen

import com.squareup.javapoet.*
import java.util.function.Consumer

fun buildClassType(fullQualifiedClassName: String): TypeName {
    val lastDotIndex = fullQualifiedClassName.lastIndexOf(".")

    if (lastDotIndex > -1) {
        val packageName = fullQualifiedClassName.substring(0, lastDotIndex)
        val className = fullQualifiedClassName
            .substring(lastDotIndex + 1, fullQualifiedClassName.length)

        return ClassName.get(packageName, className)
    }

    return TypeVariableName.get(fullQualifiedClassName)
}

fun buildConsumerType(fullQualifiedClassName: String): ParameterizedTypeName {
    val className = buildClassType(fullQualifiedClassName)
    return ParameterizedTypeName.get(ClassName.get(Consumer::class.java), className)
}

fun buildConsumerArrayType(fullQualifiedClassName: String): ArrayTypeName {
    return ArrayTypeName.of(buildConsumerType(fullQualifiedClassName))
}