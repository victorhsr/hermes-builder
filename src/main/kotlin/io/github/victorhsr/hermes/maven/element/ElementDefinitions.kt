package io.github.victorhsr.hermes.maven.element

import io.github.victorhsr.hermes.core.annotations.DSLProperty
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
    val primitiveElement: Element?,
    val shouldClassBeGenerated: Boolean,
    val isPrimitiveType: Boolean
) {
    constructor(fieldElement: Element, shouldClassBeGenerated: Boolean) : this(
        fieldName = fieldElement.simpleName.toString(),
        customBuildName = fieldElement.getAnnotation(DSLProperty::class.java)?.value,
        declaredType = if (!fieldElement.asType().kind.isPrimitive) fieldElement.asType() as DeclaredType else null,
        primitiveElement = if (fieldElement.asType().kind.isPrimitive) fieldElement else null,
        shouldClassBeGenerated = shouldClassBeGenerated,
        isPrimitiveType = fieldElement.asType().kind.isPrimitive
    )
}
