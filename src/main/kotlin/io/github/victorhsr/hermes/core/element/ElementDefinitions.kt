package io.github.victorhsr.hermes.core.element

import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

data class ClassElementDefinition(
    val element: TypeElement,
    val accessibleFields: List<FieldElementDefinition>,
    val wasAnnotated: Boolean
)

data class FieldElementDefinition(
    val fieldName: String,
    val customBuildName: String?,
    val element: Element,
    val shouldClassBeGenerated: Boolean,
    val isPrimitiveType: Boolean,
)