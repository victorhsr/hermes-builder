package com.github.victorhsr.hermes.maven

import com.github.victorhsr.hermes.maven.DSLProcessor.Companion.DSL_ROOT_QUALIFIED_NAME
import com.github.victorhsr.hermes.maven.element.builder.ElementDefinitionsBuilder
import com.google.auto.service.AutoService
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.util.ElementFilter
import javax.tools.Diagnostic

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@SupportedAnnotationTypes(DSL_ROOT_QUALIFIED_NAME)
class DSLProcessor : AbstractProcessor() {

    companion object {
        const val DSL_ROOT_QUALIFIED_NAME = "com.github.victorhsr.hermes.core.annotations.DSLRoot"
    }

    override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {

        val dslGenerator = com.github.victorhsr.hermes.core.DSLGeneratorFactory.create()
        val elementDefinitionsBuilder = ElementDefinitionsBuilder()
        val classInfoBuilder = ClassInfoBuilder()

        val (annotatedClasses, annotatedOtherElements) = this.separateAnnotatedElements(roundEnv, annotations)

        if (annotatedOtherElements.isNotEmpty()) {
            this.logInvalidElements(annotatedOtherElements)
        } else {
            val elementDefinitions = elementDefinitionsBuilder.resolveElementDefinitions(annotatedClasses)
            val classInfoList = classInfoBuilder.build(elementDefinitions)
            dslGenerator.generate(classInfoList, this.processingEnv.getFiler())
        }
        return true;
    }

    private fun separateAnnotatedElements(
        roundEnv: RoundEnvironment,
        annotations: Set<TypeElement>
    ): Pair<List<TypeElement>, List<TypeElement>> {
        val annotation = annotations.firstOrNull() ?: return Pair(listOf(), listOf())

        return roundEnv
            .getElementsAnnotatedWith(annotation)
            .map { it as TypeElement }
            .partition(this::isClassWithDefaultConstructor)
    }

    private fun logInvalidElements(annotatedOtherElements: List<TypeElement>) {

        annotatedOtherElements.forEach {
            processingEnv.messager.printMessage(
                Diagnostic.Kind.ERROR,
                "$DSL_ROOT_QUALIFIED_NAME is supposed to be used in classes with default constructor, but it was found in: $it"
            )
        }
    }

    private fun isClassWithDefaultConstructor(element: TypeElement): Boolean {
        return element.kind == ElementKind.CLASS
                &&
                ElementFilter.constructorsIn(element.enclosedElements).any {
                    it.modifiers.contains(Modifier.PUBLIC) && it.parameters.isEmpty()
                }
    }
}