package com.api.util.sig4j.slot;

import com.api.util.sig4j.Slot;

import java.util.Objects;

/**
 * A slot with 0 arguments.
 */
@FunctionalInterface
public interface Slot0 extends Slot {

    void accept();
}
