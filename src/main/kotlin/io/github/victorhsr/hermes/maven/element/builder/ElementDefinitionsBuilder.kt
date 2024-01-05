package io.github.victorhsr.hermes.maven.element.builder

import io.github.victorhsr.hermes.core.annotations.DSLProperty
import io.github.victorhsr.hermes.maven.element.ClassElementDefinition
import io.github.victorhsr.hermes.maven.element.FieldElementDefinition
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType

class ElementDefinitionsBuilder {

    private val classElementDefinitionMap = mutableMapOf<String, ClassElementDefinition>()
    private val annotatedClassesMap = mutableListOf<String>();

    fun resolveElementDefinitions(annotatedClasses: List<TypeElement>): List<ClassElementDefinition> {
        annotatedClasses.forEach { annotatedClassesMap.add(it.asType().toString()) }
        annotatedClasses.forEach(this::processAnnotatedClass)
        return this.classElementDefinitionMap.values.toList()
    }

    private fun processAnnotatedClass(typeElement: TypeElement) {
        this.buildClassElementDefinitions(typeElement)
    }

    private fun buildClassElementDefinitions(typeElement: TypeElement) {
        val fullQualifiedClassName = typeElement.asType().toString()
        val classElementDefinition = buildClassElementDefinition(typeElement, true);
        this.classElementDefinitionMap[fullQualifiedClassName] = classElementDefinition

        classElementDefinition.accessibleFields.forEach {
            if (it.shouldClassBeGenerated) {
                this.buildClassElementDefinitionsForNestedFields(
                    it.declaredType!!.asElement() as TypeElement,
                    false
                )
            }
        }
    }

    private fun buildClassElementDefinitionsForNestedFields(typeElement: TypeElement, isAnnotatedClass: Boolean) {
        val fullQualifiedClassName = typeElement.asType().toString()

        if (!isAnnotatedClass && this.classElementDefinitionMap.containsKey(fullQualifiedClassName)) {
            return
        }

        val classElementDefinition = buildClassElementDefinition(typeElement, isAnnotatedClass);
        this.classElementDefinitionMap[fullQualifiedClassName] = classElementDefinition
    }

    private fun buildClassElementDefinition(typeElement: TypeElement, isAnnotatedClass: Boolean): ClassElementDefinition {
        return ClassElementDefinition(
            element = typeElement,
            wasAnnotated = isAnnotatedClass,
            accessibleFields = this.buildAccessibleFields(typeElement)
        )
    }

    private fun buildAccessibleFields(clazz: TypeElement): List<FieldElementDefinition> {
        return FieldFinder.getFieldsFromClazz(clazz)
            .map { this.buildFieldElementDefinitions(it) }
    }

    private fun buildFieldElementDefinitions(fieldElement: Element): FieldElementDefinition {
        val isPrimitiveType = fieldElement.asType().kind.isPrimitive
        val declaredType =
            if (!isPrimitiveType) {
                fieldElement.asType() as DeclaredType
            } else null

        val dslPropertyAnnotation = fieldElement.getAnnotation(DSLProperty::class.java)
        val customBuildName = dslPropertyAnnotation?.value

        val fullQualifiedName = fieldElement.asType().toString()

        return FieldElementDefinition(
            fieldName = fieldElement.simpleName.toString(),
            declaredType = declaredType,
            primitiveElement = fieldElement,
            isPrimitiveType = isPrimitiveType,
            shouldClassBeGenerated = annotatedClassesMap.contains(fullQualifiedName),
            customBuildName = customBuildName
        )
    }

}