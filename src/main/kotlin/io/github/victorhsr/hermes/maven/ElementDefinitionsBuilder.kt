package io.github.victorhsr.hermes.maven

import io.github.victorhsr.hermes.core.annotations.DSLIgnore
import java.util.*
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.util.ElementFilter

class ElementDefinitionsBuilder(private val processingEnvironment: ProcessingEnvironment) {

    private val classMap = mutableMapOf<String, ClassElementDefinitions>()

    fun resolveElementDefinitions(annotatedClasses: List<TypeElement>): List<ClassElementDefinitions> {
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

        classElementDefinitions.accessibleFields.forEach {
            if (it.shouldClassBeGenerated) {
                this.buildClassElementDefinitions(it.element as TypeElement, false)
            }
        }
    }

    private fun resolveAccessibleFields(clazz: TypeElement): List<FieldElementDefinitions> {
        return this.resolveFields(clazz).filter { it.getAnnotation(DSLIgnore::class.java) == null }
            .map { this.buildFieldElementDefinitions(it) }
    }

    private fun buildFieldElementDefinitions(fieldElement: Element): FieldElementDefinitions {
        val isPrimitiveType = fieldElement.asType().kind.isPrimitive
        val fieldClassElement =
            if (!isPrimitiveType) {
                this.processingEnvironment.elementUtils.getTypeElement(fieldElement.asType().toString())
            } else null

        val shouldClassBeGenerated = !isPrimitiveType
                && this.hasDefaultConstructor(fieldClassElement!!)
                && !this.isNativeClass(fieldClassElement!!)

        return FieldElementDefinitions(
            fieldName = fieldElement.simpleName.toString(),
            element = if (isPrimitiveType) fieldElement else fieldClassElement!!,
            isPrimitiveType = isPrimitiveType,
            shouldClassBeGenerated = shouldClassBeGenerated,
        )
    }

    private fun isNativeClass(element: TypeElement): Boolean {
        val qualifiedName = element.qualifiedName

        return (qualifiedName.startsWith("java.") || qualifiedName.startsWith("sun."))
    }

    private fun hasDefaultConstructor(element: TypeElement): Boolean {
        return ElementFilter.constructorsIn(element.enclosedElements).any { it.parameters.isEmpty() }
    }

    private fun resolveFields(clazz: TypeElement): List<Element> {

        val getMethodsMap =
            clazz.enclosedElements.filter { it.kind == ElementKind.METHOD }.filter { it.simpleName.startsWith("get") }
                .groupBy { it.simpleName.toString() }

        return clazz.enclosedElements.filter { it.kind.isField }
            .filter { getMethodsMap.containsKey(this.buildGetMethodName(it.simpleName.toString())) }.toList()
    }

    private fun buildGetMethodName(name: String): String {
        return "get${name.replaceFirstChar { it.titlecase(Locale.getDefault()) }}"
    }

}