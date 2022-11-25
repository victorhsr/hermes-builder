package io.github.victorhsr.hermes.maven

import io.github.victorhsr.hermes.core.HermesRunnerFactory
import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugins.annotations.LifecyclePhase
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import java.io.File

@Mojo(name = "hermes-dsl", defaultPhase = LifecyclePhase.COMPILE)
class HermesMojo : AbstractMojo() {

    @Parameter(property = "package", readonly = true, required = true)
    private lateinit var packageToScan: String

    @Parameter(property = "project.compileClasspathElements", required = true, readonly = true)
    private lateinit var classpath: List<String>

    @Parameter(property = "output", defaultValue = "\${project.build.directory}/generated-sources/hermes-dsl")
    private lateinit var output: File

    private val hermesRunner = HermesRunnerFactory.create()

    override fun execute() {
        this.hermesRunner.genDSLForPackage(this.classpath, this.packageToScan, this.output)
    }
}