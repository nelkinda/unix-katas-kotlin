package com.nelkinda.test.system;

public interface ThrowingRunnable<E extends Throwable> {
    void run() throws E;
}
