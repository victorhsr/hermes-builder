package com.github.victorhsr.hermes.core.annotations

/**
 * Mark a class to be used as the root of the generated DSL.
 * The goal of the generated DSL is to build the root class instance
 *
 * @author victorhsr
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class DSLRoot