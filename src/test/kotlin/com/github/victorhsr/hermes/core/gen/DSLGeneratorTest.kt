package com.github.victorhsr.hermes.core.gen

import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import com.github.victorhsr.hermes.core.AttributeInfo
import com.github.victorhsr.hermes.core.ClassInfo
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.jupiter.api.Test
import javax.annotation.processing.Filer
import javax.lang.model.element.Modifier

class DSLGeneratorTest {

    @Test
    fun `should generate the DSL by calling JavaFile#writeTo`() {
        //   given
        val classInfo: com.github.victorhsr.hermes.core.ClassInfo = mockk<com.github.victorhsr.hermes.core.ClassInfo>()
        val classInfoList: List<com.github.victorhsr.hermes.core.ClassInfo> = listOf(classInfo)
        val filer: Filer = mockk<Filer>()
        val attributeInfo: com.github.victorhsr.hermes.core.AttributeInfo = mockk<com.github.victorhsr.hermes.core.AttributeInfo>()
        val methodSpecGenerator: MethodSpecGenerator = mockk<MethodSpecGenerator>()
        val methodSpec: MethodSpec = mockk<MethodSpec>()
        val typeSpec: TypeSpec = mockk<TypeSpec>()
        val simpleName = "simple-name"
        val packageName = "package-name"
        val typeSpecBuilder: TypeSpec.Builder = mockk<TypeSpec.Builder>()
        val javaFileBuilder: JavaFile.Builder = mockk<JavaFile.Builder>()
        val javaFile: JavaFile = mockk<JavaFile>()

        every { methodSpecGenerator.buildMethod(attributeInfo) } returns methodSpec

        every { classInfo.attributes } returns listOf(attributeInfo)
        every { classInfo.simpleName } returns simpleName
        every { classInfo.packageName } returns packageName
        every { classInfo.isRoot } returns false

        every { typeSpecBuilder.addModifiers(Modifier.PUBLIC, Modifier.FINAL) } returns typeSpecBuilder
        every { typeSpecBuilder.addMethods(listOf(methodSpec)) } returns typeSpecBuilder
        every { typeSpecBuilder.build(); } returns typeSpec

        every { javaFileBuilder.build() } returns javaFile
        every { javaFile.writeTo(filer) } returns Unit

        mockkStatic(TypeSpec::class)
        every { TypeSpec.classBuilder("${simpleName}DSL") } returns typeSpecBuilder

        mockkStatic(JavaFile::class)
        every { JavaFile.builder(packageName, typeSpec) } returns javaFileBuilder

        val dslGenerator = DSLGenerator(methodSpecGenerator)

        // when
        dslGenerator.generate(classInfoList, filer)

        // then
        verify { javaFile.writeTo(filer) }
    }

    @Test
    fun `should generate the DSL for ROOT class by calling JavaFile#writeTo`() {
        // given
        val classInfo: com.github.victorhsr.hermes.core.ClassInfo = mockk<com.github.victorhsr.hermes.core.ClassInfo>()
        val classInfoList: List<com.github.victorhsr.hermes.core.ClassInfo> = listOf(classInfo)
        val filer: Filer = mockk<Filer>()
        val attributeInfo: com.github.victorhsr.hermes.core.AttributeInfo = mockk<com.github.victorhsr.hermes.core.AttributeInfo>()
        val methodSpecGenerator: MethodSpecGenerator = mockk<MethodSpecGenerator>()
        val methodSpec: MethodSpec = mockk<MethodSpec>()
        val rootMethodSpec: MethodSpec = mockk<MethodSpec>()
        val typeSpec: TypeSpec = mockk<TypeSpec>()
        val simpleName = "simple-name"
        val packageName = "package-name"
        val typeSpecBuilder: TypeSpec.Builder = mockk<TypeSpec.Builder>()
        val javaFileBuilder: JavaFile.Builder = mockk<JavaFile.Builder>()
        val javaFile: JavaFile = mockk<JavaFile>()

        every { methodSpecGenerator.buildMethod(attributeInfo) } returns methodSpec
        every { methodSpecGenerator.buildRootMethod(classInfo) } returns rootMethodSpec

        every { classInfo.attributes } returns listOf(attributeInfo)
        every { classInfo.simpleName } returns simpleName
        every { classInfo.packageName } returns packageName
        every { classInfo.isRoot } returns true

        every { typeSpecBuilder.addModifiers(Modifier.PUBLIC, Modifier.FINAL) } returns typeSpecBuilder
        every { typeSpecBuilder.addMethods(listOf(methodSpec, rootMethodSpec)) } returns typeSpecBuilder
        every { typeSpecBuilder.build(); } returns typeSpec

        every { javaFileBuilder.build() } returns javaFile
        every { javaFile.writeTo(filer) } returns Unit

        mockkStatic(TypeSpec::class)
        every { TypeSpec.classBuilder("${simpleName}DSL") } returns typeSpecBuilder

        mockkStatic(JavaFile::class)
        every { JavaFile.builder(packageName, typeSpec) } returns javaFileBuilder

        val dslGenerator = DSLGenerator(methodSpecGenerator)

        // when
        dslGenerator.generate(classInfoList, filer)

        // then
        verify { javaFile.writeTo(filer) }
    }

}