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

import code.backend.data.CountdownHandler;
import code.backend.data.LegendHandler;
import code.frontend.capabilities.concurrency.Watchdog;
import code.frontend.capabilities.countdowns.CountdownList.CountdownFilter;
import code.frontend.capabilities.sidebar.Sidebar;
import code.frontend.capabilities.views.CountdownView;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

public final class MainContainer extends HBox {
    private static MainContainer instance = null;
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

    private CountdownView activeView;
    private CountdownView completedView;
    private CountdownView deletedView;

    private MainContainer() {}

    /*


     PRIVATE API
    -------------------------------------------------------------------------------------*/

    private void init() {
        /*
         * init the content first
         */
        final StackPane container = new StackPane();
        this.activeView = new CountdownView(
            "Active Countdowns", LegendHandler.getLegends(), CountdownHandler.getAll());
        this.completedView = new CountdownView(
            "Completed Countdowns", CountdownHandler.getAll(), CountdownFilter.COMPLETED);
        this.deletedView = new CountdownView(
            "Deleted Countdowns", CountdownHandler.getAll(), CountdownFilter.DELETED);

        container.getChildren().addAll(this.activeView, this.completedView, this.deletedView);
        container.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        HBox.setHgrow(container, Priority.ALWAYS);

        this.selectActiveView();
        /*
         * then do everything else
         */
        this.setBackground(RiceHandler.createBG(RiceHandler.getColour("night"), 0, 0));
        this.setFillHeight(true);
        this.getChildren().addAll(new Sidebar(), container);

        Watchdog.watch(this.activeView, this.completedView, this.deletedView);
    }

    private void hideAllViews() {
        this.activeView.setVisible(false);
        this.completedView.setVisible(false);
        this.deletedView.setVisible(false);
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

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
}
