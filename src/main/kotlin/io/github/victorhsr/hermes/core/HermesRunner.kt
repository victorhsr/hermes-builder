package io.github.victorhsr.hermes.core

import io.github.victorhsr.hermes.core.gen.DSLGenerator
import io.github.victorhsr.hermes.core.reflection.lookForDSLRootClasses
import io.github.victorhsr.hermes.gen.Person

fun main() {
    println(Person::class.java.simpleName)
    //HermesRunner().genDSLForPackage("io.github.victorhsr")
}

class HermesRunner {

    private val dslGenerator = DSLGenerator()

    fun genDSLForPackage(packageToScan: String) {
        val classes = lookForDSLRootClasses(packageToScan)
        this.dslGenerator.generate(classes)
    }

}