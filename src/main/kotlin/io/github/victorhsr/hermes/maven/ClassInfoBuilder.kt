package io.github.victorhsr.hermes.maven

import io.github.victorhsr.hermes.core.AttributeInfo
import io.github.victorhsr.hermes.core.ClassInfo
import io.github.victorhsr.hermes.core.ext.myCapitalize
import io.github.victorhsr.hermes.maven.element.ClassElementDefinition
import io.github.victorhsr.hermes.maven.element.FieldElementDefinition
import java.util.*

class ClassInfoBuilder {

    companion object {
        private const val OBJECT_TYPE_NAME = "java.lang.Object"
    }

    fun build(classElementDefinitions: List<ClassElementDefinition>): List<ClassInfo> {
        return classElementDefinitions.map(this::buildClassInfo)
    }

    private fun buildClassInfo(classDefinition: ClassElementDefinition): ClassInfo {
        return ClassInfo(
            fullQualifiedName = classDefinition.fullQualifiedClassName,
            parameterName = classDefinition.simpleName.replaceFirstChar { it.lowercase(Locale.getDefault()) },
            isRoot = classDefinition.wasAnnotated,
            attributes = classDefinition.accessibleFields.map { this.buildAttributeInfo(classDefinition, it) }
        )
    }

    private fun buildAttributeInfo(
        parentClassElementDefinition: ClassElementDefinition,
        fieldDefinition: FieldElementDefinition
    ): AttributeInfo {

        val typeName: String = if (fieldDefinition.isGenericType) {
            OBJECT_TYPE_NAME
        } else {
            fieldDefinition.fullTypeName
        }

        return AttributeInfo(
            name = fieldDefinition.fieldName,
            buildMethodName = fieldDefinition.customBuildName ?: fieldDefinition.fieldName,
            setterMethodName = "set${fieldDefinition.fieldName.myCapitalize()}",
            type = typeName,
            wrapperClass = parentClassElementDefinition.fullQualifiedClassName,
            hasOptions = fieldDefinition.shouldClassBeGenerated,
        )
    }
}