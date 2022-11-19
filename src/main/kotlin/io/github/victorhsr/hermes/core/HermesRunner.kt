package io.github.victorhsr.hermes.core

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.victorhsr.hermes.core.gen.DSLGenerator
import io.github.victorhsr.hermes.core.reflection.AttributeInfoBuilder
import io.github.victorhsr.hermes.core.reflection.ClassInfoBuilder
import io.github.victorhsr.hermes.core.reflection.Scanner

fun main() {
    HermesRunner().genDSLForPackage("io.github.victorhsr")
}

class HermesRunner {

    private val scanner = Scanner()
    private val attributeInfoBuilder = AttributeInfoBuilder()
    private val classInfoBuilder = ClassInfoBuilder(attributeInfoBuilder)

    private val dslGenerator = DSLGenerator()

    fun genDSLForPackage(packageToScan: String) {
        val rootClasses = this.scanner.lookForDSLRootClasses(packageToScan)
        val classInfoList = this.classInfoBuilder.processRootClasses(rootClasses)

        this.dslGenerator.generate(classInfoList)
    }

}