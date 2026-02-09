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

package code.frontend.capabilities.countdown.components;

import code.frontend.libs.katlaf.ricing.RiceHandler;
import javafx.scene.layout.VBox;

/**
 * The sidebar user interface consisting of a countdown create button,
 * and a reorderable folder list.
 */
public class FolderSidebar extends VBox {
    public FolderSidebar() {
        this.setBackground(RiceHandler.createBG(RiceHandler.getColour("midnight"), 0, 0));
    }

    private void init() {}
}
