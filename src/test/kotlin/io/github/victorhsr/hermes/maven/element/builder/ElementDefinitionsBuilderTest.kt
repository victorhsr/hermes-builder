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
import javax.lang.model.element.TypeParameterElement
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

    @MockK
    private lateinit var kFieldTypeElement: TypeElement

    private companion object {
        private const val PERSON_FULL_NAME = "PERSON_FULL_NAME"
        private const val PERSON_SIMPLE_NAME = "PERSON_SIMPLE_NAME"
        private const val PERSON_TYPE_NAME = "PERSON_TYPE_NAME"

        private const val ADDRESS_FULL_NAME = "ADDRESS_FULL_NAME"
        private const val ADDRESS_SIMPLE_NAME = "ADDRESS_SIMPLE_NAME"
        private const val ADDRESS_TYPE_NAME = "ADDRESS_TYPE_NAME"

        private const val NAME_FULL_NAME = "NAME_FULL_NAME"
        private const val NAME_SIMPLE_NAME = "NAME_SIMPLE_NAME"
        private const val NAME_TYPE_NAME = "NAME_TYPE_NAME"

        private const val STREET_FULL_NAME = "STREET_FULL_NAME"
        private const val STREET_SIMPLE_NAME = "STREET_SIMPLE_NAME"
        private const val STREET_TYPE_NAME = "STREET_TYPE_NAME"

        private const val K_FULL_NAME = "K_FULL_NAME"
        private const val K_SIMPLE_NAME = "K_SIMPLE_NAME"
        private const val K_TYPE_NAME = "K_TYPE_NAME"
    }

    @Test
    fun `resolveElementDefinitions should create the element definitions based on the class annotations`() {
        // given
        mockkObject(FieldFinder)
        every { FieldFinder.getFieldsFromClazz(personClassTypeElement) } returns listOf(
            nameFieldTypeElement,
            addressFieldTypeElement
        )
        every { FieldFinder.getFieldsFromClazz(addressClassTypeElement) } returns listOf(
            streetFieldTypeElement,
            kFieldTypeElement
        )

        mockTypeElement(
            personClassTypeElement,
            PERSON_FULL_NAME,
            PERSON_TYPE_NAME,
            PERSON_SIMPLE_NAME,
            listOf(nameFieldTypeElement, addressFieldTypeElement)
        )

        mockTypeElement(
            addressClassTypeElement,
            ADDRESS_FULL_NAME,
            ADDRESS_TYPE_NAME,
            ADDRESS_SIMPLE_NAME,
            listOf(streetFieldTypeElement),
            isPrimitive = false,
            isGenericType = true,
            genericTypeName = K_TYPE_NAME
        )
        mockTypeElement(nameFieldTypeElement, NAME_FULL_NAME, NAME_TYPE_NAME, NAME_SIMPLE_NAME)
        mockTypeElement(streetFieldTypeElement, STREET_FULL_NAME, STREET_TYPE_NAME, STREET_SIMPLE_NAME)
        mockTypeElement(
            kFieldTypeElement,
            K_FULL_NAME,
            K_TYPE_NAME,
            K_SIMPLE_NAME,
            isPrimitive = false,
            isGenericType = true,
            genericTypeName = K_TYPE_NAME
        )
        mockTypeElement(
            addressFieldTypeElement,
            ADDRESS_FULL_NAME,
            ADDRESS_FULL_NAME,
            ADDRESS_SIMPLE_NAME,
            listOf(),
            isPrimitive = false
        )

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
            fullQualifiedClassName = ADDRESS_FULL_NAME,
            accessibleFields = listOf(buildStreetElementDefinition(), buildKElementDefinition()),
            wasAnnotated = true
        )
    }

    private fun buildPersonClassElementDefinition(): ClassElementDefinition {
        return ClassElementDefinition(
            element = personClassTypeElement,
            fullQualifiedClassName = PERSON_FULL_NAME,
            accessibleFields = listOf(buildNameElementDefinition(), buildAddressElementDefinition()),
            wasAnnotated = true
        )
    }

    private fun buildStreetElementDefinition(): FieldElementDefinition {
        return FieldElementDefinition(
            fieldElement = streetFieldTypeElement,
            fullTypeName = STREET_TYPE_NAME,
            shouldClassBeGenerated = false,
            isPrimitiveType = true,
            isGenericType = false
        )
    }

    private fun buildKElementDefinition(): FieldElementDefinition {
        return FieldElementDefinition(
            fieldElement = kFieldTypeElement,
            fullTypeName = K_TYPE_NAME,
            shouldClassBeGenerated = false,
            isPrimitiveType = false,
            isGenericType = true
        )
    }

    private fun buildAddressElementDefinition(): FieldElementDefinition {
        return FieldElementDefinition(
            fieldElement = addressFieldTypeElement,
            fullTypeName = ADDRESS_FULL_NAME,
            shouldClassBeGenerated = true,
            isPrimitiveType = false,
            isGenericType = false
        )
    }

    private fun buildNameElementDefinition(): FieldElementDefinition {
        return FieldElementDefinition(
            fieldElement = nameFieldTypeElement,
            fullTypeName = NAME_TYPE_NAME,
            shouldClassBeGenerated = false,
            isPrimitiveType = true,
            isGenericType = false
        )
    }

    private fun mockTypeElement(
        typeElement: TypeElement,
        fullName: String,
        typeName: String,
        typeSimpleName: String? = null,
        fields: List<TypeElement> = listOf(),
        isPrimitive: Boolean = true,
        isGenericType: Boolean = false,
        genericTypeName: String? = null
    ) {
        every { typeElement.asType() } returns mockTypeMirror(typeName, isPrimitive)
        every { typeElement.enclosedElements } returns fields
        every { typeElement.simpleName } returns if (typeSimpleName != null) mockName(typeSimpleName) else null
        every { typeElement.getAnnotation(any<Class<DSLProperty>>()) } returns null
        every { typeElement.toString() } returns fullName
        every { typeElement.typeParameters } returns if (isGenericType) buildGenericTypeParameter(genericTypeName!!) else listOf()
    }

    private fun buildGenericTypeParameter(fullName: String): List<TypeParameterElement> {
        val typeParameterElement: TypeParameterElement = mockk<TypeParameterElement>()
        every { typeParameterElement.toString() } returns fullName

        return listOf(typeParameterElement)
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