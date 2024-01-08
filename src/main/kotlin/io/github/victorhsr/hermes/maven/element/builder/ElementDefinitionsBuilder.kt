package io.github.victorhsr.hermes.maven.element.builder

import io.github.victorhsr.hermes.maven.element.ClassElementDefinition
import io.github.victorhsr.hermes.maven.element.FieldElementDefinition
import javax.lang.model.element.TypeElement

class ElementDefinitionsBuilder {

    private val classElementDefinitionMap = mutableMapOf<String, ClassElementDefinition>()
    private val annotatedClassesMap = mutableListOf<String>();

    fun resolveElementDefinitions(annotatedClasses: List<TypeElement>): List<ClassElementDefinition> {
        annotatedClasses.forEach { annotatedClassesMap.add(it.asType().toString()) }
        annotatedClasses.forEach(this::buildClassElementDefinitions)
        return this.classElementDefinitionMap.values.toList()
    }

    private fun buildClassElementDefinitions(typeElement: TypeElement) {
        val fullQualifiedClassName = typeElement.asType().toString()
        val classElementDefinition = buildClassElementDefinition(typeElement, true);
        this.classElementDefinitionMap[fullQualifiedClassName] = classElementDefinition

        classElementDefinition.accessibleFields.forEach {
            if (it.shouldClassBeGenerated) {
                this.buildClassElementDefinitionsForNestedFields(it.declaredType!!.asElement() as TypeElement)
            }
        }
    }

    private fun buildClassElementDefinitionsForNestedFields(typeElement: TypeElement) {
        val fullQualifiedClassName = typeElement.asType().toString()

        if (this.classElementDefinitionMap.containsKey(fullQualifiedClassName)) {
            return
        }

        val classElementDefinition = buildClassElementDefinition(typeElement, false);
        this.classElementDefinitionMap[fullQualifiedClassName] = classElementDefinition
    }

    private fun buildClassElementDefinition(
        typeElement: TypeElement,
        isAnnotatedClass: Boolean
    ): ClassElementDefinition {
        return ClassElementDefinition(
            element = typeElement,
            wasAnnotated = isAnnotatedClass,
            accessibleFields = this.buildAccessibleFields(typeElement)
        )
    }

    private fun buildAccessibleFields(clazz: TypeElement): List<FieldElementDefinition> {
        return FieldFinder
            .getFieldsFromClazz(clazz)
            .map { FieldElementDefinition(it, annotatedClassesMap.contains(it.asType().toString())) }
    }
}