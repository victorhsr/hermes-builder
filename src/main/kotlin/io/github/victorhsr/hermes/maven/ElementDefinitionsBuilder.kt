package io.github.victorhsr.hermes.maven

import io.github.victorhsr.hermes.core.annotations.DSLIgnore
import io.github.victorhsr.hermes.core.annotations.DSLProperty
import java.util.*
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.util.ElementFilter

class ElementDefinitionsBuilder(private val processingEnvironment: ProcessingEnvironment) {

    private val classMap = mutableMapOf<String, ClassElementDefinition>()

    fun resolveElementDefinitions(annotatedClasses: List<TypeElement>): List<ClassElementDefinition> {
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

        val classElementDefinition = ClassElementDefinition(
            element = typeElement,
            wasAnnotated = isAnnotatedClass,
            accessibleFields = this.resolveAccessibleFields(typeElement)
        )

        this.classMap[fullQualifiedClassName] = classElementDefinition

        classElementDefinition.accessibleFields.forEach {
            if (it.shouldClassBeGenerated) {
                this.buildClassElementDefinitions(it.element as TypeElement, false)
            }
        }
    }

    private fun resolveAccessibleFields(clazz: TypeElement): List<FieldElementDefinition> {
        return this.resolveFields(clazz).filter { it.getAnnotation(DSLIgnore::class.java) == null }
            .map { this.buildFieldElementDefinitions(it) }
    }

    private fun buildFieldElementDefinitions(fieldElement: Element): FieldElementDefinition {
        val isPrimitiveType = fieldElement.asType().kind.isPrimitive
        val fieldClassElement =
            if (!isPrimitiveType) {
                this.processingEnvironment.elementUtils.getTypeElement(fieldElement.asType().toString())
            } else null

        val shouldClassBeGenerated = !isPrimitiveType
                && this.hasDefaultConstructor(fieldClassElement!!)
                && !this.isNativeClass(fieldClassElement!!)

        var customBuildName: String? = null

        val dslPropertyAnnotation = fieldElement.getAnnotation(DSLProperty::class.java)
        if (dslPropertyAnnotation != null) {
            customBuildName = dslPropertyAnnotation.value
        }

        return FieldElementDefinition(
            fieldName = fieldElement.simpleName.toString(),
            element = if (isPrimitiveType) fieldElement else fieldClassElement!!,
            isPrimitiveType = isPrimitiveType,
            shouldClassBeGenerated = shouldClassBeGenerated,
            customBuildName = customBuildName
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

        val setMethodsMap =
            clazz.enclosedElements.filter { it.kind == ElementKind.METHOD }.filter { it.simpleName.startsWith("set") }
                .groupBy { it.simpleName.toString() }

        return clazz.enclosedElements.filter { it.kind.isField }
            .filter { setMethodsMap.containsKey(this.buildSetMethodName(it.simpleName.toString())) }.toList()
    }

    private fun buildSetMethodName(name: String): String {
        return "set${name.replaceFirstChar { it.titlecase(Locale.getDefault()) }}"
    }

}