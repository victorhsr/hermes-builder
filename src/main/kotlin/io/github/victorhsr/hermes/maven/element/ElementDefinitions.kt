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

class FieldElementDefinition {

    val fieldName: String
    val customBuildName: String?
    val declaredType: DeclaredType?
    val primitiveElement: Element?
    val shouldClassBeGenerated: Boolean
    val isPrimitiveType: Boolean

    constructor(
        fieldName: String,
        customBuildName: String?,
        declaredType: DeclaredType?,
        primitiveElement: Element?,
        shouldClassBeGenerated: Boolean,
        isPrimitiveType: Boolean
    ) {
        this.fieldName = fieldName
        this.customBuildName = customBuildName
        this.declaredType = declaredType
        this.primitiveElement = primitiveElement
        this.shouldClassBeGenerated = shouldClassBeGenerated
        this.isPrimitiveType = isPrimitiveType
    }

    constructor(fieldElement: Element, shouldClassBeGenerated: Boolean) {
        val isPrimitiveType: Boolean = fieldElement.asType().kind.isPrimitive
        val declaredType: DeclaredType? = if (!isPrimitiveType) fieldElement.asType() as DeclaredType else null

        this.fieldName = fieldElement.simpleName.toString()
        this.declaredType = declaredType
        this.primitiveElement = fieldElement
        this.isPrimitiveType = isPrimitiveType
        this.shouldClassBeGenerated = shouldClassBeGenerated
        this.customBuildName = fieldElement.getAnnotation(DSLProperty::class.java)?.value
    }

    override fun toString(): String {
        return "FieldElementDefinition(fieldName='$fieldName', customBuildName=$customBuildName, declaredType=$declaredType, primitiveElement=$primitiveElement, shouldClassBeGenerated=$shouldClassBeGenerated, isPrimitiveType=$isPrimitiveType)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FieldElementDefinition

        if (fieldName != other.fieldName) return false
        if (customBuildName != other.customBuildName) return false
        if (declaredType != other.declaredType) return false
        if (primitiveElement != other.primitiveElement) return false
        if (shouldClassBeGenerated != other.shouldClassBeGenerated) return false
        if (isPrimitiveType != other.isPrimitiveType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = fieldName.hashCode()
        result = 31 * result + (customBuildName?.hashCode() ?: 0)
        result = 31 * result + (declaredType?.hashCode() ?: 0)
        result = 31 * result + (primitiveElement?.hashCode() ?: 0)
        result = 31 * result + shouldClassBeGenerated.hashCode()
        result = 31 * result + isPrimitiveType.hashCode()
        return result
    }

}
