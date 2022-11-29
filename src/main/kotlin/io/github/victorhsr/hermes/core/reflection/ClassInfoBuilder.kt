package io.github.victorhsr.hermes.core.reflection

import io.github.victorhsr.hermes.core.AttributeInfo
import io.github.victorhsr.hermes.core.ClassInfo
import io.github.victorhsr.hermes.core.annotations.DSLIgnore
import java.util.*
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

class ClassInfoBuilder(private val attributeInfoBuilder: AttributeInfoBuilder) {

    private val classMap = mutableMapOf<String, ClassInfo>()

    fun processRootClasses(rootClasses: List<TypeElement>, processingEnv: ProcessingEnvironment): List<ClassInfo> {
        rootClasses.forEach { clazz -> this.processRootClass(clazz, processingEnv) }
        return classMap.values.toList()
    }

    private fun processRootClass(clazz: TypeElement, processingEnv: ProcessingEnvironment) {
        this.buildClassInfo(clazz, true, processingEnv)
    }

    private fun buildClassInfo(clazz: TypeElement, isRootClass: Boolean, processingEnv: ProcessingEnvironment) {

        val fullQualifiedClassName = clazz.qualifiedName.toString()

        if (this.classMap.containsKey(fullQualifiedClassName)) {
            return
        }

        val simpleName = clazz.simpleName.toString()

        val classInfo = ClassInfo(
            fullQualifiedName = fullQualifiedClassName,
            packageName = fullQualifiedClassName.substring(0, fullQualifiedClassName.lastIndexOf(".")),
            name = fullQualifiedClassName,
            parameterName = simpleName.replaceFirstChar { it.lowercase(Locale.getDefault()) },
            isRoot = isRootClass,
            attributes = this.buildAttributes(clazz, processingEnv)
        )

        println("classInfo = ${classInfo}")
        this.classMap[fullQualifiedClassName] = classInfo

        classInfo.attributes.forEach {
            if (it.hasOptions) {
                //       this.buildClassInfo(it.type, false)
            }
        }
    }

    private fun buildAttributes(clazz: TypeElement, processingEnv: ProcessingEnvironment): List<AttributeInfo> {
        return this.resolveFields(clazz)
            .filter { it.getAnnotation(DSLIgnore::class.java) == null }
            .map { this.attributeInfoBuilder.buildAttributeInfo(it, processingEnv) }
    }

    private fun resolveFields(clazz: TypeElement): List<Element> {

        val getMethodsMap = clazz.enclosedElements
            .filter { it.kind == ElementKind.METHOD }
            .filter { it.simpleName.startsWith("get") }
            .groupBy { it.simpleName.toString() }

        return clazz.enclosedElements
            .filter { it.kind.isField }
            .filter { getMethodsMap.containsKey(this.buildGetMethodName(it.simpleName.toString())) }
            .toList()
    }

    private fun buildGetMethodName(name: String): String {
        return "get${name.replaceFirstChar { it.titlecase(Locale.getDefault()) }}"
    }

}