package io.github.victorhsr.hermes.core.reflection

import io.github.victorhsr.hermes.core.annotations.DSLRoot
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import java.io.File
import java.net.URLClassLoader

class Scanner {

    fun lookForDSLRootClasses(classpath: List<String>, packageToScan: String): Set<Class<*>> {
        this.updateClassLoader(classpath)
        return Reflections(packageToScan, Scanners.TypesAnnotated).getTypesAnnotatedWith(DSLRoot::class.java)
    }

    private fun updateClassLoader(classpath: List<String>) {
        val urls = classpath.map { File(it).toURI().toURL() }.toTypedArray()
        val urlClassLoader = URLClassLoader.newInstance(urls, Thread.currentThread().contextClassLoader)
        Thread.currentThread().contextClassLoader = urlClassLoader;
    }
}

