package com.nelkinda.training.unix.cat

import com.nelkinda.test.system.intercept
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

class CatTest {
    @Test
    fun `cat from empty stdin prints nothing`() {
        val result = intercept(Runnable { main() })
        assertAll(
                { result.assertStderrEmpty() },
                { result.assertStdoutEmpty() },
                { result.assertSuccess() }
        )
    }

    @Test
    fun `cat from stdin prints its contents`() {
        val result = intercept("foo", Runnable { main() })
        assertAll(
                { result.assertStderrEmpty() },
                { result.assertStdout("foo") },
                { result.assertSuccess() }
        )
    }

    @Test
    fun `cat from a file prints that file`() {
        val result = intercept(Runnable { main(testFile("input1.txt")) })
        assertAll(
                { result.assertStderrEmpty() },
                { result.assertStdout("File Input 1\n") },
                { result.assertSuccess() }
        )
    }

    @Test
    fun `cat from multiple files concatenates those files`() {
        val result = intercept(Runnable { main(testFile("input1.txt"), testFile("input2.txt")) })
        assertAll(
                { result.assertStderrEmpty() },
                { result.assertStdout("File Input 1\nFile Input 2\n") },
                { result.assertSuccess() }
        )
    }

    @Test
    fun `cat with a non-existent file prints an error message`() {
        val result = intercept(Runnable { main("non-existent-file") })
        assertAll(
                { result.assertStderrFormat("cat: non-existent-file (No such file or directory)%n") },
                { result.assertStdoutEmpty() },
                { result.assertStatus(1) }
        )
    }

    @Test
    fun `cat with mixed existing and non-existing files concatenates the existing files and prints all errors`() {
        val result = intercept(Runnable { main("non-existent-1", testFile("input1.txt"), "non-existent-2", testFile("input2.txt"), "non-existent-3") })
        assertAll(
                { result.assertStderrFormat(
                        "cat: non-existent-1 (No such file or directory)%n" +
                                "cat: non-existent-2 (No such file or directory)%n" +
                                "cat: non-existent-3 (No such file or directory)%n"
                ) },
                { result.assertStdout("File Input 1\nFile Input 2\n") },
                { result.assertStatus(1) }
        )
    }

    private fun testFile(basename: String): String = javaClass.getResource(basename).file.toString()
}