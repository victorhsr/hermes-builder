package com.github.victorhsr.hermes.maven

import com.github.victorhsr.hermes.core.ext.myCapitalize
import com.github.victorhsr.hermes.maven.element.ClassElementDefinition
import com.github.victorhsr.hermes.maven.element.FieldElementDefinition
import java.util.*

class ClassInfoBuilder {

    companion object {
        private const val OBJECT_TYPE_NAME = "java.lang.Object"
    }

    fun build(classElementDefinitions: List<ClassElementDefinition>): List<com.github.victorhsr.hermes.core.ClassInfo> {
        return classElementDefinitions.map(this::buildClassInfo)
    }

    private fun buildClassInfo(classDefinition: ClassElementDefinition): com.github.victorhsr.hermes.core.ClassInfo {
        return com.github.victorhsr.hermes.core.ClassInfo(
            fullQualifiedName = classDefinition.fullQualifiedClassName,
            parameterName = classDefinition.simpleName.replaceFirstChar { it.lowercase(Locale.getDefault()) },
            isRoot = classDefinition.wasAnnotated,
            attributes = classDefinition.accessibleFields.map { this.buildAttributeInfo(classDefinition, it) }
        )
    }

    private fun buildAttributeInfo(
        parentClassElementDefinition: ClassElementDefinition,
        fieldDefinition: FieldElementDefinition
    ): com.github.victorhsr.hermes.core.AttributeInfo {

        val typeName: String = if (fieldDefinition.isGenericType) {
            OBJECT_TYPE_NAME
        } else {
            fieldDefinition.fullTypeName
        }

        return com.github.victorhsr.hermes.core.AttributeInfo(
            name = fieldDefinition.fieldName,
            buildMethodName = fieldDefinition.customBuildName ?: fieldDefinition.fieldName,
            setterMethodName = "set${fieldDefinition.fieldName.myCapitalize()}",
            type = typeName,
            wrapperClass = parentClassElementDefinition.fullQualifiedClassName,
            hasOptions = fieldDefinition.shouldClassBeGenerated,
        )
    }
}