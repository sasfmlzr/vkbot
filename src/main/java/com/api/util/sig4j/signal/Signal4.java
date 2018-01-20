package com.api.util.sig4j.signal;

import com.api.util.sig4j.ConnectionType;
import com.api.util.sig4j.Signal;
import com.api.util.sig4j.SlotDispatcher;
import com.api.util.sig4j.Slot;
import com.api.util.sig4j.slot.Slot4;

/**
 * A signal with 4 generic arguments.
 *
 * @param <T> The type of the first argument.
 * @param <U> The type of the second argument.
 * @param <V> The type of the third argument.
 * @param <W> The type of the forth argument.
 */
public class Signal4<T, U, V, W> extends Signal {

    /**
     * @see Signal#connect(Slot)
     */
    public void connect(final Slot4<T, U, V, W> slot) {
        super.connect(slot);
    }

    /**
     * @see Signal#connect(Slot, ConnectionType)
     */
    public void connect(final Slot4<T, U, V, W> slot,
                        final ConnectionType type) {
        super.connect(slot, type);
    }

    /**
     * @see Signal#connect(SlotDispatcher, Slot)
     */
    public void connect(final SlotDispatcher dispatcher,
                        final Slot4<T, U, V, W> slot) {
        super.connect(dispatcher, slot);
    }

    /**
     * @see Signal#emit(Object...)
     */
    public void emit(final T t, final U u, final V v, final W w) {
        super.emit(t, u, v, w);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void actuate(final Slot slot, final Object[] args) {
        ((Slot4<T, U, V, W>) slot).accept(
                (T)args[0], (U)args[1], (V)args[2], (W)args[3]);
    }
}
