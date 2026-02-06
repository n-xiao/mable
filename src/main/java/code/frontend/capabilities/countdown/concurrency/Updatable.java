package code.frontend.capabilities.countdown.concurrency;

/**
 * This allows the {@link Watchdog} to send updates to
 * implementations of this interface.
 */
public interface Updatable {
    /**
     * Will be executed by the Watchdog's concurrent thread.
     */
    public void update();
}
