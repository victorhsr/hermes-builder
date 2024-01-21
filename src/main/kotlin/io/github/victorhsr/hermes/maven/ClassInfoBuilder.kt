package io.github.victorhsr.hermes.maven

import io.github.victorhsr.hermes.core.AttributeInfo
import io.github.victorhsr.hermes.core.ClassInfo
import io.github.victorhsr.hermes.core.ext.myCapitalize
import io.github.victorhsr.hermes.maven.element.ClassElementDefinition
import io.github.victorhsr.hermes.maven.element.FieldElementDefinition
import java.util.*

class ClassInfoBuilder {

    fun build(classElementDefinitions: List<ClassElementDefinition>): List<ClassInfo> {
        return classElementDefinitions.map(this::buildClassInfo)
    }

    private fun buildClassInfo(classDefinition: ClassElementDefinition): ClassInfo {
        val simpleName = classDefinition.element.simpleName.toString()

        return ClassInfo(
            fullQualifiedName = classDefinition.fullQualifiedClassName,
            parameterName = simpleName.replaceFirstChar { it.lowercase(Locale.getDefault()) },
            isRoot = classDefinition.wasAnnotated,
            attributes = classDefinition.accessibleFields.map { this.buildAttributeInfo(classDefinition, it) }
        )
    }

    private fun buildAttributeInfo(
        parentClassElementDefinition: ClassElementDefinition,
        fieldDefinition: FieldElementDefinition
    ): AttributeInfo {

        val typeName: String = if (fieldDefinition.isGenericType) {
            "java.lang.Object"
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