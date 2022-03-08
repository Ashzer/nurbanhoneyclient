package org.devjj.platform.nurbanhoney.core.extension

fun Collection<Any>?.isIterable() = !this.isNullOrEmpty()

fun Collection<Any>.getExclusiveList(dest : Collection<Any>) = this.filter { !dest.contains(it) }.toList()