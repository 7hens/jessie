package cn.jessie.runtime.program

import cn.jessie.program.Program

abstract class AbstractProgram : Program {
    override fun equals(other: Any?): Boolean {
        return if (other is Program) {
            packageName == other.packageName
        } else {
            super.equals(other)
        }
    }

    override fun hashCode(): Int {
        return packageName.hashCode()
    }

    override fun toString(): String {
        return "${super.toString()}($packageName)"
    }
}