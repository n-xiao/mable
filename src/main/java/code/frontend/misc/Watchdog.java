package code.frontend.misc;

import code.frontend.panels.CountdownPaneView;
import java.time.LocalDate;
import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;

public class Watchdog extends ScheduledService<Void> {
    private static Watchdog watchdog = null;

    private Watchdog() {}

    public static void startWatchdog() {
        watchdog = new Watchdog();
        watchdog.setPeriod(Duration.millis(1000));
        watchdog.setRestartOnFailure(true);
        watchdog.setMaximumFailureCount(10);
        watchdog.start();
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                final CountdownPaneView CPV = CountdownPaneView.getInstance();
                Platform.runLater(() -> { CPV.repopulate(LocalDate.now()); });
                return null;
            }
        };
    }

    @Override
    protected void failed() {
        super.failed();
        if (getCurrentFailureCount() == getMaximumFailureCount())
            System.err.println("Watchdog has died! UI will no longer automatically update.");
        else {
            System.err.println("Watchdog has failed! Retrying...");
        }
    }
}
