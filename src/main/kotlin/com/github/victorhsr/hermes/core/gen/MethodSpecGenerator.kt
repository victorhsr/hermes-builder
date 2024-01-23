package com.github.victorhsr.hermes.core.gen

import com.squareup.javapoet.MethodSpec
import javax.lang.model.element.Modifier

class MethodSpecGenerator(private val codeBlockGenerator: CodeBlockGenerator) {

    fun buildMethod(attributeInfo: com.github.victorhsr.hermes.core.AttributeInfo): MethodSpec {
        if (attributeInfo.hasOptions) {
            return this.buildMethodWithOptions(attributeInfo)
        }
        return this.buildMethodWithoutOptions(attributeInfo)
    }

    private fun buildMethodWithoutOptions(attributeInfo: com.github.victorhsr.hermes.core.AttributeInfo): MethodSpec {
        return MethodSpec.methodBuilder(attributeInfo.buildMethodName)
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .addParameter(TypeUtil.buildClassType(attributeInfo.type), attributeInfo.name)
            .addCode(this.codeBlockGenerator.buildCodeBlock(attributeInfo))
            .returns(TypeUtil.buildConsumerType(attributeInfo.wrapperClass))
            .build()
    }

    private fun buildMethodWithOptions(attributeInfo: com.github.victorhsr.hermes.core.AttributeInfo): MethodSpec {
        return MethodSpec.methodBuilder(attributeInfo.buildMethodName)
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .addParameter(TypeUtil.buildConsumerArrayType(attributeInfo.type), "options")
            .varargs()
            .addCode(this.codeBlockGenerator.buildCodeBlock(attributeInfo))
            .returns(TypeUtil.buildConsumerType(attributeInfo.wrapperClass))
            .addAnnotation(SafeVarargs::class.java)
            .build()
    }

    fun buildRootMethod(classInfo: com.github.victorhsr.hermes.core.ClassInfo): MethodSpec {
        return MethodSpec.methodBuilder(classInfo.parameterName)
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(TypeUtil.buildClassType(classInfo.fullQualifiedName))
            .addParameter(TypeUtil.buildConsumerArrayType(classInfo.fullQualifiedName), "options")
            .varargs(true)
            .addCode(this.codeBlockGenerator.buildCodeBlock(classInfo))
            .addAnnotation(SafeVarargs::class.java)
            .build()
    }

}