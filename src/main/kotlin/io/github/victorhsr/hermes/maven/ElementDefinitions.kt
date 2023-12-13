package io.github.victorhsr.hermes.maven

import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType

data class ClassElementDefinition(
    val element: TypeElement,
    val accessibleFields: List<FieldElementDefinition>,
    val wasAnnotated: Boolean
)

data class FieldElementDefinition(
    val fieldName: String,
    val customBuildName: String?,
    val declaredType: DeclaredType?,
    val primitiveElement : Element?,
    val shouldClassBeGenerated: Boolean,
    val isPrimitiveType: Boolean,
)