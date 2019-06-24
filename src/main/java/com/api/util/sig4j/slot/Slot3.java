package com.api.util.sig4j.slot;

import com.api.util.sig4j.Slot;

/**
 * A slot with 3 generic arguments.
 *
 * @param <T> The type of the first argument.
 * @param <U> The type of the second argument.
 * @param <V> The type of the third argument.
 */
@FunctionalInterface
public interface Slot3<T, U, V> extends Slot {

    void accept(final T t, final U u, final V v);
}
