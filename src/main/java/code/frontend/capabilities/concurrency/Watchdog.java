/*
    Copyright (C) 2026 Nicholas Siow <nxiao.dev@gmail.com>
    DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package code.frontend.capabilities.concurrency;

import java.util.ArrayList;
import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;

public final class Watchdog extends ScheduledService<Void> {
    private static final ArrayList<Updatable> UPDATABLES = new ArrayList<Updatable>();
    private static Watchdog watchdog = null;

    private Watchdog() {}

    /*


     PROTECTED API
    -------------------------------------------------------------------------------------*/

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> {
                    for (Updatable updatable : UPDATABLES) {
                        updatable.update();
                    }
                });
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

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    public static void startWatchdog() {
        watchdog = new Watchdog();
        watchdog.setPeriod(Duration.millis(1000));
        watchdog.setRestartOnFailure(true);
        watchdog.setMaximumFailureCount(10);
        watchdog.start();
    }

    public static void watch(Updatable updatable) {
        UPDATABLES.add(updatable);
    }

    public static void watch(Updatable... updatables) {
        for (Updatable updatable : updatables) {
            watch(updatable);
        }
    }
}
