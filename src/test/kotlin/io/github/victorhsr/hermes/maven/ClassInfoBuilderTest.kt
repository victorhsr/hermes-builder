package io.github.victorhsr.hermes.maven

import io.github.victorhsr.hermes.core.AttributeInfo
import io.github.victorhsr.hermes.core.ClassInfo
import io.github.victorhsr.hermes.core.ext.uncapitalize
import io.github.victorhsr.hermes.maven.element.ClassElementDefinition
import io.github.victorhsr.hermes.maven.element.FieldElementDefinition
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import javax.lang.model.element.Element
import javax.lang.model.element.Name
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeMirror

class ClassInfoBuilderTest {

    private companion object {
        const val SET_PREFIX = "set"
        const val PERSON_TYPE_NAME = "PERSON_TYPE_NAME"
        const val AGE_SIMPLE_NAME = "AGE_SIMPLE_NAME"
        const val NAME_TYPE_NAME = "NAME_TYPE_NAME"
        const val PERSON_TYPE_FULL_QUALIFIED_NAME = "PACKAGE.PERSON_TYPE_FULL_QUALIFIED_NAME"
        const val NAME_SIMPLE_NAME = "NAME_SIMPLE_NAME"
        const val AGE_TYPE_NAME = "AGE_TYPE_NAME"
    }

    @Test
    fun `build test`() {
        // given
        val expectedResult: List<ClassInfo> = buildExpectedResult()
        val classElementDefinitions: List<ClassElementDefinition> = listOf(mockPerson())

        // when
        val actual = ClassInfoBuilder().build(classElementDefinitions)

        // then
        assertThat(actual).isEqualTo(expectedResult)
    }

    private fun buildExpectedResult(): List<ClassInfo> {
        return listOf(
            ClassInfo(
                fullQualifiedName = PERSON_TYPE_FULL_QUALIFIED_NAME,
                parameterName = PERSON_TYPE_NAME.uncapitalize(),
                isRoot = true,
                attributes = listOf(
                    buildNameAttributeInfo(),
                    buildAgeAttributeInfo()
                )
            )
        )
    }

    private fun buildAgeAttributeInfo(): AttributeInfo {
        return AttributeInfo(
            name = AGE_SIMPLE_NAME,
            setterMethodName = SET_PREFIX + AGE_SIMPLE_NAME,
            type = AGE_TYPE_NAME,
            wrapperClass = PERSON_TYPE_FULL_QUALIFIED_NAME,
            hasOptions = false,
            buildMethodName = AGE_SIMPLE_NAME
        )
    }

    private fun buildNameAttributeInfo(): AttributeInfo {
        return AttributeInfo(
            name = NAME_SIMPLE_NAME,
            setterMethodName = SET_PREFIX + NAME_SIMPLE_NAME,
            type = NAME_TYPE_NAME,
            wrapperClass = PERSON_TYPE_FULL_QUALIFIED_NAME,
            hasOptions = false,
            buildMethodName = NAME_SIMPLE_NAME
        )
    }

    private fun mockPerson(): ClassElementDefinition {
        return ClassElementDefinition(
            element = mockPersonClassTypeElement(),
            accessibleFields = listOf(buildNameElementDefinition(), buildAgeElementDefinition()),
            wasAnnotated = true
        )
    }

    private fun mockPersonClassTypeElement(): TypeElement {
        val personTypeElement = mockk<TypeElement>()
        every { personTypeElement.qualifiedName } returns mockName(PERSON_TYPE_FULL_QUALIFIED_NAME)
        every { personTypeElement.simpleName } returns mockName(PERSON_TYPE_NAME)

        return personTypeElement
    }

    private fun mockName(nameString: String): Name {
        val name = mockk<Name>()
        every { name.toString() } returns nameString

        return name
    }

    private fun buildNameElementDefinition(): FieldElementDefinition {
        return FieldElementDefinition(
            fieldName = NAME_SIMPLE_NAME,
            customBuildName = null,
            declaredType = mockDeclaredType(NAME_TYPE_NAME),
            primitiveElement = null,
            shouldClassBeGenerated = false,
            isPrimitiveType = false
        )
    }

    private fun buildAgeElementDefinition(): FieldElementDefinition {
        return FieldElementDefinition(
            fieldName = AGE_SIMPLE_NAME,
            customBuildName = null,
            declaredType = null,
            primitiveElement = mockElement(AGE_TYPE_NAME),
            shouldClassBeGenerated = false,
            isPrimitiveType = true
        )
    }

    private fun mockElement(typeName: String): Element {
        val element = mockk<Element>()
        every { element.asType() } returns mockTypeMirror(typeName)

        return element
    }

    private fun mockTypeMirror(typeName: String): TypeMirror {
        val typeMirror = mockk<TypeMirror>()
        every { typeMirror.toString() } returns typeName

        return typeMirror
    }

    private fun mockDeclaredType(declaredTypeName: String): DeclaredType {
        val declaredType = mockk<DeclaredType>()
        every { declaredType.toString() } returns declaredTypeName

        return declaredType
    }
}