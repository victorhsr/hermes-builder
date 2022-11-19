package io.github.victorhsr.hermes.core.reflection

import io.github.victorhsr.hermes.core.AttributeInfo
import java.lang.reflect.Field

class AttributeInfoBuilder {

    fun buildAttributeInfo(wrapperClass: Class<*>, field: Field): AttributeInfo {
        val attributeClass = field.type
        val hasDefaultConstructor = this.hasDefaultConstructor(attributeClass)
        val isNativeClass = this.isNativeClass(attributeClass)

        return AttributeInfo(
            name = field.name,
            methodName = "set${field.name.replaceFirstChar { it.titlecase() }}",
            type = attributeClass,
            wrapperClass = wrapperClass,
            hasOptions = hasDefaultConstructor && !isNativeClass,
            hasDefaultConstructor = hasDefaultConstructor,
            isNativeClass = isNativeClass
        )
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