package io.github.victorhsr.hermes.core.gen

import com.squareup.javapoet.*
import java.util.function.Consumer


fun buildClassType(fullQualifiedClassName: String): TypeName {
    if(fullQualifiedClassName.contains(">")){
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
    val startIndex: Int = typeAsString.indexOf("<")
    val endIndex: Int = typeAsString.lastIndexOf(">")


    // Extract class name and generic type argument
    val className: String = typeAsString.substring(0, startIndex)
    val genericType: String = typeAsString.substring(startIndex + 1, endIndex)


    // Create TypeName for the generic type argument using ClassName.bestGuess
    val typeNameArg: TypeName = ClassName.bestGuess(genericType)


    // Create TypeName for the parameterized type
    val typeName: TypeName = ParameterizedTypeName.get(ClassName.bestGuess(className), typeNameArg)
    return typeName
}

fun buildConsumerType(fullQualifiedClassName: String): ParameterizedTypeName {
    val className = buildClassType(fullQualifiedClassName)
    return ParameterizedTypeName.get(ClassName.get(Consumer::class.java), className)
}

fun buildConsumerArrayType(fullQualifiedClassName: String): ArrayTypeName {
    return ArrayTypeName.of(buildConsumerType(fullQualifiedClassName))
}

fun main() {
    val typeAsString = "java.util.List<java.lang.String>"

    val startIndex: Int = typeAsString.indexOf("<")
    val endIndex: Int = typeAsString.lastIndexOf(">")


    // Extract class name and generic type argument
    val className: String = typeAsString.substring(0, startIndex)
    val genericType: String = typeAsString.substring(startIndex + 1, endIndex)


    // Create TypeName for the generic type argument using ClassName.bestGuess
    val typeNameArg: TypeName = ClassName.bestGuess(genericType)


    // Create TypeName for the parameterized type
    val typeName: TypeName = ParameterizedTypeName.get(ClassName.bestGuess(className), typeNameArg)

    System.out.println(typeName);
}