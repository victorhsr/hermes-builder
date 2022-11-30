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

    fun buildClassType(fullyQualifiedClassName: String): TypeName {
        val lastDotIndex = fullyQualifiedClassName.lastIndexOf(".")

        if (lastDotIndex > -1) {
            val packageName = fullyQualifiedClassName.substring(0, lastDotIndex)
            val className = fullyQualifiedClassName
                .substring(lastDotIndex + 1, fullyQualifiedClassName.length)

            return ClassName.get(packageName, className)
        }

        return TypeVariableName.get(fullyQualifiedClassName)
    }

    private fun buildConsumerType(fullyQualifiedClassName: String): ParameterizedTypeName {
        val className = this.buildClassType(fullyQualifiedClassName)
        return ParameterizedTypeName.get(ClassName.get(Consumer::class.java), className)
    }

    private fun buildConsumerArrayType(fullyQualifiedClassName: String): ArrayTypeName {
        return ArrayTypeName.of(this.buildConsumerType(fullyQualifiedClassName))
    }

    private fun buildMethodWithoutOptions(attributeInfo: AttributeInfo): MethodSpec {
        return MethodSpec.methodBuilder(attributeInfo.buildMethodName)
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .addParameter(this.buildClassType(attributeInfo.type), attributeInfo.name)
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
            .returns(this.buildClassType(classInfo.fullQualifiedName))
            .addParameter(this.buildConsumerArrayType(classInfo.fullQualifiedName), "options")
            .varargs(true)
            .addCode(this.codeBlockGenerator.buildCodeBlock(classInfo))
            .addAnnotation(SafeVarargs::class.java)
            .build()
    }

}