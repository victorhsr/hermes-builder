package io.github.victorhsr.hermes.core.gen

import com.squareup.javapoet.*
import io.github.victorhsr.hermes.core.AttributeInfo
import io.github.victorhsr.hermes.core.ClassInfo
import java.io.File
import java.util.*
import java.util.function.Consumer
import java.util.stream.Stream
import javax.lang.model.element.Modifier

class DSLGenerator {

    private fun buildConsumerType(clazz: Class<*>) =
        ParameterizedTypeName.get(ClassName.get(Consumer::class.java), TypeName.get(clazz))

    private fun buildConsumerArrayType(clazz: Class<*>) = ArrayTypeName.of(buildConsumerType(clazz))

    fun generate(classInfoList: List<ClassInfo>) {
        classInfoList.forEach(this::generate)
    }

    fun generate(classInfo: ClassInfo) {
        val methods = classInfo.attributes.map(this::buildMethod)
        val clazz = this.buildClass(classInfo, methods)

        val javaFile = JavaFile.builder("com.example.PersonDSL", clazz)
            .build();

        javaFile.writeTo(File("dsl"));
    }

    private fun buildMethod(attributeInfo: AttributeInfo): MethodSpec {
        if (attributeInfo.hasOptions) {
            return this.buildMethodWithOptions(attributeInfo)
        }
        return this.buildMethodWithoutOptions(attributeInfo)
    }

    private fun buildMethodWithoutOptions(attributeInfo: AttributeInfo): MethodSpec {
        return MethodSpec.methodBuilder(attributeInfo.name)
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .addParameter(attributeInfo.type, attributeInfo.name)
            .addCode(this.buildCodeBlock(attributeInfo))
            .returns(this.buildConsumerType(attributeInfo.wrapperClass))
            .build()
    }

    private fun buildMethodWithOptions(attributeInfo: AttributeInfo): MethodSpec {
        return MethodSpec.methodBuilder(attributeInfo.name)
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .addParameter(this.buildConsumerArrayType(attributeInfo.type), "options")
            .varargs()
            .addCode(this.buildCodeBlock(attributeInfo))
            .returns(this.buildConsumerType(attributeInfo.wrapperClass))
            .addAnnotation(SafeVarargs::class.java)
            .build()
    }

    private fun buildCodeBlock(attributeInfo: AttributeInfo): CodeBlock {
        if (attributeInfo.hasOptions) {
            return this.buildCodeBlockWithOptions(attributeInfo)
        }
        return this.buildCodeBlockWithoutOptions(attributeInfo)
    }

    private fun buildCodeBlockWithoutOptions(attributeInfo: AttributeInfo): CodeBlock {
        val wrapperSimpleName =
            attributeInfo.wrapperClass.simpleName.replaceFirstChar { it.lowercase(Locale.getDefault()) }

        return CodeBlock
            .builder()
            .addStatement("return (${wrapperSimpleName}) -> $wrapperSimpleName.${attributeInfo.methodName}(${attributeInfo.name})")
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
            .addStatement("return (${wrapperSimpleName}) -> $wrapperSimpleName.${attributeInfo.methodName}(${attributeInfo.name})")
            .build()
    }

    private fun buildRootMethod(classInfo: ClassInfo): MethodSpec {
        return MethodSpec.methodBuilder(classInfo.parameterName)
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(classInfo.type)
            .addParameter(this.buildConsumerArrayType(classInfo.type), "options")
            .varargs(true)
            .addCode(this.buildCodeBlock(classInfo))
            .addAnnotation(SafeVarargs::class.java)
            .build()
    }

    private fun buildCodeBlock(classInfo: ClassInfo): CodeBlock {
        val classDeclaration = classInfo.type.name

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

    private fun buildRootMethodCode(returnType: Class<*>): CodeBlock {
        val className = this.resolveClassName(returnType)
        val variableName = className.replaceFirstChar { it.lowercase() }

        return CodeBlock
            .builder()
            .addStatement("final $className $variableName = new $className()")
            .addStatement("\$T.of(options).forEach(option -> option.accept($variableName))", Stream::class.java)
            .addStatement("return $variableName")
            .build()
    }

    private fun buildClass(classInfo: ClassInfo, methods: List<MethodSpec>): TypeSpec {
        var methodsToUse = methods

        if (classInfo.isRoot) {
            methodsToUse = methods + this.buildRootMethod(classInfo)
        }

        return TypeSpec.classBuilder(this.resolveClassName(classInfo.type))
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addMethods(methodsToUse)
            .build();
    }

    private fun resolveClassName(clazz: Class<*>) = "${clazz.simpleName}DSL"

}