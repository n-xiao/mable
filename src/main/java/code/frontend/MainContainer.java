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
import code.frontend.capabilities.countdowns.CountdownList;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import java.util.ArrayList;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

public final class MainContainer extends Pane {
    /*


     SINGLETON CLASS
    -------------------------------------------------------------------------------------*/

    private static MainContainer instance = null;
    public static MainContainer getInstance() {
        if (instance == null) {
            instance = new MainContainer();
            instance.getChildren().add(instance.new CountdownListContainer());
        }
        return instance;
    }

    private MainContainer() {
        this.setBackground(RiceHandler.createBG(RiceHandler.getColour("night"), 0, 0));
    }

    /*


     COMPOSITIONS
    -------------------------------------------------------------------------------------*/

    private class CountdownListContainer extends Region {
        CountdownListContainer() {
            this.setBackground(null);
            this.prefHeightProperty().bind(MainContainer.this.heightProperty());
            this.prefWidthProperty().bind(MainContainer.this.widthProperty());

            final CountdownList countdownList = new CountdownList();
            final ScrollPane scrollpane = new ScrollPane();
            scrollpane.setStyle("-fx-background: transparent;");
            scrollpane.setBackground(null);
            scrollpane.setContent(countdownList);
            scrollpane.setFitToWidth(true);
            scrollpane.prefWidthProperty().bind(this.widthProperty());
            scrollpane.prefHeightProperty().bind(this.heightProperty());
            scrollpane.setHbarPolicy(ScrollBarPolicy.NEVER);
            scrollpane.setVbarPolicy(ScrollBarPolicy.NEVER);
            this.getChildren().add(scrollpane);

            /*
             * click "anywhere" to deselect
             */
            scrollpane.setOnMousePressed(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    countdownList.getSelector().deselectAll();
                    event.consume();
                }
            });

            /*
             * test
             */
            ArrayList<Countdown> test = new ArrayList<Countdown>();
            test.add(new Countdown("testOverdue", 1, 1, 2025));
            test.add(new Countdown("test", 1, 12, 2026));
            countdownList.populate(test);
        }
    }
}
