package io.github.victorhsr.hermes.core

import io.github.victorhsr.hermes.core.gen.CodeBlockGenerator
import io.github.victorhsr.hermes.core.gen.DSLGenerator
import io.github.victorhsr.hermes.core.gen.MethodSpecGenerator

class DSLGeneratorFactory {

    companion object {
        fun create(): DSLGenerator {
            val codeBlockGenerator = CodeBlockGenerator()
            val methodSpecGenerator = MethodSpecGenerator(codeBlockGenerator)
            return DSLGenerator(methodSpecGenerator)
        }
    }

}