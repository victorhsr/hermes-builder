package io.github.victorhsr.hermes.core

import io.github.victorhsr.hermes.core.gen.CodeBlockGenerator
import io.github.victorhsr.hermes.core.gen.DSLGenerator
import io.github.victorhsr.hermes.core.gen.MethodSpecGenerator

class HermesRunnerFactory {

    companion object {
        fun create(): HermesRunner {
            val codeBlockGenerator = CodeBlockGenerator()
            val methodSpecGenerator = MethodSpecGenerator(codeBlockGenerator)
            val dslGenerator = DSLGenerator(methodSpecGenerator)

            return HermesRunner(dslGenerator)
        }
    }

}