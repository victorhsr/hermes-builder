package com.github.victorhsr.hermes.core.gen

import com.squareup.javapoet.*
import java.util.function.Consumer

object TypeUtil {

    private const val LESS_THAN_SYMBOL = "<"
    private const val GREATER_THAN_SYMBOL = ">"
    private const val DOT = "."

    fun buildClassType(fullQualifiedClassName: String): TypeName {
        if (fullQualifiedClassName.contains(GREATER_THAN_SYMBOL)) {
            return buildClassTypeForGenerics(fullQualifiedClassName);
        }

        val lastDotIndex = fullQualifiedClassName.lastIndexOf(DOT)

        if (lastDotIndex > -1) {
            val packageName = fullQualifiedClassName.substring(0, lastDotIndex)
            val className = fullQualifiedClassName.substring(lastDotIndex + 1, fullQualifiedClassName.length)

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

    private fun buildClassTypeForGenerics(typeAsString: String): TypeName {
        val startIndex = typeAsString.indexOf(LESS_THAN_SYMBOL)
        val endIndex = typeAsString.lastIndexOf(GREATER_THAN_SYMBOL)

        if (startIndex == -1 || endIndex == -1) {
            // If no '<' or '>' found, it means it's a non-generic type
            return ClassName.bestGuess(typeAsString)
        }

        // Extract class name and generic type arguments
        val className = typeAsString.substring(0, startIndex)
        val genericTypesString = typeAsString.substring(startIndex + 1, endIndex)

        // Split generic types by comma
        val genericTypes: List<String> = splitGenericTypes(genericTypesString)

        // Create TypeName instances for generic type arguments
        val typeNames: MutableList<TypeName> = ArrayList()
        for (genericType in genericTypes) {
            val typeNameArg = buildClassTypeForGenerics(genericType)
            typeNames.add(typeNameArg)
        }

        // Create TypeName for the parameterized type
        val classNameObj = ClassName.bestGuess(className)
        val typeNameArgs = typeNames.toTypedArray<TypeName>()
        return ParameterizedTypeName.get(classNameObj, *typeNameArgs)
    }

    private fun splitGenericTypes(genericTypesString: String): List<String> {
        val genericTypes: MutableList<String> = ArrayList()
        var level = 0
        var start = 0

        for (i in genericTypesString.indices) {
            val c = genericTypesString[i]
            if (c == '<') {
                level++
            } else if (c == '>') {
                level--
            } else if (c == ',' && level == 0) {
                genericTypes.add(genericTypesString.substring(start, i))
                start = i + 1
            }
        }

        // Add the last type argument
        genericTypes.add(genericTypesString.substring(start))

        return genericTypes
    }
}
