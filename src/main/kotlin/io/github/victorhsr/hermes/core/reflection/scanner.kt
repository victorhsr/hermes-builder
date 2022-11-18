package io.github.victorhsr.hermes.core.reflection

import io.github.victorhsr.hermes.core.annotations.DSLRoot
import org.reflections.Reflections
import org.reflections.scanners.Scanners

fun lookForAnnotatedClasses(packageToScan: String, clazz: Class<*>): MutableSet<Class<*>> {
    return Reflections(packageToScan, Scanners.TypesAnnotated)
        .getTypesAnnotatedWith(DSLRoot::class.java)
}

fun lookForDSLRootClasses(packageToScan: String) = lookForAnnotatedClasses(packageToScan, DSLRoot::class.java)