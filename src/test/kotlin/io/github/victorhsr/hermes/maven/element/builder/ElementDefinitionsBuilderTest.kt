package io.github.victorhsr.hermes.maven.element.builder

import io.github.victorhsr.hermes.core.annotations.DSLProperty
import io.github.victorhsr.hermes.maven.element.ClassElementDefinition
import io.github.victorhsr.hermes.maven.element.FieldElementDefinition
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.mockkObject
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import javax.lang.model.element.Name
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror

@ExtendWith(MockKExtension::class)
class ElementDefinitionsBuilderTest {

    @MockK
    private lateinit var personClassTypeElement: TypeElement

    @MockK
    private lateinit var addressClassTypeElement: TypeElement

    @MockK
    private lateinit var addressFieldTypeElement: TypeElement

    @MockK
    private lateinit var nameFieldTypeElement: TypeElement

    @MockK
    private lateinit var streetFieldTypeElement: TypeElement

    private companion object {
        const val PERSON_TYPE_NAME = "PERSON_TYPE_NAME"
        const val ADDRESS_TYPE_NAME = "ADDRESS_TYPE_NAME"
        const val NAME_SIMPLE_NAME = "NAME_SIMPLE_NAME"
        const val ADDRESS_SIMPLE_NAME = "ADDRESS_SIMPLE_NAME"
        const val STREET_SIMPLE_NAME = "STREET_SIMPLE_NAME"
        const val NAME_TYPE_NAME = "NAME_TYPE_NAME"
        const val STREET_TYPE_NAME = "STREET_TYPE_NAME"
    }

    @Test
    fun `resolveElementDefinitions should create the element definitions based on the class annotations`() {
        // given
        mockkObject(FieldFinder)
        every { FieldFinder.getFieldsFromClazz(personClassTypeElement) } returns listOf(nameFieldTypeElement, addressFieldTypeElement)
        every { FieldFinder.getFieldsFromClazz(addressClassTypeElement) } returns listOf(streetFieldTypeElement)

        mockTypeElement(personClassTypeElement, PERSON_TYPE_NAME, fields = listOf(nameFieldTypeElement, addressFieldTypeElement))
        mockTypeElement(addressClassTypeElement, ADDRESS_TYPE_NAME, fields = listOf(streetFieldTypeElement))
        mockTypeElement(nameFieldTypeElement, NAME_TYPE_NAME, NAME_SIMPLE_NAME)
        mockTypeElement(streetFieldTypeElement, STREET_TYPE_NAME, STREET_SIMPLE_NAME)
        mockTypeElement(addressFieldTypeElement, ADDRESS_TYPE_NAME, ADDRESS_SIMPLE_NAME)

        val expectedResult: List<ClassElementDefinition> = buildExpectedResult()
        val annotatedClasses: List<TypeElement> = listOf(personClassTypeElement, addressClassTypeElement)
        val elementDefinitionsBuilder = ElementDefinitionsBuilder()

        // when
        val actual = elementDefinitionsBuilder.resolveElementDefinitions(annotatedClasses)

        // then
        assertThat(actual).isEqualTo(expectedResult)
    }

    private fun buildExpectedResult(): List<ClassElementDefinition> {
        return listOf(
            buildPersonClassElementDefinition(),
            buildAddressClassElementDefinition()
        )
    }

    private fun buildAddressClassElementDefinition(): ClassElementDefinition {
        return ClassElementDefinition(
            element = addressClassTypeElement,
            accessibleFields = listOf(buildStreetElementDefinition()),
            wasAnnotated = true
        )
    }

    private fun buildPersonClassElementDefinition(): ClassElementDefinition {
        return ClassElementDefinition(
            element = personClassTypeElement,
            accessibleFields = listOf(buildNameElementDefinition(), buildAddressElementDefinition()),
            wasAnnotated = true
        )
    }

    private fun buildStreetElementDefinition(): FieldElementDefinition {
        return FieldElementDefinition(
            fieldName = STREET_SIMPLE_NAME,
            customBuildName = null,
            declaredType = null,
            primitiveElement = streetFieldTypeElement,
            shouldClassBeGenerated = false,
            isPrimitiveType = true
        )
    }

    private fun buildAddressElementDefinition(): FieldElementDefinition {
        return FieldElementDefinition(
            fieldName = ADDRESS_SIMPLE_NAME,
            customBuildName = null,
            declaredType = null,
            primitiveElement = addressFieldTypeElement,
            shouldClassBeGenerated = true,
            isPrimitiveType = true
        )
    }

    private fun buildNameElementDefinition(): FieldElementDefinition {
        return FieldElementDefinition(
            fieldName = NAME_SIMPLE_NAME,
            customBuildName = null,
            declaredType = null,
            primitiveElement = nameFieldTypeElement,
            shouldClassBeGenerated = false,
            isPrimitiveType = true
        )
    }

    private fun mockTypeElement(
        typeElement: TypeElement,
        typeName: String,
        typeSimpleName: String? = null,
        fields: List<TypeElement> = listOf(),
        isPrimitive: Boolean = true
    ) {
        every { typeElement.asType() } returns mockTypeMirror(typeName, isPrimitive)
        every { typeElement.enclosedElements } returns fields
        every { typeElement.simpleName } returns if (typeSimpleName != null) mockName(typeSimpleName) else null
        every { typeElement.getAnnotation(any<Class<DSLProperty>>()) } returns null
    }

    private fun mockName(personSimpleName: String): Name {
        val name = mockk<Name>()
        every { name.toString() } returns personSimpleName

        return name
    }

    private fun mockTypeMirror(typeAsString: String, isPrimitive: Boolean): TypeMirror {
        val typeMirror: TypeMirror = mockk<TypeMirror>()
        every { typeMirror.toString() } returns typeAsString
        every { typeMirror.kind } returns mockTypeKind(isPrimitive)

        return typeMirror
    }

    private fun mockTypeKind(isPrimitive: Boolean): TypeKind {
        val typeKind = mockk<TypeKind>()
        every { typeKind.isPrimitive } returns isPrimitive

        return typeKind
    }

}