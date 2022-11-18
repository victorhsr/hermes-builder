package io.github.victorhsr.hermes.core.gen

import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import javax.lang.model.element.Modifier

class DSLGenerator {

    fun generate(classes: MutableSet<Class<*>>) {
        classes.forEach(this::generate)
    }

    private fun generate(clazz: Class<*>) {
        val className = this.resolveClassName(clazz)


        TODO("Implement it")
    }

    private fun generateMethod(methodName: String, returnType: Class<*>) {
        return MethodSpec.methodBuilder(methodName)
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(returnType)
            .addParameter(consumerArrayType, "options")
            .varargs(true)
            .addCode(codeBlock)
            .build()
    }

    private fun generateClass(className: String, methods: List<MethodSpec>): TypeSpec {
        return TypeSpec.classBuilder(className)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addMethods(methods)
            .build();
    }

    private fun resolveClassName(clazz: Class<*>) = "${clazz.simpleName}DSL"

}