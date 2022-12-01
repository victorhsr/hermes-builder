package io.github.victorhsr.hermes.core.gen

import com.squareup.javapoet.MethodSpec
import io.github.victorhsr.hermes.core.AttributeInfo
import io.github.victorhsr.hermes.core.ClassInfo
import javax.lang.model.element.Modifier

class MethodSpecGenerator(private val codeBlockGenerator: CodeBlockGenerator) {

    fun buildMethod(attributeInfo: AttributeInfo): MethodSpec {
        if (attributeInfo.hasOptions) {
            return this.buildMethodWithOptions(attributeInfo)
        }
        return this.buildMethodWithoutOptions(attributeInfo)
    }

    private fun buildMethodWithoutOptions(attributeInfo: AttributeInfo): MethodSpec {
        return MethodSpec.methodBuilder(attributeInfo.buildMethodName)
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .addParameter(buildClassType(attributeInfo.type), attributeInfo.name)
            .addCode(this.codeBlockGenerator.buildCodeBlock(attributeInfo))
            .returns(buildConsumerType(attributeInfo.wrapperClass))
            .build()
    }

    private fun buildMethodWithOptions(attributeInfo: AttributeInfo): MethodSpec {
        return MethodSpec.methodBuilder(attributeInfo.buildMethodName)
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .addParameter(buildConsumerArrayType(attributeInfo.type), "options")
            .varargs()
            .addCode(this.codeBlockGenerator.buildCodeBlock(attributeInfo))
            .returns(buildConsumerType(attributeInfo.wrapperClass))
            .addAnnotation(SafeVarargs::class.java)
            .build()
    }

    fun buildRootMethod(classInfo: ClassInfo): MethodSpec {
        return MethodSpec.methodBuilder(classInfo.parameterName)
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(buildClassType(classInfo.fullQualifiedName))
            .addParameter(buildConsumerArrayType(classInfo.fullQualifiedName), "options")
            .varargs(true)
            .addCode(this.codeBlockGenerator.buildCodeBlock(classInfo))
            .addAnnotation(SafeVarargs::class.java)
            .build()
    }

}