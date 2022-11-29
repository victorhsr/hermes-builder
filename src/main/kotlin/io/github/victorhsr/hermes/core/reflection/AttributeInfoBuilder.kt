package io.github.victorhsr.hermes.core.reflection

import io.github.victorhsr.hermes.core.AttributeInfo
import io.github.victorhsr.hermes.core.annotations.DSLProperty
import java.lang.reflect.Field
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.type.DeclaredType
import javax.lang.model.util.ElementFilter


class AttributeInfoBuilder {

    fun buildAttributeInfo(attribute: Element, processingEnvironment: ProcessingEnvironment): AttributeInfo {
        println("aatribute $attribute")
        val constructorsIn = ElementFilter.constructorsIn(setOf(attribute))

        val attributeTypeString = attribute.asType().toString()
        val typeElement = processingEnvironment.elementUtils.getTypeElement(attributeTypeString)
        val enclosedElements = typeElement.enclosedElements
        println("enclosedElements = ${enclosedElements}")
        println("typeElement = ${typeElement}")

//        i decided to not worry about having or not a default constructor neither if
//            it is about a native class, if the user dont want it to be generated
//                he must annotate the attribute with @DSLIgnore
        val attributeClass = attributeTypeString
//        val hasDefaultConstructor = this.hasDefaultConstructor(attributeClass)
//        val isNativeClass = this.isNativeClass(attributeClass)

        return AttributeInfo(
            name = "field.name",
            buildMethodName = "this.resolveBuildMethodName(field)",
            setterMethodName = "set",
            type = String.javaClass,
            wrapperClass = Int.javaClass,
            hasOptions = false,
            hasDefaultConstructor = false,
            isNativeClass = false
        )

//        return AttributeInfo(
//            name = field.name,
//            buildMethodName = this.resolveBuildMethodName(field),
//            setterMethodName = "set${field.name.replaceFirstChar { it.titlecase() }}",
//            type = attributeClass,
//            wrapperClass = wrapperClass,
//            hasOptions = hasDefaultConstructor && !isNativeClass,
//            hasDefaultConstructor = hasDefaultConstructor,
//            isNativeClass = isNativeClass
//        )
    }

    fun buildAttributeInfo(wrapperClass: Class<*>, field: Field): AttributeInfo {
        val attributeClass = field.type
        val hasDefaultConstructor = this.hasDefaultConstructor(attributeClass)
        val isNativeClass = this.isNativeClass(attributeClass)

        return AttributeInfo(
            name = field.name,
            buildMethodName = this.resolveBuildMethodName(field),
            setterMethodName = "set${field.name.replaceFirstChar { it.titlecase() }}",
            type = attributeClass,
            wrapperClass = wrapperClass,
            hasOptions = hasDefaultConstructor && !isNativeClass,
            hasDefaultConstructor = hasDefaultConstructor,
            isNativeClass = isNativeClass
        )
    }

    private fun resolveBuildMethodName(field: Field): String {
        val annotations = field.annotations
        val declaredAnnotations = field.declaredAnnotations

        println("annotations = ${annotations}")
        println("declaredAnnotations = ${declaredAnnotations}")

        val annotation = field.getAnnotation(DSLProperty::class.java) ?: return field.name
        return annotation.value
    }

    private fun isNativeClass(attributeClass: Class<*>): Boolean {
        val packageName = attributeClass.packageName

        return (packageName.startsWith("java.")
                ||
                packageName.startsWith("sun.")
                ||
                packageName.startsWith("javafx."))
    }

    private fun hasDefaultConstructor(clazz: Class<*>) = clazz.declaredConstructors.any { it.parameterCount == 0 }

}