package io.github.victorhsr.hermes.maven.element.builder

import io.github.victorhsr.hermes.core.annotations.DSLIgnore
import io.github.victorhsr.hermes.core.ext.myCapitalize
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

object FieldFinder {

    private const val SET_METHOD_PREFIX = "set"

    fun getFieldsFromClazz(clazz: TypeElement): List<Element> {
        return resolveFields(clazz)
            .filter { it.getAnnotation(DSLIgnore::class.java) == null }
    }

    private fun resolveFields(clazz: TypeElement): List<Element> {
        val setMethodsMap = getSetMethods(clazz)

        return clazz.enclosedElements
            .filter { it.kind.isField }
            .filter { setMethodsMap.containsKey(buildSetMethodName(it.simpleName.toString())) }
            .toList()
    }

    private fun getSetMethods(clazz: TypeElement): Map<String, List<Element>> {
        return clazz.enclosedElements
            .filter { it.kind == ElementKind.METHOD }
            .filter { it.simpleName.startsWith(SET_METHOD_PREFIX) }
            .groupBy { it.simpleName.toString() }
    }

    private fun buildSetMethodName(fieldName: String): String {
        return "${SET_METHOD_PREFIX}${fieldName.myCapitalize()}"
    }

}