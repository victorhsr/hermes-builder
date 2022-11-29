package io.github.victorhsr.hermes.core

import io.github.victorhsr.hermes.core.gen.DSLGenerator
import io.github.victorhsr.hermes.core.reflection.ClassInfoBuilder
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.TypeElement

class HermesRunner(
    private val classInfoBuilder: ClassInfoBuilder,
    private val dslGenerator: DSLGenerator
) {

    fun genDSL(rootClasses: List<TypeElement>, processingEnv: ProcessingEnvironment) {
        val classInfoList = this.classInfoBuilder.processRootClasses(rootClasses, processingEnv)
//        this.dslGenerator.generate(classInfoList)
    }

}