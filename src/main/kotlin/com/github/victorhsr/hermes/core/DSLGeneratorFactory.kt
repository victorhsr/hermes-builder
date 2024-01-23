package com.github.victorhsr.hermes.core

import com.github.victorhsr.hermes.core.gen.CodeBlockGenerator
import com.github.victorhsr.hermes.core.gen.DSLGenerator
import com.github.victorhsr.hermes.core.gen.MethodSpecGenerator

object DSLGeneratorFactory {

    fun create(): DSLGenerator {
        val codeBlockGenerator = CodeBlockGenerator()
        val methodSpecGenerator = MethodSpecGenerator(codeBlockGenerator)
        return DSLGenerator(methodSpecGenerator)
    }
}