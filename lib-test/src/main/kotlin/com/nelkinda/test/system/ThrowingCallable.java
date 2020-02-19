package com.nelkinda.test.system;

public interface ThrowingCallable<T, E extends Throwable> {
    T call() throws E;
}
