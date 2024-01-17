package io.github.victorhsr.hermes.core.gen

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeVariableName
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class TypeUtilTest {

    @Nested
    inner class BuildClassType {

        @Test
        fun `build type for primitive data type`() {
            // given
            val fullQualifiedClassName = "int"
            val expectedResult = mockk<TypeVariableName>()

            mockkStatic(TypeVariableName::class)
            every { TypeVariableName.get(fullQualifiedClassName) } returns expectedResult

            // when
            val actual: TypeName = TypeUtil.buildClassType(fullQualifiedClassName)

            // then
            assertThat(actual).isEqualTo(expectedResult)
        }

        @Test
        fun `build type for non-primitive data type`() {
            // given
            val fullQualifiedClassName = "java.lang.String"
            val packageName = "java.lang"
            val className = "String"

            val expectedResult = mockk<ClassName>()

            mockkStatic(ClassName::class)
            every { ClassName.get(packageName, className) } returns expectedResult

            // when
            val actual: TypeName = TypeUtil.buildClassType(fullQualifiedClassName)

            // then
            assertThat(actual).isEqualTo(expectedResult)
        }

        @Test
        fun `build type for non-primitive data type with generics`() {
            // given
            val fullQualifiedClassName = "java.util.Map<java.lang.String,java.util.List<java.lang.String>>"

            val expectedResult = mockk<ClassName>()

//            mockkStatic(ClassName::class)
//            mockkStatic(ParameterizedTypeName::class)

            // when
            val actual: TypeName = TypeUtil.buildClassType(fullQualifiedClassName)

            // then
            assertThat(actual).isEqualTo(expectedResult)
        }

    }

}

//
//ClassName.bestGuess(typeAsString)
//java.lang.String
//
//ClassName.bestGuess(typeAsString)
//java.lang.String
//
//ClassName.bestGuess(className)
//java.util.List
//
//ParameterizedTypeName.get(classNameObj, *typeNameArgs)
//classNameObj = {ClassName@4603} "java.util.List"
//packageName = "java.util"
//enclosingClassName = null
//simpleName = "List"
//simpleNames = null
//canonicalName = "java.util.List"
//keyword = null
//annotations = {Collections$UnmodifiableRandomAccessList@4622}  size = 0
//cachedString = "java.util.List"
//typeNameArgs = {TypeName[1]@4604}
//0 = {ClassName@4613} "java.lang.String"
//packageName = "java.lang"
//enclosingClassName = null
//simpleName = "String"
//simpleNames = null
//canonicalName = "java.lang.String"
//keyword = null
//annotations = {Collections$UnmodifiableRandomAccessList@4618}  size = 0
//cachedString = "java.lang.String"
//
//ClassName.bestGuess(className)
//java.util.Map
//
//ParameterizedTypeName.get(classNameObj, *typeNameArgs)
//classNameObj = {ClassName@4630} "java.util.Map"
//packageName = "java.util"
//enclosingClassName = null
//simpleName = "Map"
//simpleNames = null
//canonicalName = "java.util.Map"
//keyword = null
//annotations = {Collections$UnmodifiableRandomAccessList@4651}  size = 0
//cachedString = "java.util.Map"
//typeNameArgs = {TypeName[2]@4631}
//0 = {ClassName@4634} "java.lang.String"
//packageName = "java.lang"
//enclosingClassName = null
//simpleName = "String"
//simpleNames = null
//canonicalName = "java.lang.String"
//keyword = null
//annotations = {Collections$UnmodifiableRandomAccessList@4646}  size = 0
//cachedString = "java.lang.String"
//1 = {ParameterizedTypeName@4635} "java.util.List<java.lang.String>"
//enclosingType = null
//rawType = {ClassName@4638} "java.util.List"
//typeArguments = {Collections$UnmodifiableRandomAccessList@4639}  size = 1
//keyword = null
//annotations = {Collections$UnmodifiableRandomAccessList@4640}  size = 0
//cachedString = "java.util.List<java.lang.String>"