package io.github.victorhsr.hermes.core.reflection

import io.github.victorhsr.hermes.core.ClassInfo
import java.util.*
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

class ClassInfoBuilder(private val attributeInfoBuilder: AttributeInfoBuilder) {

    private val classMap = mutableMapOf<String, ClassInfo>()

    fun processRootClasses(rootClasses: List<TypeElement>): List<ClassInfo> {
        rootClasses.forEach(this::processRootClass)
        return classMap.values.toList()
    }

    private fun processRootClass(clazz: TypeElement) {
        this.buildClassInfo(clazz, true)
    }

    private fun buildClassInfo(clazz: TypeElement, isRootClass: Boolean) {

        val fullQualifiedClassName = clazz.qualifiedName.toString()

        if (this.classMap.containsKey(fullQualifiedClassName)) {
            return
        }

        val simpleName = clazz.simpleName.toString()

        val classInfo = ClassInfo(
            fullQualifiedName = fullQualifiedClassName,
            packageName = fullQualifiedClassName.substring(0, fullQualifiedClassName.lastIndexOf(".")),
            name = fullQualifiedClassName,
            parameterName = simpleName.replaceFirstChar { it.lowercase(Locale.getDefault()) },
            isRoot = isRootClass,
            attributes = this.resolveFields(clazz).map { this.attributeInfoBuilder.buildAttributeInfo(it) }
        )

        this.classMap[fullQualifiedClassName] = classInfo

        classInfo.attributes.forEach {
            if (it.hasOptions) {
                this.buildClassInfo(it.type, false)
            }
        }
    }

    private fun resolveFields(clazz: TypeElement): List<Element> {

        val getMethodsMap = clazz.enclosedElements
            .filter { it.kind == ElementKind.METHOD }
            .filter { it.simpleName.startsWith("get") }
            .groupBy { it.simpleName.toString() }

        return clazz.enclosedElements
            .filter { it.kind.isField }
            .filter { getMethodsMap.containsKey(this.buildGetMethodName(it.simpleName.toString())) }
            .toList()
    }

    private fun buildGetMethodName(name: String): String {
        return "get${name.replaceFirstChar { it.titlecase(Locale.getDefault()) }}"
    }

}