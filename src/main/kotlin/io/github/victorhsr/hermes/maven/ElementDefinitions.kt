package io.github.victorhsr.hermes.maven

import javax.lang.model.element.Element

data class ClassElementDefinitions(
    val element: Element,
    val accessibleFields: List<FieldElementDefinitions>,
    val wasAnnotated: Boolean
)

data class FieldElementDefinitions(
    val fieldName: String,
    val element: Element,
    val shouldClassBeGenerated: Boolean
)