package io.github.victorhsr.hermes.core.reflection

import io.github.victorhsr.hermes.core.ClassInfo
import java.util.*

class ClassInfoBuilder(private val attributeInfoBuilder: AttributeInfoBuilder) {

    private val classMap = mutableMapOf<Class<*>, ClassInfo>()

    fun processRootClasses(rootClasses: Set<Class<*>>): List<ClassInfo> {
        rootClasses.forEach(this::processRootClass)
        return classMap.values.toList()
    }

    private fun processRootClass(clazz: Class<*>) {
        this.buildClassInfo(clazz, true)
    }

    private fun buildClassInfo(clazz: Class<*>, isRootClass: Boolean) {
        if (this.classMap.containsKey(clazz)) {
            return
        }

        val classInfo = ClassInfo(
            type = clazz,
            name = clazz.simpleName,
            parameterName = clazz.simpleName.replaceFirstChar { it.lowercase(Locale.getDefault()) },
            isRoot = isRootClass,
            attributes = clazz.declaredFields.map { this.attributeInfoBuilder.buildAttributeInfo(clazz, it) }
        )

        this.classMap[clazz] = classInfo

        classInfo.attributes.forEach {
            if (it.hasOptions) {
                this.buildClassInfo(it.type, false)
            }
        }
    }


}