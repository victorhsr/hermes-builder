package io.github.victorhsr.hermes.maven

import io.github.victorhsr.hermes.core.AttributeInfo
import io.github.victorhsr.hermes.core.ClassInfo
import myCapitalize
import java.util.*

class ClassInfoBuilder {

    fun processRootClasses(rootClasses: List<ClassElementDefinition>): List<ClassInfo> {
        return rootClasses.map(this::buildClassInfo)
    }

    private fun buildClassInfo(classDefinition: ClassElementDefinition): ClassInfo {
        val fullQualifiedClassName = classDefinition.element.qualifiedName.toString()
        val simpleName = classDefinition.element.simpleName.toString()

        return ClassInfo(
            fullQualifiedName = fullQualifiedClassName,
            parameterName = simpleName.replaceFirstChar { it.lowercase(Locale.getDefault()) },
            isRoot = classDefinition.wasAnnotated,
            attributes = classDefinition.accessibleFields.map { this.buildAttributeInfo(classDefinition, it) }
        )
    }

    private fun buildAttributeInfo(
        parentClassElementDefinition: ClassElementDefinition,
        fieldDefinition: FieldElementDefinition
    ): AttributeInfo {

        return AttributeInfo(
            name = fieldDefinition.fieldName,
            buildMethodName = fieldDefinition.customBuildName ?: fieldDefinition.fieldName,
            setterMethodName = "set${fieldDefinition.fieldName.myCapitalize()}",
            type = fieldDefinition.element.asType().toString(),
            wrapperClass = parentClassElementDefinition.element.qualifiedName.toString(),
            hasOptions = fieldDefinition.shouldClassBeGenerated,
        )
    }

}