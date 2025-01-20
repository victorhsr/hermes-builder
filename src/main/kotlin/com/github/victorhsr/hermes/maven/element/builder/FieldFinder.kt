package com.github.victorhsr.hermes.maven.element.builder

import com.github.victorhsr.hermes.core.annotations.DSLIgnore
import com.github.victorhsr.hermes.core.ext.myCapitalize
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

private const val SET_METHOD_PREFIX = "set"
private const val LOMBOK_SETTER_ANNOTATION = "lombok.Setter"
private const val LOMBOK_DATA_ANNOTATION = "lombok.Data"
private val LOMBOK_CLASS_ANNOTATIONS = setOf(LOMBOK_SETTER_ANNOTATION, LOMBOK_DATA_ANNOTATION)

object FieldFinder {

    fun getFieldsFromClazz(clazz: TypeElement): List<Element> {
        return resolveFields(clazz)
            .filter { it.getAnnotation(DSLIgnore::class.java) == null }
    }

    private fun resolveFields(clazz: TypeElement): List<Element> {
        val setMethodsMap = getFieldsWithSetterMethod(clazz)

        return clazz.enclosedElements
            .filter { it.kind.isField }
            .filter { setMethodsMap.contains(buildSetMethodName(it.simpleName.toString())) }
            .toList()
    }

    private fun getFieldsWithSetterMethod(clazz: TypeElement): Set<String> {
        val setMethodsFromFieldsAnnotatedWithLombok = getAnnotatedFieldsWithLombok(clazz)
        val setMethodsFromClassOrRegularMethods =
            if (isLombokManagedClass(clazz))
                return clazz.enclosedElements
                    .filter { it.kind == ElementKind.FIELD }
                    .map { buildSetMethodName(it.simpleName.toString()) }
                    .toSet()
            else clazz.enclosedElements
                .filter { it.kind == ElementKind.METHOD }
                .filter { it.simpleName.startsWith(SET_METHOD_PREFIX) }
                .map { it.simpleName.toString() }
                .toSet()

        return setMethodsFromFieldsAnnotatedWithLombok + setMethodsFromClassOrRegularMethods
    }

    private fun getAnnotatedFieldsWithLombok(clazz: TypeElement): Set<String> {
        return clazz.enclosedElements
            .filter { it.kind == ElementKind.FIELD }
            .filter { field -> field.annotationMirrors.any { it.annotationType.toString() == LOMBOK_SETTER_ANNOTATION } }
            .map { buildSetMethodName(it.simpleName.toString()) }
            .toSet()
    }

    private fun isLombokManagedClass(clazz: TypeElement): Boolean {
        return clazz.annotationMirrors.any { LOMBOK_CLASS_ANNOTATIONS.contains(it.annotationType.toString()) }
    }

    private fun buildSetMethodName(fieldName: String): String {
        return "$SET_METHOD_PREFIX${fieldName.myCapitalize()}"
    }

}