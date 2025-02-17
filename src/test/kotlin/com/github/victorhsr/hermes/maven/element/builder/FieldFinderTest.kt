package com.github.victorhsr.hermes.maven.element.builder

import com.github.victorhsr.hermes.core.annotations.DSLIgnore
import com.github.victorhsr.hermes.maven.util.TestName
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType

class FieldFinderTest {

    @Nested
    inner class PlainJavaTests {
        @Test
        fun `getFieldsFromClazz should identify fields that have a setter method`() {
            // given
            val fieldWithSetterName = "fieldA";
            val fieldWithoutSetterName = "fieldB";
            val setterName = "setFieldA"
            val fieldWithSetter = mockElement(ElementKind.FIELD, fieldWithSetterName)
            val fieldWithoutSetter = mockElement(ElementKind.FIELD, fieldWithoutSetterName)
            val setterMethod = mockElement(ElementKind.METHOD, setterName)
            val clazz = mockClazz(fieldWithSetter, fieldWithoutSetter, setterMethod)
            val expected = listOf(fieldWithSetter)

            // when
            val actual = FieldFinder.getFieldsFromClazz(clazz)

            // then
            assertThat(actual).isEqualTo(expected)
        }

        @Test
        fun `getFieldsFromClazz should filter classes annotated with DSLIgnore out`() {
            // given
            val fieldWithSetterName = "fieldA";
            val fieldWithoutSetterName = "fieldB";
            val setterName = "setFieldA"
            val fieldWithSetter = mockElement(ElementKind.FIELD, fieldWithSetterName, true)
            val fieldWithoutSetter = mockElement(ElementKind.FIELD, fieldWithoutSetterName)
            val setterMethod = mockElement(ElementKind.METHOD, setterName)
            val clazz = mockClazz(fieldWithSetter, fieldWithoutSetter, setterMethod)
            val expected = listOf<Element>()

            // when
            val actual = FieldFinder.getFieldsFromClazz(clazz)

            // then
            assertThat(actual).isEqualTo(expected)
        }
    }


    @Nested
    inner class LombokTests {

        @Nested
        inner class ClazzLevelAnnotationTests {

            @Test
            fun `getFieldsFromClazz should identify @Setter on class level`() {
                // given
                val fieldWithSetterName = "fieldA";
                val fieldWithSetter = mockElement(ElementKind.FIELD, fieldWithSetterName)
                val clazz = mockClazz("lombok.Setter", fieldWithSetter)
                val expected = listOf(fieldWithSetter)

                // when
                val actual = FieldFinder.getFieldsFromClazz(clazz)

                // then
                assertThat(actual).isEqualTo(expected)
            }

            @Test
            fun `getFieldsFromClazz should identify @Data on class level`() {
                // given
                val fieldWithSetterName = "fieldA";
                val anotherFieldWithSetterName = "fieldB";
                val fieldWithSetter = mockElement(ElementKind.FIELD, fieldWithSetterName)
                val anotherFieldWithSetter = mockElement(ElementKind.FIELD, anotherFieldWithSetterName)
                val clazz = mockClazz("lombok.Data", fieldWithSetter, anotherFieldWithSetter)
                val expected = listOf(fieldWithSetter, anotherFieldWithSetter)

                // when
                val actual = FieldFinder.getFieldsFromClazz(clazz)

                // then
                assertThat(actual).isEqualTo(expected)
            }
        }

        @Nested
        inner class FiledLevelAnnotationTests {
            @Test
            fun `getFieldsFromClazz should identify @Setter on class level`() {
                // given
                val fieldWithSetterName = "fieldA";
                val fieldWithoutSetterName = "fieldB";
                val fieldWithSetter = mockElement("lombok.Setter", ElementKind.FIELD, fieldWithSetterName)
                val fieldWithoutSetter = mockElement(ElementKind.FIELD, fieldWithoutSetterName)
                val clazz = mockClazz(fieldWithSetter, fieldWithoutSetter)
                val expected = listOf(fieldWithSetter)

                // when
                val actual = FieldFinder.getFieldsFromClazz(clazz)

                // then
                assertThat(actual).isEqualTo(expected)
            }
        }
    }

    private fun mockElement(elementKind: ElementKind, name: String, shouldBeIgnored: Boolean = false): Element {
        return mockElement(null, elementKind, name, shouldBeIgnored)
    }

        private fun mockElement(annotation: String?, elementKind: ElementKind, name: String, shouldBeIgnored: Boolean = false): Element {
        val element: Element = mockk<Element>()

        every { element.kind } returns elementKind
        every { element.simpleName } returns TestName(name)
        every { element.annotationMirrors } returns
                if (annotation == null) mutableListOf()
                else mutableListOf(mockAnnotationMirror(annotation))
        every { element.getAnnotation(any<Class<DSLIgnore>>()) } returns if (shouldBeIgnored) mockk<DSLIgnore>() else null

        return element
    }

    private fun mockClazz(vararg field: Element): TypeElement {
        return mockClazz(null, *field)
    }

    private fun mockClazz(annotation: String? = null, vararg field: Element): TypeElement {
        val typeElement = mockk<TypeElement>()
        every { typeElement.enclosedElements } returns field.toList()
        every { typeElement.annotationMirrors } returns
                if (annotation == null) mutableListOf()
                else mutableListOf(mockAnnotationMirror(annotation))
        every { typeElement.getAnnotation(any<Class<DSLIgnore>>()) } returns null

        return typeElement
    }

    private fun mockAnnotationMirror(annotation: String): AnnotationMirror {
        val annotationMirror = mockk<AnnotationMirror>()
        every { annotationMirror.annotationType } returns mockAnnotationType(annotation)

        return annotationMirror
    }

    private fun mockAnnotationType(annotation: String): DeclaredType {
        val declaredType = mockk<DeclaredType>()
        every { declaredType.toString() } returns annotation

        return declaredType
    }
}
