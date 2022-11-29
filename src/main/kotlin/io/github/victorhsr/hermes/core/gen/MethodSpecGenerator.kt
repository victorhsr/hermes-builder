package io.github.victorhsr.hermes.core.gen

import com.squareup.javapoet.*
import io.github.victorhsr.hermes.core.AttributeInfo
import io.github.victorhsr.hermes.core.ClassInfo
import java.util.function.Consumer
import javax.lang.model.element.Modifier

class MethodSpecGenerator(private val codeBlockGenerator: CodeBlockGenerator) {

    fun buildMethod(attributeInfo: AttributeInfo): MethodSpec {
        if (attributeInfo.hasOptions) {
            return this.buildMethodWithOptions(attributeInfo)
        }
        return this.buildMethodWithoutOptions(attributeInfo)
    }

    private fun buildConsumerType(clazz: Class<*>) =
        ParameterizedTypeName.get(ClassName.get(Consumer::class.java), TypeName.get(clazz))

    private fun buildConsumerArrayType(clazz: Class<*>) = ArrayTypeName.of(buildConsumerType(clazz))

    private fun buildMethodWithoutOptions(attributeInfo: AttributeInfo): MethodSpec {
        return MethodSpec.methodBuilder(attributeInfo.buildMethodName)
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .addParameter(attributeInfo.type, attributeInfo.name)
            .addCode(this.codeBlockGenerator.buildCodeBlock(attributeInfo))
            .returns(this.buildConsumerType(attributeInfo.wrapperClass))
            .build()
    }

    private fun buildMethodWithOptions(attributeInfo: AttributeInfo): MethodSpec {
        return MethodSpec.methodBuilder(attributeInfo.buildMethodName)
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .addParameter(this.buildConsumerArrayType(attributeInfo.type), "options")
            .varargs()
            .addCode(this.codeBlockGenerator.buildCodeBlock(attributeInfo))
            .returns(this.buildConsumerType(attributeInfo.wrapperClass))
            .addAnnotation(SafeVarargs::class.java)
            .build()
    }

    fun buildRootMethod(classInfo: ClassInfo): MethodSpec {
        return MethodSpec.methodBuilder(classInfo.parameterName)
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(String.javaClass)
            .addParameter(this.buildConsumerArrayType(String.javaClass), "options")
            .varargs(true)
            .addCode(this.codeBlockGenerator.buildCodeBlock(classInfo))
            .addAnnotation(SafeVarargs::class.java)
            .build()
    }

}