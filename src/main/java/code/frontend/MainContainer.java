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

import code.frontend.capabilities.countdown.components.CountdownContainer;
import code.frontend.capabilities.countdown.components.Sidebar;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

public class MainContainer extends StackPane {
    private static MainContainer instance = null;

    private HBox countdownContainer;

    private MainContainer() {
        this.setBackground(null);
    }

    private void initCountdown() {
        this.countdownContainer = new HBox();
        this.countdownContainer.setBackground(null);
        this.countdownContainer.setFillHeight(true);
        CountdownContainer cmc = CountdownContainer.getInstance();
        Sidebar sidebar = Sidebar.getInstance();
        sidebar.setMinWidth(250);
        HBox.setMargin(sidebar, new Insets(0, 5, 0, 0));
        HBox.setMargin(cmc, new Insets(10));
        HBox.setHgrow(cmc, Priority.ALWAYS);
        this.countdownContainer.getChildren().addAll(sidebar, cmc);
        this.getChildren().add(this.countdownContainer);
    }

    public void init() {
        this.initCountdown();
    }

    public static MainContainer getInstance() {
        if (instance == null) {
            instance = new MainContainer();
        }
        return instance;
    }
}
