package io.github.victorhsr.hermes.core.gen

import com.squareup.javapoet.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.function.Consumer

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
            val packageName = "java.lang"
            val className = "String"
            val fullQualifiedClassName = "$packageName.$className"

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
            val fullQualifiedTypeName = "java.util.Map<java.lang.String,java.util.List<java.lang.Integer>>"
            val stringClassName = mockk<ClassName>()
            val integerClassName = mockk<ClassName>()
            val listClassName = mockk<ClassName>()
            val mapClassName = mockk<ClassName>()
            val listParameterizedTypeName = mockk<ParameterizedTypeName>()
            val mapParameterizedTypeName = mockk<ParameterizedTypeName>()

            mockkStatic(ClassName::class)
            every { ClassName.bestGuess("java.lang.String") } returns stringClassName
            every { ClassName.bestGuess("java.lang.Integer") } returns integerClassName
            every { ClassName.bestGuess("java.util.List") } returns listClassName
            every { ClassName.bestGuess("java.util.Map") } returns mapClassName

            mockkStatic(ParameterizedTypeName::class)
            every { ParameterizedTypeName.get(listClassName, integerClassName) } returns listParameterizedTypeName
            every {
                ParameterizedTypeName.get(
                    mapClassName,
                    stringClassName,
                    listParameterizedTypeName
                )
            } returns mapParameterizedTypeName

            // when
            val actual: TypeName = TypeUtil.buildClassType(fullQualifiedTypeName)

            // then
            assertThat(actual).isEqualTo(mapParameterizedTypeName)
        }

    }

    @Nested
    inner class BuildConsumerType {

        @Test
        fun `build consumer type`() {
            // given
            val expectedResult = mockk<ParameterizedTypeName>()
            val packageName = "io.my-packege"
            val clazz = "Foo"
            val fullQualifiedClassName = "$packageName.$clazz"

            val className = mockk<ClassName>()
            val consumerClassName = mockk<ClassName>()

            mockkStatic(ClassName::class)
            every { ClassName.get(packageName, clazz) } returns className
            every { ClassName.get(Consumer::class.java) } returns consumerClassName

            mockkStatic(ParameterizedTypeName::class)
            every { ParameterizedTypeName.get(consumerClassName, className) } returns expectedResult

            // when
            val actual: TypeName = TypeUtil.buildConsumerType(fullQualifiedClassName)

            // then
            assertThat(actual).isEqualTo(expectedResult)
        }

        @Test
        fun `build consumer array type`() {
            // given
            val expectedResult = mockk<ArrayTypeName>()
            val packageName = "io.my-packege"
            val clazz = "Foo"
            val fullQualifiedClassName = "$packageName.$clazz"

            val className = mockk<ClassName>()
            val consumerClassName = mockk<ClassName>()
            val consumerParameterizedTypeName = mockk<ParameterizedTypeName>()

            mockkStatic(ClassName::class)
            every { ClassName.get(packageName, clazz) } returns className
            every { ClassName.get(Consumer::class.java) } returns consumerClassName

            mockkStatic(ParameterizedTypeName::class)
            every { ParameterizedTypeName.get(consumerClassName, className) } returns consumerParameterizedTypeName

            mockkStatic(ArrayTypeName::class)
            every { ArrayTypeName.of(consumerParameterizedTypeName) } returns expectedResult

            // when
            val actual: TypeName = TypeUtil.buildConsumerArrayType(fullQualifiedClassName)

            // then
            assertThat(actual).isEqualTo(expectedResult)
        }
    }

}
