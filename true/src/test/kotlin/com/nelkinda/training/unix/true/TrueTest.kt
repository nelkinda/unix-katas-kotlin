package com.nelkinda.training.unix.`true`

import com.nelkinda.test.system.intercept
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

class TrueTest {
    @Test
    fun `true does nothing, successfully`() {
        val result = intercept { main() }
        assertAll(
                { result.assertStatus(0) },
                { result.assertStderrEmpty() },
                { result.assertStdoutEmpty() }
        )
    }
}
