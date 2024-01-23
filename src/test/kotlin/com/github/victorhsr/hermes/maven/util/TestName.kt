package com.github.victorhsr.hermes.maven.util

import javax.lang.model.element.Name

class TestName(private val nameValue: String) : Name {

    override val length: Int
        get() = nameValue.length

    override fun contentEquals(cs: CharSequence) = nameValue == cs.toString()

    override fun get(index: Int) = nameValue[index]

    override fun subSequence(startIndex: Int, endIndex: Int) = nameValue.subSequence(startIndex, endIndex)

    override fun hashCode() = nameValue.hashCode()

    override fun equals(other: Any?) = nameValue == other.toString()

    override fun toString() = nameValue
}