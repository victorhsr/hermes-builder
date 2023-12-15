package io.github.victorhsr.hermes.core.gen

import com.squareup.javapoet.*
import java.util.function.Consumer


fun buildClassType(fullQualifiedClassName: String): TypeName {
    if (fullQualifiedClassName.contains(">")) {
        return buildClassTypeForGenerics(fullQualifiedClassName);
    }

    val lastDotIndex = fullQualifiedClassName.lastIndexOf(".")

    if (lastDotIndex > -1) {
        val packageName = fullQualifiedClassName.substring(0, lastDotIndex)
        val className = fullQualifiedClassName
            .substring(lastDotIndex + 1, fullQualifiedClassName.length)

        return ClassName.get(packageName, className)
    }

    return TypeVariableName.get(fullQualifiedClassName)
}

fun buildClassTypeForGenerics(typeAsString: String): TypeName {
    val startIndex = typeAsString.indexOf("<")
    val endIndex = typeAsString.lastIndexOf(">")

    if (startIndex == -1 || endIndex == -1) {
        try {
            // If no '<' or '>' found, it means it's a non-generic type
            return ClassName.bestGuess(typeAsString)
        } catch (ex: Exception) {
            throw ex
        }
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

    for (i in 0 until genericTypesString.length) {
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

fun buildConsumerType(fullQualifiedClassName: String): ParameterizedTypeName {
    val className = buildClassType(fullQualifiedClassName)
    return ParameterizedTypeName.get(ClassName.get(Consumer::class.java), className)
}

fun buildConsumerArrayType(fullQualifiedClassName: String): ArrayTypeName {
    return ArrayTypeName.of(buildConsumerType(fullQualifiedClassName))
}

fun main() {
    val typeAsString = "java.util.Map<java.lang.String,java.util.List<java.util.List<java.util.List<java.lang.String>>>>"
    val typeName: TypeName = buildClassTypeForGenerics(typeAsString)

    System.out.println(typeName);
}