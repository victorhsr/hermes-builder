package io.github.victorhsr.hermes.maven

import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugins.annotations.LifecyclePhase
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import org.apache.maven.project.MavenProject

@Mojo(name = "hermes-dsl", defaultPhase = LifecyclePhase.COMPILE)
class HermesMojo: AbstractMojo() {

    @Parameter(defaultValue = "\${project}", readonly = true, required = true)
    private val project: MavenProject? = null

    override fun execute() {
        log.info(project!!.basedir.toString())
    }
}