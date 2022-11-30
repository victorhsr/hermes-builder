package io.github.victorhsr.hermes.core

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.victorhsr.hermes.core.element.ClassElementDefinition
import io.github.victorhsr.hermes.core.gen.DSLGenerator
import javax.annotation.processing.Filer

class HermesRunner(
    private val classInfoBuilder: ClassInfoBuilder,
    private val dslGenerator: DSLGenerator
) {

    fun genDSL(elementDefinitions: List<ClassElementDefinition>, filer: Filer) {
        val classInfoList = this.classInfoBuilder.processRootClasses(elementDefinitions)
        this.dslGenerator.generate(classInfoList, filer)
    }

}