package com.nelkinda.training.unix.`false`

import com.nelkinda.test.system.intercept
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

class FalseTest {
    @Test
    fun `false does nothing, unsuccessfully`() {
        val result = intercept("", Runnable {main()})
        assertAll(
                {result.assertStatus(1)},
                {result.assertStderrEmpty()},
                {result.assertStdoutEmpty()}
        )
    }
}