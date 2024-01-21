package io.github.victorhsr.hermes.maven.element

import io.github.victorhsr.hermes.core.annotations.DSLProperty
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType

data class ClassElementDefinition(
    val element: TypeElement,
    val fullQualifiedClassName: String,
    val accessibleFields: List<FieldElementDefinition>,
    val wasAnnotated: Boolean
)

data class FieldElementDefinition(
    val fieldName: String,
    val fullTypeName: String,
    val customBuildName: String?,
    val shouldClassBeGenerated: Boolean,
    val isPrimitiveType: Boolean,
    val fieldElement: Element,
    val isGenericType: Boolean
) {
    constructor(
        fieldElement: Element,
        fullTypeName: String,
        shouldClassBeGenerated: Boolean,
        isPrimitiveType: Boolean,
        isGenericType: Boolean
    ) : this(
        fieldElement = fieldElement,
        fullTypeName = fullTypeName,
        fieldName = fieldElement.simpleName.toString(),
        customBuildName = fieldElement.getAnnotation(DSLProperty::class.java)?.value,
        shouldClassBeGenerated = shouldClassBeGenerated,
        isPrimitiveType = isPrimitiveType,
        isGenericType = isGenericType
    )
}
