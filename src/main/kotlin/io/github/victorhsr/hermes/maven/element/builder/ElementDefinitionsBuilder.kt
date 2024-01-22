package io.github.victorhsr.hermes.maven.element.builder

import io.github.victorhsr.hermes.maven.element.ClassElementDefinition
import io.github.victorhsr.hermes.maven.element.FieldElementDefinition
import javax.lang.model.element.TypeElement

class ElementDefinitionsBuilder {

    companion object {
        private const val LESS_THAN_SYMBOL = "<"
    }

    private val classElementDefinitionMap = mutableMapOf<String, ClassElementDefinition>()
    private val annotatedClassesToGenericTypesMap = mutableMapOf<String, List<String>>();

    fun resolveElementDefinitions(annotatedClasses: List<TypeElement>): List<ClassElementDefinition> {
        annotatedClasses.forEach {
            annotatedClassesToGenericTypesMap[removeGenericFromTypeName(it.toString())] = getGenericTypes(it)
        }
        annotatedClasses.forEach(::buildClassElementDefinitions)

        return classElementDefinitionMap.values.toList()
    }

    private fun getGenericTypes(typeElement: TypeElement): List<String> {
        return typeElement.typeParameters.map { it.toString() }
    }

    private fun buildClassElementDefinitions(typeElement: TypeElement) {
        val fullQualifiedClassName = removeGenericFromTypeName(typeElement.toString())
        val classElementDefinition =
            ClassElementDefinition(typeElement, fullQualifiedClassName, buildAccessibleFields(typeElement), true)
        classElementDefinitionMap[fullQualifiedClassName] = classElementDefinition
    }

    private fun buildAccessibleFields(clazz: TypeElement): List<FieldElementDefinition> {
        return FieldFinder
            .getFieldsFromClazz(clazz)
            .map {
                val typeToString = removeGenericFromTypeName(it.asType().toString())

                val isPrimitive: Boolean = it.asType().kind.isPrimitive
                val isGenericType: Boolean = annotatedClassesToGenericTypesMap[clazz.toString()]!!
                    .contains(typeToString)
                val shouldClassBeGenerated: Boolean = annotatedClassesToGenericTypesMap.contains(typeToString)

                FieldElementDefinition(it, typeToString, shouldClassBeGenerated, isPrimitive, isGenericType)
            }
    }

    private fun removeGenericFromTypeName(typeName: String): String {
        return typeName.substringBefore(LESS_THAN_SYMBOL)
    }
}