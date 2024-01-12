package io.github.victorhsr.hermes.maven.element

import io.github.victorhsr.hermes.core.annotations.DSLProperty
import io.github.victorhsr.hermes.maven.util.TestName
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror

class FieldElementDefinitionTest {

    companion object {
        private const val PROPERTY_CUSTOM_NAME = "my-custom-name"
    }

    @Test
    fun `should build FieldElementDefinition from primitive Element`() {
        // given
        val fieldName = "field"
        val element: Element = mockElement(name = fieldName, typeMirror = mockType(true), hasCustomName = false)

        val expectedResult = FieldElementDefinition(
            fieldName = fieldName,
            customBuildName = null,
            declaredType = null,
            primitiveElement = element,
            shouldClassBeGenerated = false,
            isPrimitiveType = true
        )

        // when
        val actual = FieldElementDefinition(fieldElement = element, shouldClassBeGenerated = false)

        // then
        assertThat(actual).isEqualTo(expectedResult)
    }

    @Test
    fun `should build FieldElementDefinition`() {
        // given
        val fieldName = "field"
        val typeMirror: DeclaredType = mockType(false)
        val element: Element = mockElement(name = fieldName, typeMirror = typeMirror)

        val expectedResult = FieldElementDefinition(
            fieldName = fieldName,
            customBuildName = null,
            declaredType = typeMirror,
            primitiveElement = null,
            shouldClassBeGenerated = true,
            isPrimitiveType = false,
        )

        // when
        val actual = FieldElementDefinition(fieldElement = element, shouldClassBeGenerated = true)

        // then
        assertThat(actual).isEqualTo(expectedResult)
    }

    private fun mockElement(
        name: String,
        typeMirror: TypeMirror,
        hasCustomName: Boolean = false
    ): Element {
        val element: Element = mockk<Element>()

        every { element.kind } returns ElementKind.FIELD
        every { element.simpleName } returns TestName(name)
        every { element.getAnnotation(any<Class<DSLProperty>>()) } returns if (hasCustomName) mockDslPropertyAnnotation() else null
        every { element.asType() } returns typeMirror

        return element
    }

    private fun mockDslPropertyAnnotation(): DSLProperty {
        val dslProperty = mockk<DSLProperty>()
        every { dslProperty.value } returns PROPERTY_CUSTOM_NAME

        return dslProperty
    }

    private fun mockType(isPrimitive: Boolean): DeclaredType {
        val declaredType: DeclaredType = mockk<DeclaredType>()
        every { declaredType.kind } returns mockKind(isPrimitive)

        return declaredType
    }

    private fun mockKind(primitive: Boolean): TypeKind {
        val typeKind: TypeKind = mockk<TypeKind>()
        every { typeKind.isPrimitive } returns primitive

        return typeKind
    }

}