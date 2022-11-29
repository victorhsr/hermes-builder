package io.github.victorhsr.hermes.maven

import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

data class ClassElementDefinitions(
    val element: TypeElement,
    val accessibleFields: List<FieldElementDefinitions>,
    val wasAnnotated: Boolean
)

data class FieldElementDefinitions(
    val fieldName: String,
    val element: Element,
    val shouldClassBeGenerated: Boolean,
    val isPrimitiveType: Boolean
)