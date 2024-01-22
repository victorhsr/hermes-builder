package io.github.victorhsr.hermes.maven.util

import javax.lang.model.element.Name

class TestName(private val nameValue: String) : Name {

    override val length: Int
        get() = nameValue.length

    override fun contentEquals(cs: CharSequence) = nameValue == cs.toString()

    override fun get(index: Int) = nameValue[index]

    override fun subSequence(start: Int, end: Int) = nameValue.subSequence(start, end)

    override fun hashCode() = nameValue.hashCode()

    override fun equals(obj: Any?) = nameValue == obj.toString()

    override fun toString() = nameValue
}