package io.github.victorhsr.hermes.maven

import io.github.victorhsr.hermes.core.ClassInfo
import io.github.victorhsr.hermes.core.annotations.DSLRoot
import io.github.victorhsr.hermes.core.gen.DSLGenerator
import io.github.victorhsr.hermes.maven.element.ClassElementDefinition
import io.github.victorhsr.hermes.maven.element.builder.ElementDefinitionsBuilder
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

class DSLProcessorTest {

    @Test
    fun `should generate dsl`() {
        // given
        mockkConstructor(ElementDefinitionsBuilder::class)
        mockkConstructor(ClassInfoBuilder::class)
        mockkConstructor(DSLGenerator::class)
        mockkConstructor(ProcessingEnvironment::class)

        val annotation: TypeElement = mockk<TypeElement>()
        val annotatedElement: TypeElement = mockAnnotatedElement()
        val roundEnv: RoundEnvironment = mockRoundEnv(annotation, annotatedElement)
        val elementDefinitions: List<ClassElementDefinition> = mockk<List<ClassElementDefinition>>()
        val classInfoList: List<ClassInfo> = mockk<List<ClassInfo>>()
        val processingEnvironment: ProcessingEnvironment = mockk<ProcessingEnvironment>()
        val filer: Filer = mockk<Filer>()

        every { anyConstructed<ElementDefinitionsBuilder>().resolveElementDefinitions(listOf(annotatedElement)) } returns elementDefinitions
        every { anyConstructed<ClassInfoBuilder>().build(elementDefinitions) } returns classInfoList
        every { processingEnvironment.filer } returns filer
        every { anyConstructed<DSLGenerator>().generate(any(), any()) } returns Unit

        val dslProcessor = DSLProcessor()
        dslProcessor.init(processingEnvironment)

        // when
        val processingResult: Boolean = dslProcessor.process(setOf(annotation), roundEnv)

        // then
        verify { anyConstructed<DSLGenerator>().generate(classInfoList, filer) }
        assertTrue(processingResult)
    }

    @Test
    fun `should log invalid annotated elements`() {
        // given
        mockkConstructor(ProcessingEnvironment::class)

        val annotation: TypeElement = mockk<TypeElement>()
        val annotatedElement: TypeElement = mockAnnotatedElementWithWrongKind()
        val roundEnv: RoundEnvironment = mockRoundEnv(annotation, annotatedElement)
        val processingEnvironment: ProcessingEnvironment = mockk<ProcessingEnvironment>()
        val filer: Filer = mockk<Filer>()
        val messager: Messager = mockk<Messager>()

        every { processingEnvironment.filer } returns filer
        every { processingEnvironment.messager } returns messager
        every { messager.printMessage(any(), any()) } returns Unit

        val dslProcessor = DSLProcessor()
        dslProcessor.init(processingEnvironment)

        // when
        val processingResult: Boolean = dslProcessor.process(setOf(annotation), roundEnv)

        // then
        verify {
            messager.printMessage(
                eq(Diagnostic.Kind.ERROR),
                eq("${DSLRoot::class.qualifiedName} is supposed to be used in classes, but it was found in: $annotatedElement")
            )
        }
        assertTrue(processingResult)
    }

    private fun mockRoundEnv(annotation: TypeElement, annotatedElement: TypeElement): RoundEnvironment {
        val roundEnvironment = mockk<RoundEnvironment>()
        every { roundEnvironment.getElementsAnnotatedWith(annotation) } returns mutableSetOf(annotatedElement)

        return roundEnvironment
    }

    private fun mockAnnotatedElement(): TypeElement {
        val typeElement = mockk<TypeElement>()
        every { typeElement.kind } returns ElementKind.CLASS

        return typeElement
    }

    private fun mockAnnotatedElementWithWrongKind(): TypeElement {
        val typeElement = mockk<TypeElement>()
        every { typeElement.kind } returns ElementKind.FIELD

        return typeElement
    }

}