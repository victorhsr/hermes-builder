package io.github.victorhsr.hermes.maven.element

import io.github.victorhsr.hermes.core.annotations.DSLProperty
import javax.lang.model.element.Element

data class ClassElementDefinition(
    val simpleName: String,
    val fullQualifiedClassName: String,
    val accessibleFields: List<FieldElementDefinition>,
    val wasAnnotated: Boolean,
) {
    constructor(
        element: Element,
        fullQualifiedClassName: String,
        accessibleFields: List<FieldElementDefinition>,
        wasAnnotated: Boolean,
    ) : this(
        simpleName = element.simpleName.toString(),
        fullQualifiedClassName = fullQualifiedClassName,
        accessibleFields = accessibleFields,
        wasAnnotated = wasAnnotated
    )
}

data class FieldElementDefinition(
    val fieldName: String,
    val fullTypeName: String,
    val customBuildName: String?,
    val shouldClassBeGenerated: Boolean,
    val isPrimitiveType: Boolean,
    val isGenericType: Boolean
) {
    constructor(
        fieldElement: Element,
        fullTypeName: String,
        shouldClassBeGenerated: Boolean,
        isPrimitiveType: Boolean,
        isGenericType: Boolean
    ) : this(
        fullTypeName = fullTypeName,
        fieldName = fieldElement.simpleName.toString(),
        customBuildName = fieldElement.getAnnotation(DSLProperty::class.java)?.value,
        shouldClassBeGenerated = shouldClassBeGenerated,
        isPrimitiveType = isPrimitiveType,
        isGenericType = isGenericType
    )
}
