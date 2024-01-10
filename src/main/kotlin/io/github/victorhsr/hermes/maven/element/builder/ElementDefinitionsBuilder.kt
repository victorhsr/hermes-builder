package io.github.victorhsr.hermes.maven.element.builder

import io.github.victorhsr.hermes.maven.element.ClassElementDefinition
import io.github.victorhsr.hermes.maven.element.FieldElementDefinition
import javax.lang.model.element.TypeElement

class ElementDefinitionsBuilder {

    private val classElementDefinitionMap = mutableMapOf<String, ClassElementDefinition>()
    private val annotatedClassesMap = mutableListOf<String>();

    fun resolveElementDefinitions(annotatedClasses: List<TypeElement>): List<ClassElementDefinition> {
        annotatedClasses.forEach { annotatedClassesMap.add(it.asType().toString()) }
        annotatedClasses.forEach(::buildClassElementDefinitions)

        return classElementDefinitionMap.values.toList()
    }

    private fun buildClassElementDefinitions(typeElement: TypeElement) {
        val fullQualifiedClassName = typeElement.asType().toString()
        val classElementDefinition = ClassElementDefinition(typeElement, buildAccessibleFields(typeElement), true)
        classElementDefinitionMap[fullQualifiedClassName] = classElementDefinition
    }

    private fun buildAccessibleFields(clazz: TypeElement): List<FieldElementDefinition> {
        return FieldFinder
            .getFieldsFromClazz(clazz)
            .map { FieldElementDefinition(it, annotatedClassesMap.contains(it.asType().toString())) }
    }
}