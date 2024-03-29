package com.github.victorhsr.hermes.maven

import com.github.victorhsr.hermes.core.annotations.DSLProperty
import com.github.victorhsr.hermes.core.ext.uncapitalize
import com.github.victorhsr.hermes.maven.element.ClassElementDefinition
import com.github.victorhsr.hermes.maven.element.FieldElementDefinition
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import javax.lang.model.element.Element
import javax.lang.model.element.Name
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror

class ClassInfoBuilderTest {

    private companion object {
        private const val SET_PREFIX = "set"
        private const val OBJECT_TYPE_NAME = "java.lang.Object"

        private const val PERSON_TYPE_NAME = "PERSON_TYPE_NAME"
        private const val PERSON_TYPE_FULL_QUALIFIED_NAME = "PACKAGE.PERSON_TYPE_FULL_QUALIFIED_NAME"

        private const val AGE_SIMPLE_NAME = "AGE_SIMPLE_NAME"
        private const val AGE_TYPE_NAME = "AGE_TYPE_NAME"

        private const val NAME_TYPE_NAME = "NAME_TYPE_NAME"
        private const val NAME_SIMPLE_NAME = "NAME_SIMPLE_NAME"

        private const val K_TYPE_NAME = "K_TYPE_NAME"
        private const val K_SIMPLE_NAME = "K_SIMPLE_NAME"
    }

    @Test
    fun `build test`() {
        // given
        val expectedResult: List<com.github.victorhsr.hermes.core.ClassInfo> = buildExpectedResult()
        val classElementDefinitions: List<ClassElementDefinition> = listOf(mockPerson())

        // when
        val actual = ClassInfoBuilder().build(classElementDefinitions)

        // then
        assertThat(actual).isEqualTo(expectedResult)
    }

    private fun buildExpectedResult(): List<com.github.victorhsr.hermes.core.ClassInfo> {
        return listOf(
            com.github.victorhsr.hermes.core.ClassInfo(
                fullQualifiedName = PERSON_TYPE_FULL_QUALIFIED_NAME,
                parameterName = PERSON_TYPE_NAME.uncapitalize(),
                isRoot = true,
                attributes = listOf(
                    buildNameAttributeInfo(),
                    buildAgeAttributeInfo(),
                    buildKAttributeInfo()
                )
            )
        )
    }

    private fun buildAgeAttributeInfo(): com.github.victorhsr.hermes.core.AttributeInfo {
        return com.github.victorhsr.hermes.core.AttributeInfo(
            name = AGE_SIMPLE_NAME,
            setterMethodName = SET_PREFIX + AGE_SIMPLE_NAME,
            type = AGE_TYPE_NAME,
            wrapperClass = PERSON_TYPE_FULL_QUALIFIED_NAME,
            hasOptions = false,
            buildMethodName = AGE_SIMPLE_NAME
        )
    }

    private fun buildNameAttributeInfo(): com.github.victorhsr.hermes.core.AttributeInfo {
        return com.github.victorhsr.hermes.core.AttributeInfo(
            name = NAME_SIMPLE_NAME,
            setterMethodName = SET_PREFIX + NAME_SIMPLE_NAME,
            type = NAME_TYPE_NAME,
            wrapperClass = PERSON_TYPE_FULL_QUALIFIED_NAME,
            hasOptions = false,
            buildMethodName = NAME_SIMPLE_NAME
        )
    }

    private fun buildKAttributeInfo(): com.github.victorhsr.hermes.core.AttributeInfo {
        return com.github.victorhsr.hermes.core.AttributeInfo(
            name = K_SIMPLE_NAME,
            setterMethodName = SET_PREFIX + K_SIMPLE_NAME,
            type = OBJECT_TYPE_NAME,
            wrapperClass = PERSON_TYPE_FULL_QUALIFIED_NAME,
            hasOptions = false,
            buildMethodName = K_SIMPLE_NAME
        )
    }

    private fun mockPerson(): ClassElementDefinition {
        return ClassElementDefinition(
            element = mockPersonClassTypeElement(),
            fullQualifiedClassName = PERSON_TYPE_FULL_QUALIFIED_NAME,
            accessibleFields = listOf(
                buildNameElementDefinition(),
                buildAgeElementDefinition(),
                buildKElementDefinition()
            ),
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
            fullTypeName = NAME_TYPE_NAME,
            fieldName = NAME_SIMPLE_NAME,
            customBuildName = null,
            shouldClassBeGenerated = false,
            isPrimitiveType = false,
            isGenericType = false
        )
    }

    private fun buildKElementDefinition(): FieldElementDefinition {
        return FieldElementDefinition(
            fullTypeName = K_TYPE_NAME,
            fieldName = K_SIMPLE_NAME,
            customBuildName = null,
            shouldClassBeGenerated = false,
            isPrimitiveType = false,
            isGenericType = true
        )
    }

    private fun buildAgeElementDefinition(): FieldElementDefinition {
        return FieldElementDefinition(
            fieldElement = mockElement(AGE_SIMPLE_NAME, AGE_TYPE_NAME),
            fullTypeName = AGE_TYPE_NAME,
            shouldClassBeGenerated = false,
            isPrimitiveType = true,
            isGenericType = false
        )
    }

    private fun mockElement(simpleName: String, typeName: String): Element {
        val element = mockk<Element>()
        every { element.asType() } returns mockTypeMirror(typeName)
        every { element.simpleName } returns mockName(simpleName)
        every { element.getAnnotation(any<Class<DSLProperty>>()) } returns null

        return element
    }

    private fun mockTypeMirror(typeName: String): TypeMirror {
        val typeMirror = mockk<TypeMirror>()
        every { typeMirror.toString() } returns typeName

        return typeMirror
    }
}