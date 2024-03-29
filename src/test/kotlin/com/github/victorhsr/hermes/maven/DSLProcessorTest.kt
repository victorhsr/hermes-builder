package com.github.victorhsr.hermes.maven

import com.github.victorhsr.hermes.core.annotations.DSLRoot
import com.github.victorhsr.hermes.core.gen.DSLGenerator
import com.github.victorhsr.hermes.maven.element.ClassElementDefinition
import com.github.victorhsr.hermes.maven.element.builder.ElementDefinitionsBuilder
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.util.ElementFilter
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
        val classInfoList: List<com.github.victorhsr.hermes.core.ClassInfo> = mockk<List<com.github.victorhsr.hermes.core.ClassInfo>>()
        val processingEnvironment: ProcessingEnvironment = mockk<ProcessingEnvironment>()
        val filer: Filer = mockk<Filer>()
        val publicConstructor: ExecutableElement = mockPublicConstructor()

        every { anyConstructed<ElementDefinitionsBuilder>().resolveElementDefinitions(listOf(annotatedElement)) } returns elementDefinitions
        every { anyConstructed<ClassInfoBuilder>().build(elementDefinitions) } returns classInfoList
        every { processingEnvironment.filer } returns filer
        every { anyConstructed<DSLGenerator>().generate(any(), any()) } returns Unit

        mockkStatic(ElementFilter::class)
        every { ElementFilter.constructorsIn(annotatedElement.enclosedElements) } returns listOf(publicConstructor)

        val dslProcessor = DSLProcessor()
        dslProcessor.init(processingEnvironment)

        // when
        val processingResult: Boolean = dslProcessor.process(setOf(annotation), roundEnv)

        // then
        verify { anyConstructed<DSLGenerator>().generate(classInfoList, filer) }
        assertTrue(processingResult)
    }

    private fun mockPublicConstructor(): ExecutableElement {
        val executableElement = mockk<ExecutableElement>()
        every { executableElement.modifiers } returns setOf(Modifier.PUBLIC)
        every { executableElement.parameters } returns listOf()

        return executableElement
    }

    @Test
    fun `should log invalid annotated elements (not a class)`() {
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
                eq("${DSLRoot::class.qualifiedName} is supposed to be used in classes with default constructor, but it was found in: $annotatedElement")
            )
        }
        assertTrue(processingResult)
    }

    @Test
    fun `should log invalid annotated elements (don't have public constructor)`() {
        // given
        mockkConstructor(ProcessingEnvironment::class)

        val annotation: TypeElement = mockk<TypeElement>()
        val annotatedElement: TypeElement = mockAnnotatedElement()
        val roundEnv: RoundEnvironment = mockRoundEnv(annotation, annotatedElement)
        val processingEnvironment: ProcessingEnvironment = mockk<ProcessingEnvironment>()
        val filer: Filer = mockk<Filer>()
        val messager: Messager = mockk<Messager>()

        every { processingEnvironment.filer } returns filer
        every { processingEnvironment.messager } returns messager
        every { messager.printMessage(any(), any()) } returns Unit

        mockkStatic(ElementFilter::class)
        every { ElementFilter.constructorsIn(annotatedElement.enclosedElements) } returns listOf()

        val dslProcessor = DSLProcessor()
        dslProcessor.init(processingEnvironment)

        // when
        val processingResult: Boolean = dslProcessor.process(setOf(annotation), roundEnv)

        // then
        verify {
            messager.printMessage(
                eq(Diagnostic.Kind.ERROR),
                eq("${DSLRoot::class.qualifiedName} is supposed to be used in classes with default constructor, but it was found in: $annotatedElement")
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