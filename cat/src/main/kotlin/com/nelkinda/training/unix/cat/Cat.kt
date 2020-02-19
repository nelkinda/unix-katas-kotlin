package com.nelkinda.training.unix.cat

import java.io.FileInputStream
import java.io.IOException
import kotlin.system.exitProcess

fun main(vararg args: String) {
    var exitCode = 0
    if (args.isEmpty())
        System.`in`.transferTo(System.out)
    else
        args.forEach { arg: String ->
            try {
                FileInputStream(arg).use { i ->
                    i.transferTo(System.out)
                }
            } catch (e: IOException) {
                System.err.format("cat: %s%n", e.message)
                exitCode = 1
            }
        }
    exitProcess(exitCode)
}