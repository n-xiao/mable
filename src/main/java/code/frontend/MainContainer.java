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

import code.frontend.libs.katlaf.ricing.RiceHandler;
import code.frontend.sidebar.Sidebar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class MainContainer extends Pane {
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
        final Sidebar sidebar;
        // TODO add the content!
        Container() {
            this.sidebar = new Sidebar();
            this.setBackground(null);
            this.setFillHeight(true);
            this.prefHeightProperty().bind(MainContainer.this.heightProperty());
            this.prefWidthProperty().bind(MainContainer.this.widthProperty());
            this.getChildren().addAll(this.sidebar);
        }
    }
}
