package io.github.victorhsr.hermes.annotations

import com.example.PersonDSL.PersonDSL.person
import com.squareup.javapoet.*
import lombok.Data
import java.io.File
import java.util.function.Consumer
import java.util.stream.Stream
import javax.lang.model.element.Modifier

fun main(vararg x: String) {

    val codeBlock = CodeBlock
        .builder()
        .addStatement("final Person person = new Person()")
        .addStatement("\$T.of(options).forEach(option -> option.accept(person))", Stream::class.java)
        .addStatement("return person")
        .build()

    val consumerType = ParameterizedTypeName
        .get(ClassName.get(Consumer::class.java), TypeName.get(Person::class.java));
    val consumerArrayType = ArrayTypeName.of(consumerType);

    val methodSpec = MethodSpec.methodBuilder("person")
        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
        .returns(Person::class.java)
        .addParameter(consumerArrayType, "options")
        .varargs(true)
        .addCode(codeBlock)
        .build()

    val dsl = TypeSpec.classBuilder("PersonDSL")
        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
        .addMethod(methodSpec)
        .build();

    val javaFile = JavaFile.builder("com.example.PersonDSL", dsl)
        .build();

    val file = File("dsl");
    javaFile.writeTo(file);
}

data class Person(
    val name: String? = null,
    val address: Address? = null
)

data class Address(
    val street: String? = null,
    val number: Int? = null
)