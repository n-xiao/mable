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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public final class MainContainer extends Pane {
    /*


     SINGLETON CLASS
    -------------------------------------------------------------------------------------*/

    private static MainContainer instance = null;
    public static MainContainer getInstance() {
        if (instance == null) {
            instance = new MainContainer();
            instance.getChildren().add(instance.new Container());
        }
        return instance;
    }

    private MainContainer() {
        this.setBackground(RiceHandler.createBG(RiceHandler.getColour("night"), 0, 0));
    }

    /*


     COMPOSITIONS
    -------------------------------------------------------------------------------------*/

    private class Container extends HBox {
        Container() {
            this.setBackground(null);
            this.setFillHeight(true);
            this.prefHeightProperty().bind(MainContainer.this.heightProperty());
            this.prefWidthProperty().bind(MainContainer.this.widthProperty());

            final CountdownList countdownList = new CountdownList();
            final ScrollPane scrollpane = new ScrollPane();
            scrollpane.setStyle("-fx-background: transparent;");
            scrollpane.setContent(countdownList);
            scrollpane.setFitToWidth(true);
            this.getChildren().add(scrollpane);

            /*
             * test
             */
            ArrayList<Countdown> test = new ArrayList<Countdown>();
            test.add(new Countdown("test", 1, 12, 2026));
            countdownList.populate(test);
        }
    }
}
