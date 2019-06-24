package com.api.util.sig4j;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The base class of all signals.
 *
 * Note:
 *  Connecting and emitting slots concurrently is thread safe without blocking.
 */
public abstract class Signal {
    /**
     * The {@link SlotDispatcher} of {@link ConnectionType#QUEUED} connected
     * slots.
     */
    private static final SlotDispatcher DISPATCHER = new SlotDispatcher();

    static {
        DISPATCHER.start();
    }

    private final AtomicBoolean enabled = new AtomicBoolean(true);

    /**
     * The queue of {@link ConnectionType#DIRECT} connected slots.
     */
    private final Queue<Slot> direct = new ConcurrentLinkedDeque<>();

    /**
     * The queue of {@link ConnectionType#QUEUED} connected slots.
     */
    private final Queue<Slot> queued = new ConcurrentLinkedDeque<>();

    /**
     * The queue of {@link ConnectionType#BLOCKING_QUEUED} connected slots.
     */
    private final Queue<Slot> blockingQueued = new ConcurrentLinkedDeque<>();

    /**
     * The queue of dispatched slots {@see SlotDispatcher}.
     */
    private final Queue<DispatcherAssociation> dispatched =
            new ConcurrentLinkedDeque<>();

    /**
     * Removes all connected slots. Clearing a signal is not an atomic
     * operation and may result in a non-empty slot queue if one of the
     * 'connect' methods is used concurrently.
     */
    public void clear() {
        direct.clear();
        queued.clear();
        blockingQueued.clear();
        dispatched.clear();
    }

    /**
     * Connects the given slot using {@link ConnectionType#DIRECT}. This
     * function is equivalent to {@code connect(slot, ConnectionType.DIRECT)}.
     *
     * @param slot The slot to connect.
     * @throws IllegalArgumentException If {@code slot} is null.
     */
    protected void connect(final Slot slot) {
        if (slot == null) {
            throw new IllegalArgumentException("slot is null");
        }
        direct.add(slot);
    }

    /**
     * Connects the given slot according to {@link ConnectionType}.
     *
     * @param slot The slot to connect.
     * @param type The {@link ConnectionType} to use.
     * @throws IllegalArgumentException If {@code slot} or {@code type} is null.
     */
    protected void connect(final Slot slot, final ConnectionType type) {
        if (slot == null) {
            throw new IllegalArgumentException("slot is null");
        } else if (type == null) {
            throw new IllegalArgumentException("connection type is null");
        }

        switch (type) {
            case DIRECT:
                direct.add(slot);
                break;
            case QUEUED:
                queued.add(slot);
                break;
            case BLOCKING_QUEUED:
                blockingQueued.add(slot);
                break;
            default:
                throw new RuntimeException(
                    "unknown connection type: " + type.toString());
        }
    }

    /**
     * Connects the given slot and actuates it within the thread context
     * of the given {@link SlotDispatcher} if the signal is emitted.
     *
     * @param dispatcher The {@link SlotDispatcher} to use.
     * @param slot       The slot to connect.
     * @throws IllegalArgumentException If {@code dispatcher} or
     *                      {@code slot} is null.
     */
    protected void connect(final SlotDispatcher dispatcher, final Slot slot) {
        if (dispatcher == null) {
            throw new IllegalArgumentException("dispatcher is null");
        } else if (slot == null) {
            throw new IllegalArgumentException("slot is null");
        }
        dispatched.add(new DispatcherAssociation(dispatcher, slot));
    }

    /**
     * Emits this signal with the given arguments.
     *
     * @param args The arguments to use while emitting this signal.
     */
    protected void emit(final Object... args) {
        if (enabled.get()) {
            // Actuate the slots this method needs to wait for at first
            // in order to reduce the blocking time.
            int nAcquire = 0;
            final Semaphore sem = new Semaphore(nAcquire);
            for (final Slot s : blockingQueued) {
                nAcquire++;
                final SlotActuation sa = new SemSlotActuation(s, args, sem);
                DISPATCHER.actuate(sa);
            }
            dispatched.forEach(da -> {
                final SlotActuation sa = new SlotActuation(da.slot, args);
                da.slotDispatcher.actuate(sa);
            });
            queued.forEach(s -> {
                final SlotActuation sa = new SlotActuation(s, args);
                DISPATCHER.actuate(sa);
            });
            direct.forEach(s -> actuate(s, args));
            // Wait for the remaining slots.
            try {
                sem.acquire(nAcquire);
            } catch (InterruptedException e) {/**/}
        }
    }

    /**
     * A callback function used to actuate a single slot.
     *
     * The implementer of this function does not need to create any threads
     * but cast down the given slot and actuate it with the given arguments.
     *
     * This function should not have any side effects to this class.
     *
     * @param slot The slot to actuate.
     * @param args The arguments of the actuated slot.
     */
    protected abstract void actuate(final Slot slot, final Object[] args);



    /**
     * Associates a connected slot with its dispatcher.
     */
    private static class DispatcherAssociation {
        private final SlotDispatcher slotDispatcher;
        private final Slot slot;

        private DispatcherAssociation(final SlotDispatcher sd, final Slot s) {
            slotDispatcher = sd;
            slot = s;
        }
    }

    /**
     * Represents an actual slot actuation. Use {@link #actuate()} to actuate
     * the slot with its arguments.
     */
    class SlotActuation {
        private final Slot slot;
        private final Object[] arguments;

        private SlotActuation(final Slot s, final Object[] args) {
            slot = s;
            arguments = args;
        }

        public void actuate() {
            Signal.this.actuate(slot, arguments);
        }
    }

    /**
     * This class is used for slots connected with
     * {@link ConnectionType#BLOCKING_QUEUED}. By sharing the same
     * {@link Semaphore} among various instances of this class, the method
     * {@link Signal#emit(Object...)} is able to wait for all slots to be
     * actuated before returning to the context of the caller.
     */
    private class SemSlotActuation extends SlotActuation {
        private final Semaphore semaphore;

        SemSlotActuation(final Slot s, final Object[] args,
                         final Semaphore sem) {
            super(s, args);
            semaphore = sem;
        }

        @Override
        public void actuate() {
            super.actuate();
            semaphore.release();
        }
    }
}
