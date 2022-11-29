package io.github.victorhsr.hermes.core.gen

import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import io.github.victorhsr.hermes.core.ClassInfo
import java.io.File
import javax.lang.model.element.Modifier

class DSLGenerator(private val methodSpecGenerator: MethodSpecGenerator) {

    fun generate(classInfoList: List<ClassInfo>, output: File) {
        classInfoList.forEach { this.generate(it, output) }
    }

    private fun generate(classInfo: ClassInfo, output: File) {
        val methods = classInfo.attributes.map(this.methodSpecGenerator::buildMethod)
        val clazz = this.buildClass(classInfo, methods)

//        val javaFile = JavaFile.builder(classInfo.type.packageName, clazz)
//            .build();
//
//        javaFile.writeTo(output);
//        println("written ${classInfo.name} to ${output}")
    }

    private fun buildClass(classInfo: ClassInfo, methods: List<MethodSpec>): TypeSpec {
        var methodsToUse = methods

        if (classInfo.isRoot) {
            methodsToUse = methods + this.methodSpecGenerator.buildRootMethod(classInfo)
        }

        return TypeSpec.classBuilder("").build()

//        return TypeSpec.classBuilder(this.resolveClassName(classInfo.type))
//            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
//            .addMethods(methodsToUse)
//            .build();
    }

    private fun resolveClassName(clazz: Class<*>) = "${clazz.simpleName}DSL"

}