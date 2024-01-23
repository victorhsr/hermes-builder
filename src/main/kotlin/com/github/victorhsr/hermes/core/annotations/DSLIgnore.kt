package com.github.victorhsr.hermes.core.annotations

/**
 * Mark an attribute to be ignored when the DSL code
 * is getting generated
 *
 * @author victorhsr
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class DSLIgnore()
