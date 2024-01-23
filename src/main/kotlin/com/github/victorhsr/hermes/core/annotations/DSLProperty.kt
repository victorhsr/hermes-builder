package com.github.victorhsr.hermes.core.annotations

/**
 * Annotation used to customize the name of the method
 * generated for that specific field. The default value
 * is the name of the attribute.
 *
 * @author victorhsr
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class DSLProperty(val value: String)
