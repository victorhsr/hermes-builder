package io.github.victorhsr.hermes.maven.element.builder

import io.github.victorhsr.hermes.core.annotations.DSLIgnore
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Name
import javax.lang.model.element.TypeElement

class FieldFinderTest {

    companion object {
        private const val SET_NAME_METHOD_NAME = "setName"
        private const val SET_AGE_METHOD_NAME = "setAge"
        private const val DO_SOMETHING_METHOD_NAME = "doSomething"
        private const val NAME_FIELD_NAME = "name"
        private const val AGE_FIELD_NAME = "age"
        private const val ADDRESS_FIELD_NAME = "address"
    }

    @Test
    fun `should filter elements that are fields and have 'set' methods and ignore the ones annoted with DSLIgnore`() {
        // given
        val clazz: TypeElement = mockk<TypeElement>()

        val nameSetMethod = mockElement(ElementKind.METHOD, SET_NAME_METHOD_NAME)
        val ageSetMethod = mockElement(ElementKind.METHOD, SET_AGE_METHOD_NAME)
        val doSomethingMethod = mockElement(ElementKind.METHOD, DO_SOMETHING_METHOD_NAME)
        val nameField = mockElement(ElementKind.FIELD, NAME_FIELD_NAME)
        val ageField = mockElement(ElementKind.FIELD, AGE_FIELD_NAME, true)
        val addressField = mockElement(ElementKind.FIELD, ADDRESS_FIELD_NAME)

        val enclosedElements = listOf(nameField, nameSetMethod, ageField, ageSetMethod, doSomethingMethod, addressField)
        val expectedResult = listOf(nameField)

        every { clazz.enclosedElements } returns enclosedElements

        // when
        val actual: List<Element> = FieldFinder.getFieldsFromClazz(clazz)

        // then
        assertThat(actual).hasSameElementsAs(expectedResult)
    }

    private fun mockElement(elementKind: ElementKind, name: String, shouldBeIgnored: Boolean = false): Element {
        val element: Element = mockk<Element>()

        every { element.kind } returns elementKind
        every { element.simpleName } returns TestName(name)
        every { element.getAnnotation(any<Class<Annotation>>()) } returns if (shouldBeIgnored) mockk<DSLIgnore>() else null

        return element
    }

}

class TestName(private val nameValue: String) : Name {

    override val length: Int
        get() = nameValue.length

    override fun contentEquals(cs: CharSequence) = nameValue == cs.toString()

    override fun get(index: Int) = nameValue[index]

    override fun subSequence(start: Int, end: Int) = nameValue.subSequence(start, end)

    override fun hashCode() = nameValue.hashCode()

    override fun equals(obj: Any?) = nameValue == obj.toString()

    override fun toString() = nameValue
}
