package io.github.victorhsr.hermes.maven

import io.github.victorhsr.hermes.core.HermesRunner
import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugins.annotations.LifecyclePhase
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter

@Mojo(name = "hermes-dsl", defaultPhase = LifecyclePhase.COMPILE)
class HermesMojo : AbstractMojo() {

    @Parameter(property = "package", readonly = true, required = true)
    private lateinit var packageToScan: String

    private val hermesRunner = HermesRunner();

    override fun execute() {
        this.hermesRunner.genDSLForPackage(this.packageToScan)
    }
}