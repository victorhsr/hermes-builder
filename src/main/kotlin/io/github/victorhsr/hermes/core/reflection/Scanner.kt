package io.github.victorhsr.hermes.core.reflection

import io.github.victorhsr.hermes.core.annotations.DSLRoot
import org.reflections.Reflections
import org.reflections.scanners.Scanners

class Scanner {

    fun lookForDSLRootClasses(packageToScan: String): Set<Class<*>> {
        return Reflections(packageToScan, Scanners.TypesAnnotated)
            .getTypesAnnotatedWith(DSLRoot::class.java)
    }
}




