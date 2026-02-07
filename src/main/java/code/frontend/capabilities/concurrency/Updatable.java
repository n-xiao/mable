package code.frontend.capabilities.concurrency;

/**
 * This allows the {@link Watchdog} to send updates to
 * implementations of this interface.
 */
public interface Updatable {
    /**
     * This method will be called by the Watchdog's thread at regular intervals.
     * Implementations should use this method to update any UI elements that
     * may change throughout time.
     *
     * Note: Implementations need to call {@link Watchdog#watch(Updatable)}
     * and add themselves to the list of {@link Updatable} stuff that
     * the Watchdog will watch.
     */
    public void update();
}
