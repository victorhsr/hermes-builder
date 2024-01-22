package io.github.victorhsr.hermes.core.gen

import com.squareup.javapoet.CodeBlock
import io.github.victorhsr.hermes.core.AttributeInfo
import io.github.victorhsr.hermes.core.ClassInfo
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.stream.Stream

class CodeBlockGeneratorTest {

    private val codeBlockGenerator = CodeBlockGenerator()

    @Test
    fun `build code block for ClassInfo`() {
        // given
        val classInfo: ClassInfo = mockk<ClassInfo>()
        val builder: CodeBlock.Builder = mockk<CodeBlock.Builder>()
        val simpleName = "simple-name"
        val parameterName = "parameter-name"
        val expectedResult: CodeBlock = mockk<CodeBlock>();

        every { classInfo.simpleName } returns simpleName
        every { classInfo.parameterName } returns parameterName
        every { builder.addStatement("final $simpleName $parameterName = new $simpleName()") } returns builder
        every {
            builder.addStatement(
                "\$T.of(options).forEach(option -> option.accept($parameterName))",
                Stream::class.java
            )
        } returns builder
        every { builder.addStatement("return $parameterName") } returns builder
        every { builder.build() } returns expectedResult

        mockkStatic(CodeBlock::class)
        every { CodeBlock.builder() } returns builder

        // when
        val actual: CodeBlock = codeBlockGenerator.buildCodeBlock(classInfo)

        // then
        assertThat(actual).isEqualTo(expectedResult)
    }

    @Test
    fun `build code block`() {
        // given
        val attributeInfo: AttributeInfo = mockk<AttributeInfo>()
        val wrapperClassParamName = "wrapper-class-param-name"
        val setterMethodName = "set-method-name"
        val attributeName = "attribute-name"
        val builder: CodeBlock.Builder = mockk<CodeBlock.Builder>()
        val expectedResult: CodeBlock = mockk<CodeBlock>()

        every { attributeInfo.hasOptions } returns false
        every { attributeInfo.wrapperClassParamName } returns wrapperClassParamName
        every { attributeInfo.setterMethodName } returns setterMethodName
        every { attributeInfo.name } returns attributeName

        mockkStatic(CodeBlock::class)
        every { CodeBlock.builder() } returns builder
        every { builder.addStatement("return ($wrapperClassParamName) -> $wrapperClassParamName.$setterMethodName($attributeName)") } returns builder
        every { builder.build() } returns expectedResult

        // when
        val actual: CodeBlock = codeBlockGenerator.buildCodeBlock(attributeInfo)

        // then
        assertThat(actual).isEqualTo(expectedResult)
    }

    @Test
    fun `build code block with options`() {
        // given
        val attributeInfo: AttributeInfo = mockk<AttributeInfo>()
        val wrapperClassParamName = "wrapper-class-param-name"
        val setterMethodName = "set-method-name"
        val attributeName = "attribute-name"
        val type = "type"
        val builder: CodeBlock.Builder = mockk<CodeBlock.Builder>()
        val expectedResult: CodeBlock = mockk<CodeBlock>()

        every { attributeInfo.hasOptions } returns true
        every { attributeInfo.wrapperClassParamName } returns wrapperClassParamName
        every { attributeInfo.setterMethodName } returns setterMethodName
        every { attributeInfo.name } returns attributeName
        every { attributeInfo.type } returns type

        mockkStatic(CodeBlock::class)
        every { CodeBlock.builder() } returns builder
        every { builder.addStatement("final $type $attributeName = new $type()") } returns builder
        every {
            builder.addStatement(
                "\$T.of(options).forEach(option -> option.accept($attributeName))",
                Stream::class.java
            )
        } returns builder
        every { builder.addStatement("return ($wrapperClassParamName) -> $wrapperClassParamName.$setterMethodName($attributeName)") } returns builder
        every { builder.build() } returns expectedResult

        // when
        val actual: CodeBlock = codeBlockGenerator.buildCodeBlock(attributeInfo)

        // then
        assertThat(actual).isEqualTo(expectedResult)
    }

}