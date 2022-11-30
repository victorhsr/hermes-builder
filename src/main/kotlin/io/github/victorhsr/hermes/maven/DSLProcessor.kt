package io.github.victorhsr.hermes.maven

import com.google.auto.service.AutoService
import io.github.victorhsr.hermes.core.HermesRunnerFactory
import io.github.victorhsr.hermes.core.element.ElementDefinitionsBuilder
import io.github.victorhsr.hermes.maven.DSLProcessor.Companion.DSL_ROOT_QUALIFIED_NAME
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@SupportedAnnotationTypes(DSL_ROOT_QUALIFIED_NAME)
class DSLProcessor : AbstractProcessor() {

    companion object {
        const val DSL_ROOT_QUALIFIED_NAME = "io.github.victorhsr.hermes.core.annotations.DSLRoot"
    }

    override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {

        val hermesRunner = HermesRunnerFactory.create()
        val elementDefinitionsBuilder = ElementDefinitionsBuilder(this.processingEnv)

        val (annotatedClasses, annotatedOtherElements) = this.separateAnnotatedElements(roundEnv, annotations)

        if (annotatedOtherElements.isNotEmpty()) {
            this.logInvalidElements(annotatedOtherElements)
        }

        val elementDefinitions = elementDefinitionsBuilder.resolveElementDefinitions(annotatedClasses)
        hermesRunner.genDSL(elementDefinitions, this.processingEnv.getFiler())

        return false;
    }

    private fun separateAnnotatedElements(
        roundEnv: RoundEnvironment,
        annotations: Set<TypeElement>
    ): Pair<List<TypeElement>, List<TypeElement>> {
        val annotation = annotations.firstOrNull() ?: return Pair(listOf(), listOf())

        return roundEnv
            .getElementsAnnotatedWith(annotation)
            .map { it as TypeElement }
            .partition { it.kind == ElementKind.CLASS }
    }

    private fun logInvalidElements(annotatedOtherElements: List<TypeElement>) {
        annotatedOtherElements.forEach {
            processingEnv.messager.printMessage(
                Diagnostic.Kind.ERROR,
                "${DSL_ROOT_QUALIFIED_NAME} is supposed to be used in classes, but it was found in: $it"
            )
        }
    }
}