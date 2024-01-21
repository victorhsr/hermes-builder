package io.github.victorhsr.hermes.core.gen

import com.squareup.javapoet.*
import io.github.victorhsr.hermes.core.AttributeInfo
import io.github.victorhsr.hermes.core.ClassInfo
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import javax.lang.model.element.Modifier

class MethodSpecGeneratorTest {

    @Test
    fun `build root method`() {
        // given
        val builder: MethodSpec.Builder = mockk<MethodSpec.Builder>()
        val parameterName = "parameter-name"
        val fullQualifiedName = "full-qualified-name"
        val classInfo = mockk<ClassInfo>()
        val typeName: TypeName = mockk<TypeName>()
        val arrayTypeName: ArrayTypeName = mockk<ArrayTypeName>()
        val codeBlockGenerator: CodeBlockGenerator = mockk<CodeBlockGenerator>()
        val codeBlock: CodeBlock = mockk<CodeBlock>()
        val expectedResult: MethodSpec = mockk<MethodSpec>()

        every { codeBlockGenerator.buildCodeBlock(classInfo) } returns codeBlock
        every { classInfo.parameterName } returns parameterName
        every { classInfo.fullQualifiedName } returns fullQualifiedName
        every { builder.addModifiers(Modifier.PUBLIC, Modifier.STATIC) } returns builder
        every { builder.returns(typeName) } returns builder
        every { builder.addParameter(arrayTypeName, "options") } returns builder
        every { builder.varargs(true) } returns builder
        every { builder.addCode(codeBlock) } returns builder
        every { builder.addAnnotation(SafeVarargs::class.java) } returns builder
        every { builder.build() } returns expectedResult

        mockkObject(TypeUtil)
        every { TypeUtil.buildClassType(fullQualifiedName) } returns typeName
        every { TypeUtil.buildConsumerArrayType(fullQualifiedName) } returns arrayTypeName

        mockkStatic(MethodSpec::class)
        every { MethodSpec.methodBuilder(parameterName) } returns builder

        val methodSpecGenerator = MethodSpecGenerator(codeBlockGenerator)

        // when
        val actual: MethodSpec = methodSpecGenerator.buildRootMethod(classInfo)

        // then
        assertThat(actual).isEqualTo(expectedResult)
    }

    @Test
    fun `build method without options`() {
        // given
        val attributeInfo: AttributeInfo = mockk<AttributeInfo>()
        val attributeName = "attribute-name"
        val type = "type"
        val buildMethodName = "build-method-name"
        val wrapperClassName = "wrapper-class-name"
        val builder: MethodSpec.Builder = mockk<MethodSpec.Builder>()
        val expectedResult: MethodSpec = mockk<MethodSpec>()
        val typeName: TypeName = mockk<TypeName>()
        val parameterizedTypeName: ParameterizedTypeName = mockk<ParameterizedTypeName>()
        val codeBlockGenerator: CodeBlockGenerator = mockk<CodeBlockGenerator>()
        val codeBlock: CodeBlock = mockk<CodeBlock>()

        every { codeBlockGenerator.buildCodeBlock(attributeInfo) } returns codeBlock
        every { attributeInfo.wrapperClass } returns wrapperClassName
        every { attributeInfo.buildMethodName } returns buildMethodName
        every { attributeInfo.hasOptions } returns false
        every { attributeInfo.name } returns attributeName
        every { attributeInfo.type } returns type
        every { builder.addModifiers(Modifier.PUBLIC, Modifier.STATIC) } returns builder
        every { builder.addParameter(typeName, attributeName) } returns builder
        every { builder.addCode(codeBlock) } returns builder
        every { builder.returns(parameterizedTypeName) } returns builder
        every { builder.build() } returns expectedResult

        mockkStatic(MethodSpec::class)
        every { MethodSpec.methodBuilder(buildMethodName) } returns builder

        mockkObject(TypeUtil)
        every { TypeUtil.buildClassType(type) } returns typeName
        every { TypeUtil.buildConsumerType(wrapperClassName) } returns parameterizedTypeName

        val methodSpecGenerator = MethodSpecGenerator(codeBlockGenerator)

        // when
        val actual: MethodSpec = methodSpecGenerator.buildMethod(attributeInfo)

        // then
        assertThat(actual).isEqualTo(expectedResult)
    }

    @Test
    fun `build method with options`() {
        // given
        val attributeInfo: AttributeInfo = mockk<AttributeInfo>()
        val attributeName = "attribute-name"
        val type = "type"
        val buildMethodName = "build-method-name"
        val wrapperClassName = "wrapper-class-name"
        val builder: MethodSpec.Builder = mockk<MethodSpec.Builder>()
        val expectedResult: MethodSpec = mockk<MethodSpec>()
        val parameterizedTypeName: ParameterizedTypeName = mockk<ParameterizedTypeName>()
        val codeBlockGenerator: CodeBlockGenerator = mockk<CodeBlockGenerator>()
        val codeBlock: CodeBlock = mockk<CodeBlock>()
        val arrayTypeName: ArrayTypeName = mockk<ArrayTypeName>()

        every { codeBlockGenerator.buildCodeBlock(attributeInfo) } returns codeBlock
        every { attributeInfo.wrapperClass } returns wrapperClassName
        every { attributeInfo.buildMethodName } returns buildMethodName
        every { attributeInfo.hasOptions } returns true
        every { attributeInfo.name } returns attributeName
        every { attributeInfo.type } returns type
        every { builder.addModifiers(Modifier.PUBLIC, Modifier.STATIC) } returns builder
        every { builder.addParameter(arrayTypeName, "options") } returns builder
        every { builder.varargs() } returns builder
        every { builder.addCode(codeBlock) } returns builder
        every { builder.returns(parameterizedTypeName) } returns builder
        every { builder.addAnnotation(SafeVarargs::class.java) } returns builder
        every { builder.build() } returns expectedResult

        mockkStatic(MethodSpec::class)
        every { MethodSpec.methodBuilder(buildMethodName) } returns builder

        mockkObject(TypeUtil)
        every { TypeUtil.buildConsumerArrayType(type) } returns arrayTypeName
        every { TypeUtil.buildConsumerType(wrapperClassName) } returns parameterizedTypeName

        val methodSpecGenerator = MethodSpecGenerator(codeBlockGenerator)

        // when
        val actual: MethodSpec = methodSpecGenerator.buildMethod(attributeInfo)

        // then
        assertThat(actual).isEqualTo(expectedResult)
    }
}