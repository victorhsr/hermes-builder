package io.github.victorhsr.hermes.core.gen

import com.squareup.javapoet.CodeBlock
import io.github.victorhsr.hermes.core.AttributeInfo
import io.github.victorhsr.hermes.core.ClassInfo
import java.util.*
import java.util.stream.Stream

class CodeBlockGenerator {

    fun buildCodeBlock(attributeInfo: AttributeInfo): CodeBlock {
        if (attributeInfo.hasOptions) {
            return this.buildCodeBlockWithOptions(attributeInfo)
        }
        return this.buildCodeBlockWithoutOptions(attributeInfo)
    }

    fun buildCodeBlock(classInfo: ClassInfo): CodeBlock {
        val classDeclaration = String.javaClass

        return CodeBlock
            .builder()
            .addStatement("final $classDeclaration ${classInfo.parameterName} = new $classDeclaration()")
            .addStatement(
                "\$T.of(options).forEach(option -> option.accept(${classInfo.parameterName}))",
                Stream::class.java
            )
            .addStatement("return ${classInfo.parameterName}")
            .build()
    }


    private fun buildCodeBlockWithoutOptions(attributeInfo: AttributeInfo): CodeBlock {
        val wrapperSimpleName =
            attributeInfo.wrapperClass.simpleName.replaceFirstChar { it.lowercase(Locale.getDefault()) }

        return CodeBlock
            .builder()
            .addStatement("return (${wrapperSimpleName}) -> $wrapperSimpleName.${attributeInfo.setterMethodName}(${attributeInfo.name})")
            .build()
    }

    private fun buildCodeBlockWithOptions(attributeInfo: AttributeInfo): CodeBlock {
        val className = attributeInfo.type.name
        val wrapperSimpleName =
            attributeInfo.wrapperClass.simpleName.replaceFirstChar { it.lowercase(Locale.getDefault()) }

        return CodeBlock
            .builder()
            .addStatement("final $className ${attributeInfo.name} = new $className()")
            .addStatement("\$T.of(options).forEach(option -> option.accept(${attributeInfo.name}))", Stream::class.java)
            .addStatement("return (${wrapperSimpleName}) -> $wrapperSimpleName.${attributeInfo.setterMethodName}(${attributeInfo.name})")
            .build()
    }

}