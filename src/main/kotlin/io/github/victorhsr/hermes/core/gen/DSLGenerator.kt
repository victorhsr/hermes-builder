package io.github.victorhsr.hermes.core.gen

import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import io.github.victorhsr.hermes.core.ClassInfo
import javax.annotation.processing.Filer
import javax.lang.model.element.Modifier

class DSLGenerator(private val methodSpecGenerator: MethodSpecGenerator) {

    fun generate(classInfoList: List<ClassInfo>, filer: Filer) {
        classInfoList.forEach { this.generate(it, filer) }
    }

    private fun generate(classInfo: ClassInfo, filer: Filer) {
        val methods = classInfo.attributes.map(this.methodSpecGenerator::buildMethod)
        val clazz = this.buildClass(classInfo, methods)

        val javaFile = JavaFile.builder(classInfo.packageName, clazz).build();
        javaFile.writeTo(filer)
    }

    private fun buildClass(classInfo: ClassInfo, methods: List<MethodSpec>): TypeSpec {
        var methodsToUse = methods

        if (classInfo.isRoot) {
            methodsToUse = methods + this.methodSpecGenerator.buildRootMethod(classInfo)
        }

        return TypeSpec.classBuilder("${classInfo.simpleName}DSL")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addMethods(methodsToUse)
            .build();
    }

}