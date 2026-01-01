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

import code.frontend.misc.Vals.Colour;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class Sidebar extends VBox {
    private static Sidebar instance = null;

    private final FlowPane PAGE_SELECTOR;
    private final VBox STATS;
    private final ScrollPane FOLDER_SELECTOR;

    private Sidebar() {
        PAGE_SELECTOR = null;
        STATS = null;
        FOLDER_SELECTOR = null;
        // todo
        this.setBackground(Colour.createBG(Colour.SIDE_BAR, 0, 0));
    }

    public static Sidebar getInstance() {
        if (instance == null)
            instance = new Sidebar();
        return instance;
    }
}
