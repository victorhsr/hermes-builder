package io.github.victorhsr.hermes.core.gen

import com.squareup.javapoet.*
import io.github.victorhsr.hermes.core.ClassInfo
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

    private fun buildMemberMethod(methodName: String, parameterClass: Class<*>, returnClass: Class<*>): MethodSpec? {

        return MethodSpec.methodBuilder(methodName)
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(this.buildConsumerType(returnClass))
            .addParameter(this.buildConsumerArrayType(parameterClass), "options")
            .varargs(true)
            .build()
    }

    private fun buildRootMethod(methodName: String, returnType: Class<*>): MethodSpec? {
        return MethodSpec.methodBuilder(methodName)
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(returnType)
            .addParameter(this.buildConsumerArrayType(returnType), "options")
            .varargs(true)
            .addCode(this.buildRootMethodCode(returnType))
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

    private fun buildClass(className: String, methods: List<MethodSpec>): TypeSpec {
        return TypeSpec.classBuilder(className)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addMethods(methods)
            .build();
    }

    private fun resolveClassName(clazz: Class<*>) = "${clazz.simpleName}DSL"

}