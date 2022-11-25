package io.github.victorhsr.hermes.core

import io.github.victorhsr.hermes.core.gen.DSLGenerator
import io.github.victorhsr.hermes.core.reflection.ClassInfoBuilder
import io.github.victorhsr.hermes.core.reflection.Scanner
import java.io.File

class HermesRunner(
    private val scanner: Scanner,
    private val classInfoBuilder: ClassInfoBuilder,
    private val dslGenerator: DSLGenerator
) {

    fun genDSLForPackage(classpath: List<String>, packageToScan: String, output: File) {
        val rootClasses = this.scanner.lookForDSLRootClasses(classpath, packageToScan)
        val classInfoList = this.classInfoBuilder.processRootClasses(rootClasses)
        this.dslGenerator.generate(classInfoList, output)
    }

}