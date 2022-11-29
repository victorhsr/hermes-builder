package io.github.victorhsr.hermes.maven

import io.github.victorhsr.hermes.core.annotations.DSLIgnore
import java.util.*
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

class ElementDefinitionsBuilder(private val processingEnvironment: ProcessingEnvironment) {

    private val classMap = mutableMapOf<String, ClassElementDefinitions>()

    fun resolveElementDefinitions(annotatedClasses: List<TypeElement>): List<ClassElementDefinitions> {
        // refactor to look like ClassInfoBuilder flow
        annotatedClasses.forEach(this::processAnnotatedClass)
        return this.classMap.values.toList()
    }

    private fun processAnnotatedClass(typeElement: TypeElement) {
        this.buildClassElementDefinitions(typeElement, true)
    }

    private fun buildClassElementDefinitions(typeElement: TypeElement, isAnnotatedClass: Boolean) {
        val fullQualifiedClassName = typeElement.asType().toString()

        if (this.classMap.containsKey(fullQualifiedClassName)) {
            return
        }

        val classElementDefinitions = ClassElementDefinitions(
            element = typeElement,
            wasAnnotated = isAnnotatedClass,
            accessibleFields = this.resolveAccessibleFields(typeElement)
        )

        this.classMap[fullQualifiedClassName] = classElementDefinitions

        classElementDefinitions.accessibleFields.forEach{

        }

    }

    private fun resolveAccessibleFields(clazz: TypeElement): List<FieldElementDefinitions> {
        return this.resolveFields(clazz)
            .filter { it.getAnnotation(DSLIgnore::class.java) == null }
            .map { this.buildFieldElementDefinitions(it) }
    }

    private fun buildFieldElementDefinitions(fieldElement: Element): FieldElementDefinitions {
        val fieldClassElement = this.processingEnvironment.elementUtils.getTypeElement(fieldElement.asType().toString())
        return FieldElementDefinitions(
            fieldName = fieldElement.simpleName.toString(),
            element = fieldClassElement,
            shouldClassBeGenerated = this.hasDefaultConstructor(fie) && !this.isNativeClass(),
        )
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