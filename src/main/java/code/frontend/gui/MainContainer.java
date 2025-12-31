/*
   Copyright (C) 2026  Nicholas Siow

   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU Affero General Public License as
   published by the Free Software Foundation, either version 3 of the
   License, or (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU Affero General Public License for more details.

   You should have received a copy of the GNU Affero General Public License
   along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package code.frontend.gui;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class MainContainer extends HBox {
    private static MainContainer instance = null;

    private Sidebar sidebar;
    private Content content;

    private MainContainer() {
        this.sidebar = Sidebar.getInstance();
        this.content = Content.getInstance();
        this.setBackground(null);
        this.setFillHeight(true);
        this.sidebar.setMinWidth(200);
        HBox.setMargin(this.sidebar, new Insets(0, 5, 0, 0));
        HBox.setHgrow(this.content, Priority.ALWAYS);
        this.getChildren().addAll(this.sidebar, this.content);
    }

    public static MainContainer getInstance() {
        if (instance == null) {
            instance = new MainContainer();
        }
        return instance;
    }
}
