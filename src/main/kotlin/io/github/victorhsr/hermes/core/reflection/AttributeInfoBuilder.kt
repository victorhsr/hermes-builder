package io.github.victorhsr.hermes.core.reflection

import io.github.victorhsr.hermes.core.AttributeInfo
import io.github.victorhsr.hermes.core.annotations.DSLProperty
import java.lang.reflect.Field
import javax.lang.model.element.Element

class AttributeInfoBuilder {

    fun buildAttributeInfo(attribute: Element): AttributeInfo {
        val attributeClass = attribute.asType().toString()
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