package io.github.victorhsr.hermes.core

import io.github.victorhsr.hermes.core.gen.DSLGenerator
import javax.annotation.processing.Filer

class HermesRunner(private val dslGenerator: DSLGenerator) {

    fun genDSL(classInfoList: List<ClassInfo>, filer: Filer) {
        this.dslGenerator.generate(classInfoList, filer)
    }

}