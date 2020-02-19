package com.nelkinda.training.unix.echo

import com.nelkinda.test.system.intercept
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

class EchoTest {
    @Test
    fun `echo without arguments prints an empty line`() {
        val result = intercept(Runnable{main()})
        assertAll(
                {result.assertStderrEmpty()},
                {result.assertStdoutFormat("%n")},
                {result.assertSuccess()}
        )
    }

    @Test
    fun `echo with one argument prints that argument`() {
        val result = intercept(Runnable{main("hello")})
        assertAll(
                {result.assertStderrEmpty()},
                {result.assertStdoutFormat("hello%n")},
                {result.assertSuccess()}
        )
    }

    @Test
    fun `echo with multiple arguments concatenates them with a single space`() {
        val result = intercept(Runnable{main("hello", "world")})
        assertAll(
                {result.assertStderrEmpty()},
                {result.assertStdoutFormat("hello world%n")},
                {result.assertSuccess()}
        )
    }
}