package com.api.util.sig4j.slot;

import com.api.util.sig4j.Slot;

/**
 * A slot with 0 arguments.
 */
@FunctionalInterface
public interface Slot0 extends Slot {

    void accept();
}
