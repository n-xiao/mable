package code.frontend.misc;

import code.frontend.panels.CountdownPaneView;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;

public class Watchdog extends ScheduledService<Void> {
    private static Watchdog watchdog = null;
    private static boolean updated = false;

    private Watchdog() {}

    public static void startWatchdog() {
        watchdog = new Watchdog();
        watchdog.setDelay(Duration.millis(5000));
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
                LocalTime timeNow = LocalTime.now();
                int hour = timeNow.getHour();
                int minute = timeNow.getMinute();
                final boolean IS_UPDATE_TIME = hour == 0 && minute <= 1;
                if (!updated && IS_UPDATE_TIME) {
                    final CountdownPaneView CPV = CountdownPaneView.getInstance();
                    final boolean HAS_LOCK = CPV.getLock().tryLock(2, TimeUnit.SECONDS);
                    if (HAS_LOCK) {
                        try {
                            CPV.repopulate(LocalDate.now());
                        } finally {
                            CPV.getLock().unlock();
                        }
                        updated = true;
                    }
                } else if (!IS_UPDATE_TIME) {
                    updated = false;
                }
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
            System.out.println("Watchdog has failed! Retrying...");
        }
    }
}
