package io.github.victorhsr.hermes.maven

import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.tools.Diagnostic

@SupportedAnnotationTypes("io.github.victorhsr.hermes.core.annotations.DSLRoot")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
class DSLProcessor : AbstractProcessor() {

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {

        val annotation = annotations.first()
        val annotatedElements = roundEnv.getElementsAnnotatedWith(annotation)

        val (annotatedClasses, annotatedOtherElements) = annotatedElements
            .map { it as DeclaredType }
            .partition { it.asElement().kind == ElementKind.CLASS }

        if (annotatedOtherElements.isNotEmpty()) {
            annotatedOtherElements.forEach {
                processingEnv.messager.printMessage(
                    Diagnostic.Kind.ERROR,
                    "@DSLRoot is supposed to be used in classes, but it was found in: $it"
                )
            }
        }

        annotatedClasses.get(0)
            .

        TODO("Not yet implemented")
    }
}