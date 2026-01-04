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
import code.frontend.panels.SidebarFolderSelector;
import code.frontend.panels.SidebarStatsPane;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

public class Sidebar extends VBox {
    private static Sidebar instance = null;

    private final SidebarStatsPane STATS;
    private final SidebarFolderSelector FOLDER_SELECTOR;

    private Sidebar() {
        this.STATS = SidebarStatsPane.getInstance();
        this.FOLDER_SELECTOR = SidebarFolderSelector.getInstance();
        this.setBackground(Colour.createBG(Colour.SIDE_BAR, 0, 0));
        this.getChildren().addAll(this.STATS, this.FOLDER_SELECTOR);
        VBox.setMargin(this.STATS, new Insets(10));
        VBox.setMargin(this.FOLDER_SELECTOR, new Insets(20, 10, 5, 10));
    }

    public static Sidebar getInstance() {
        if (instance == null)
            instance = new Sidebar();
        return instance;
    }
}
