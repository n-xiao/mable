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

package code.frontend;

import code.backend.data.Countdown;
import code.backend.data.CountdownHandler;
import code.backend.data.LegendHandler;
import code.frontend.capabilities.concurrency.Watchdog;
import code.frontend.capabilities.countdowns.CountdownList.CountdownFilter;
import code.frontend.capabilities.sidebar.Sidebar;
import code.frontend.capabilities.views.CountdownView;
import code.frontend.capabilities.views.SettingsView;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import java.util.Set;
import javafx.application.Platform;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public final class MainContainer extends HBox {
    private static MainContainer instance = null;
    private static Stage stage = null;

    public static void setStage(final Stage stage) {
        if (stage == null)
            MainContainer.stage = stage;
    }

    public static Stage getStage() {
        return MainContainer.stage;
    }

    public static MainContainer getInstance() {
        if (instance == null) {
            instance = new MainContainer();
            instance.init();
        }
        return instance;
    }

    /*


     FIELDS AND CONSTRUCTOR
    -------------------------------------------------------------------------------------*/

    private Sidebar sidebar;
    private CountdownView activeView;
    private CountdownView completedView;
    private CountdownView deletedView;
    private SettingsView settingsView;

    private MainContainer() {}

    /*


     PRIVATE API
    -------------------------------------------------------------------------------------*/

    private void init() {
        /*
         * init the content first
         */
        final CountdownView activeView =
            new CountdownView("Active Countdowns", LegendHandler.getLegends()) {
                @Override
                public Set<Countdown> getCountdowns() {
                    return CountdownHandler.getAll();
                }
            };
        final CountdownView completedView =
            new CountdownView("Completed Countdowns", CountdownFilter.COMPLETED) {
                @Override
                public Set<Countdown> getCountdowns() {
                    return CountdownHandler.getAll();
                }
            };
        final CountdownView deletedView =
            new CountdownView("Deleted Countdowns", CountdownFilter.DELETED) {
                @Override
                public Set<Countdown> getCountdowns() {
                    return CountdownHandler.getAll();
                }
            };
        final SettingsView settingsView = SettingsView.setup();

        init(activeView, completedView, deletedView, settingsView);

        this.selectActiveView();
    }

    private void init(final CountdownView activeView, final CountdownView completedView,
        final CountdownView deletedView, final SettingsView settingsView) {
        this.activeView = activeView;
        this.completedView = completedView;
        this.deletedView = deletedView;
        this.settingsView = settingsView;

        final StackPane container = new StackPane();
        container.getChildren().addAll(
            this.activeView, this.completedView, this.deletedView, this.settingsView);
        container.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        HBox.setHgrow(container, Priority.ALWAYS);

        this.setBackground(RiceHandler.createBG(RiceHandler.getColour("night"), 0, 0));
        this.setFillHeight(true);

        this.sidebar = new Sidebar();
        this.getChildren().addAll(this.sidebar, container);

        Watchdog.watch(this.activeView, this.completedView, this.deletedView);
    }

    private void hideAllViews() {
        this.activeView.setVisible(false);
        this.completedView.setVisible(false);
        this.deletedView.setVisible(false);
        this.settingsView.setVisible(false);
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    public static synchronized void refresh() {
        Platform.runLater(() -> {
            Watchdog.unwatch(instance.activeView, instance.completedView, instance.deletedView);

            final CountdownView activeView =
                new CountdownView("Active Countdowns", LegendHandler.getLegends()) {
                    @Override
                    public Set<Countdown> getCountdowns() {
                        return CountdownHandler.getAll();
                    }
                };
            activeView.setVisible(instance.activeView.isVisible());

            final CountdownView completedView =
                new CountdownView("Completed Countdowns", CountdownFilter.COMPLETED) {
                    @Override
                    public Set<Countdown> getCountdowns() {
                        return CountdownHandler.getAll();
                    }
                };
            completedView.setVisible(instance.completedView.isVisible());

            final CountdownView deletedView =
                new CountdownView("Deleted Countdowns", CountdownFilter.DELETED) {
                    @Override
                    public Set<Countdown> getCountdowns() {
                        return CountdownHandler.getAll();
                    }
                };
            deletedView.setVisible(instance.deletedView.isVisible());

            final SettingsView settingsView = SettingsView.setup();
            settingsView.setVisible(instance.settingsView.isVisible());

            instance.getChildren().clear();

            instance.init(activeView, completedView, deletedView, settingsView);
            instance.sidebar.match(activeView, completedView, deletedView, settingsView);
        });
    }

    public void selectActiveView() {
        this.hideAllViews();
        this.activeView.setVisible(true);
    }

    public void selectCompletedView() {
        this.hideAllViews();
        this.completedView.setVisible(true);
    }

    public void selectDeletedView() {
        this.hideAllViews();
        this.deletedView.setVisible(true);
    }

    public void selectSettingsView() {
        this.hideAllViews();
        this.settingsView.setVisible(true);
    }
}
