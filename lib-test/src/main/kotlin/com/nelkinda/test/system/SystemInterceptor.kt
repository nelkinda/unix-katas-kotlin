package com.nelkinda.test.system

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.function.Executable
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.PrintStream
import java.nio.charset.Charset
import java.util.concurrent.Callable

fun intercept(runnable: Runnable): InterceptionResult<Void?> {
    return intercept("", runnable)
}

fun <T> intercept(callable: Callable<T>): InterceptionResult<T> {
    return intercept("", callable)
}

fun intercept(stdin: String, runnable: Runnable): InterceptionResult<Void?> {
    return intercept(ByteArrayInputStream(stdin.toByteArray()), runnable)
}

fun <T> intercept(stdin: String, callable: Callable<T>): InterceptionResult<T> {
    return intercept(ByteArrayInputStream(stdin.toByteArray()), callable)
}

fun intercept(stdin: InputStream, runnable: Runnable): InterceptionResult<Void?> {
    return intercept(stdin, Callable<Void?> {
        runnable.run()
        null
    })
}

fun <T> intercept(stdin: InputStream, callable: Callable<T>): InterceptionResult<T> {
    val originalStdin = System.`in`
    val originalStdout = System.out
    val originalStderr = System.err
    val originalSecurityManager = System.getSecurityManager()
    val redirectedStderr = ByteArrayOutputStream()
    val redirectedStdout = ByteArrayOutputStream()
    val newSecurityManager = ExitInterceptingSecurityManager(originalSecurityManager)
    var result: T? = null
    var throwable: Throwable? = null
    try {
        System.setIn(stdin)
        System.setOut(PrintStream(redirectedStdout))
        System.setErr(PrintStream(redirectedStderr))
        System.setSecurityManager(newSecurityManager)
        result = callable.call()
    } catch (ignore: ExitException) {
        // ignored here, taken from the security manager
    } catch (e: Throwable) {
        throwable = e
    } finally {
        System.setIn(originalStdin)
        System.setOut(originalStdout)
        System.setErr(originalStderr)
        System.setSecurityManager(originalSecurityManager)
    }
    return InterceptionResult(redirectedStdout.toByteArray(), redirectedStderr.toByteArray(), newSecurityManager.status, throwable, result)
}

data class InterceptionResult<T>(
        private val stdout: ByteArray,
        private val stderr: ByteArray,
        private val status: Int?,
        private val exception: Throwable?,
        val result: T?
) {

    fun assertStdoutEmpty() {
        assertEquals(0, stdout.size) { String.format("expected stdout to be empty but was <%s>", String(stdout)) }
    }

    fun assertStdout(expectedStdout: ByteArray) {
        assertArrayEquals(expectedStdout, stdout)
    }

    fun assertStdout(expectedStdout: String) {
        assertEquals(expectedStdout, String(stdout))
    }

    fun assertStdout(expectedStdout: String, charset: Charset) {
        assertEquals(expectedStdout, String(stdout, charset))
    }

    fun assertStdoutFormat(expectedStdout: String, vararg args: Any?) {
        assertEquals(String.format(expectedStdout, *args), String(stdout))
    }

    fun assertStdoutFormat(expectedStdout: String, charset: Charset, vararg args: Any?) {
        assertEquals(String.format(expectedStdout, *args), String(stdout, charset))
    }

    fun assertStdoutMatches(regex: Regex) {
        assertTrue(String(stdout).matches(regex))
    }

    fun assertStdoutMatches(regex: Regex, charset: Charset) {
        assertTrue(String(stdout, charset).matches(regex))
    }

    fun assertStderrEmpty() {
        assertEquals(0, stderr.size) { String.format("expected stderr to be empty but was <%s>", String(stderr)) }
    }

    fun assertStderr(expectedStderr: ByteArray) {
        assertArrayEquals(expectedStderr, stderr)
    }

    fun assertStderr(expectedStderr: String) {
        assertEquals(expectedStderr, String(stderr))
    }

    fun assertStderr(expectedStderr: String, charset: Charset) {
        assertEquals(expectedStderr, String(stderr, charset))
    }

    fun assertStderrFormat(expectedStderr: String, vararg args: Any?) {
        assertEquals(String.format(expectedStderr, *args), String(stderr))
    }

    fun assertStderrFormat(expectedStderr: String, charset: Charset, vararg args: Any?) {
        assertEquals(String.format(expectedStderr, *args), String(stderr, charset))
    }

    fun assertStderrMatches(regex: Regex) {
        assertTrue(String(stderr).matches(regex))
    }

    fun assertStderrMatches(regex: Regex, charset: Charset) {
        assertTrue(String(stderr, charset).matches(regex))
    }

    fun assertStatus(expectedStatus: Int?) {
        assertEquals(expectedStatus, status)
    }

    fun assertNoError() {
        assertAll(Executable { assertNoException() }, Executable { assertSuccess() })
    }

    fun assertNoException() {
        assertNull(exception, "expected no exception to be thrown")
    }

    fun assertSuccess() {
        assertTrue(status == null || status == 0, "expected System.exit() to not be called or called with 0")
    }
}
