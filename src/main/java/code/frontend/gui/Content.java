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

import code.frontend.gui.pages.HomePage;
import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;

public class Content extends StackPane {
    private static Content instance = null;

    private Content() {
        this.setBackground(null);
        HomePage homepage = HomePage.getInstance();
        StackPane.setMargin(homepage, new Insets(10));
        this.getChildren().addAll(HomePage.getInstance());
    }

    public static Content getInstance() {
        if (instance == null) {
            instance = new Content();
        }
        return instance;
    }
}
