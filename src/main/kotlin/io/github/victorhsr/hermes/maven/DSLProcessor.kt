package io.github.victorhsr.hermes.maven

import com.google.auto.service.AutoService
import io.github.victorhsr.hermes.core.HermesRunnerFactory
import io.github.victorhsr.hermes.maven.DSLProcessor.Companion.DSL_ROOT_QUALIFIED_NAME
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@SupportedAnnotationTypes(DSL_ROOT_QUALIFIED_NAME)
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@AutoService(Processor::class)
class DSLProcessor : AbstractProcessor() {

    companion object {
        const val DSL_ROOT_QUALIFIED_NAME = "io.github.victorhsr.hermes.core.annotations.DSLRoot"
    }

    private val hermesRunner = HermesRunnerFactory.create()
    private val elementDefinitionsBuilder = ElementDefinitionsBuilder()

    override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {

        val (annotatedClasses, annotatedOtherElements) = this.separateAnnotatedElements(roundEnv, annotations)
        println("annotatedClasses = ${annotatedClasses}")
        println("an = ${annotatedOtherElements}")

        if (annotatedOtherElements.isNotEmpty()) {
            this.logInvalidElements(annotatedOtherElements)
        }

        this.elementDefinitionsBuilder.resolveElementDefinitions(annotatedClasses)

        this.hermesRunner.genDSL(annotatedClasses, this.processingEnv)
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