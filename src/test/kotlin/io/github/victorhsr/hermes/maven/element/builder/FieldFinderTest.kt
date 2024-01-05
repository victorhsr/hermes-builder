package io.github.victorhsr.hermes.maven.element.builder

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Name
import javax.lang.model.element.TypeElement
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class FieldFinderTest {

    companion object {
        private const val SET_METHOD_PREFIX = "set"
    }

    @Mock
    private lateinit var clazz: TypeElement

    @Test
    fun `should filter elements that are fields and have 'set' methods`() {
        // given
        val expectedResult = mock(List::class.java)

        val methods: List<Element> = listOf(
            mockElement(ElementKind.METHOD, "setName"),
            mockElement(ElementKind.METHOD, "doSomething"),
            mockElement(ElementKind.METHOD, "setAge")
        )

        val fields: List<Element> = listOf(
            mockElement(ElementKind.FIELD, "name"),
            mockElement(ElementKind.FIELD, "age")
        )

        val enclosedElements = methods + fields
        `when`(clazz.enclosedElements).thenReturn(enclosedElements)

        // when
        val actual: List<Element> = FieldFinder.getFieldsFromClazz(clazz)

        // then
        assertEquals(actual, expectedResult)
    }

    private fun mockElement(elementKind: ElementKind, simpleNameString: String): Element {
        val simpleName: Name = mockName(simpleNameString)
        val element: Element = mock(Element::class.java)
        `when`(element.kind).thenReturn(elementKind)
        `when`(element.simpleName).thenReturn(simpleName)

        return element
    }

    private fun mockName(nameContent: String): Name {
        val name = mock(Name::class.java)
        `when`(name.startsWith(SET_METHOD_PREFIX)).thenReturn(true)
        `when`(name.toString()).thenReturn(nameContent)

        return name
    }

}