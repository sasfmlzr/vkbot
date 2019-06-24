package com.api.util.sig4j.slot;

import com.api.util.sig4j.Slot;

import java.util.Objects;

/**
 * A slot with 2 generic arguments.
 *
 * @param <T> The type of the first argument.
 * @param <U> The type of the second argument.
 */
@FunctionalInterface
public interface Slot2<T, U> extends Slot {

    void accept(final T t, final U u);
}
