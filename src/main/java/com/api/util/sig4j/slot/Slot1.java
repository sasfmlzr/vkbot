package com.api.util.sig4j.slot;

import com.api.util.sig4j.Slot;

/**
 * A slot with 1 generic argument.
 *
 * @param <T> The type of the argument.
 */
@FunctionalInterface
public interface Slot1<T> extends Slot {

    void accept(final T t);
}
