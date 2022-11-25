package io.github.victorhsr.hermes.core

import io.github.victorhsr.hermes.core.gen.CodeBlockGenerator
import io.github.victorhsr.hermes.core.gen.DSLGenerator
import io.github.victorhsr.hermes.core.gen.MethodSpecGenerator
import io.github.victorhsr.hermes.core.reflection.AttributeInfoBuilder
import io.github.victorhsr.hermes.core.reflection.ClassInfoBuilder
import io.github.victorhsr.hermes.core.reflection.Scanner

class HermesRunnerFactory {

    companion object {
        fun create(): HermesRunner {
            val scanner = Scanner()
            val attributeInfoBuilder = AttributeInfoBuilder()
            val classInfoBuilder = ClassInfoBuilder(attributeInfoBuilder)

            val codeBlockGenerator = CodeBlockGenerator()
            val methodSpecGenerator = MethodSpecGenerator(codeBlockGenerator)
            val dslGenerator = DSLGenerator(methodSpecGenerator)

            return HermesRunner(scanner, classInfoBuilder, dslGenerator)
        }
    }

}