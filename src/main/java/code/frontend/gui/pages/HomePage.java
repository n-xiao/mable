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

package code.frontend.gui.pages;

import code.frontend.misc.Vals.Colour;
import code.frontend.panels.CountdownPaneControls;
import code.frontend.panels.CountdownPaneView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class HomePage extends VBox {
    private static HomePage instance = null;
    private CountdownPaneControls controls;
    private CountdownPaneView view;

    private HomePage() {
        this.controls = CountdownPaneControls.getInstance();
        this.view = CountdownPaneView.getInstance();
        this.setFillWidth(true);
        VBox.setVgrow(this.view, Priority.ALWAYS);
        this.setBackground(Colour.createBG(Colour.BACKGROUND, 0, 0));
        this.getChildren().addAll(this.controls, this.view);
    }

    public static HomePage getInstance() {
        if (instance == null)
            instance = new HomePage();
        return instance;
    }
}
