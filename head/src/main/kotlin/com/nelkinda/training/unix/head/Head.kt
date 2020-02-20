package com.nelkinda.training.unix.head

import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.io.InputStreamReader
import kotlin.system.exitProcess

fun main(vararg args: String) {
    var status = 0
    if (args.isEmpty())
        BufferedReader(InputStreamReader(System.`in`)).use { r ->
            head(r)
        }
    else args.forEach { arg ->
        try {
            BufferedReader(FileReader(arg)).use { r ->
                head(r)
            }
        } catch (e: IOException) {
            System.err.format("head: %s%n", e.message)
            status = 1
        }
    }
    exitProcess(status)
}

private fun head(r: BufferedReader) {
    r
            .lines()
            .limit(10)
            .forEach(System.out::println)
}
