package com.nelkinda.training.unix.head

import com.nelkinda.test.system.intercept
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

class HeadTest {
    @Test
    fun `head of nothing prints nothing`() {
        val result = intercept { main() }
        assertAll(
                { result.assertStdoutEmpty() },
                { result.assertStderrEmpty() },
                { result.assertSuccess() }
        )
    }

    @Test
    fun `head of a single line prints that line`() {
        val result = intercept("hello\n") { main() }
        assertAll(
                { result.assertStdoutFormat("hello%n") },
                { result.assertStderrEmpty() },
                { result.assertSuccess() }
        )
    }

    @Test
    fun `head of 11 lines prints 10 lines`() {
        val result = intercept("hello\n".repeat(11)) { main() }
        assertAll(
                { result.assertStdoutFormat("hello%n".repeat(10)) },
                { result.assertStderrEmpty() },
                { result.assertSuccess() }
        )
    }

    @Test
    fun `head of file prints that file`() {
        val result = intercept { main(testFile("1_line.txt")) }
        assertAll(
                { result.assertStdoutFormat("One of One%n") },
                { result.assertStderrEmpty() },
                { result.assertSuccess() }
        )
    }

    @Test
    fun `head of two files prints from both files`() {
        val result = intercept { main(testFile("1_line.txt"), testFile("2_lines.txt")) }
        assertAll(
                { result.assertStdoutFormat("One of One%nOne of Two%nTwo of Two%n") },
                { result.assertStderrEmpty() },
                { result.assertSuccess() }
        )
    }

    @Test
    fun `prints the first ten lines of files`() {
        val result = intercept { main(testFile("11.txt"), testFile("12.txt")) }
        assertAll(
                { result.assertStdoutFormat("11%n".repeat(10) + "12%n".repeat(10)) },
                { result.assertStderrEmpty() },
                { result.assertSuccess() }
        )
    }

    @Test
    fun `prints error messages but still operates`() {
        val result = intercept { main("non-existent-1", testFile("11.txt"), "non-existent-2", testFile("12.txt"), "non-existent-3")}
        assertAll(
                { result.assertStdoutFormat("11%n".repeat(10) + "12%n".repeat(10)) },
                { result.assertStderrFormat(
                        "head: non-existent-1 (No such file or directory)%n" +
                                "head: non-existent-2 (No such file or directory)%n" +
                                "head: non-existent-3 (No such file or directory)%n"
                ) },
                { result.assertStatus(1) }
        )
    }

    private fun testFile(basename: String): String = javaClass.getResource(basename).file.toString()
}