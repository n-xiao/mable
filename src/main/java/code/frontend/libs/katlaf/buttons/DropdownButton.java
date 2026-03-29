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

package code.frontend.libs.katlaf.buttons;

import code.frontend.libs.katlaf.menus.RightClickMenu;
import javafx.scene.input.MouseEvent;

/**
 * This represents a button that, when clicked, provides a dropdown list
 * of clickable buttons, facilitated by
 * {@link code.frontend.libs.katlaf.menus.RightClickMenu}
 * <br>By default, this button is just an empty
 * {@link javafx.scene.layout.StackPane} <br> It is the responsibility of child classes
 * to implement a user interface for this class; this class only provides basic logic.
 *
 * @since v3.1.0
 */
public class DropdownButton extends ButtonFoundation {
    private final RightClickMenu rcm;

    public DropdownButton() {
        this.rcm = new RightClickMenu();
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    /**
     * On mouse pressed, this button will init the RightClickMenu contained
     * at the top left hand corner of this instance.
     */
    @Override
    public void onMousePressed(MouseEvent event) {
        final double x = event.getSceneX() - event.getX();
        final double y = event.getSceneY() - event.getY();
        this.rcm.spawn(x, y);
    }

    /**
     * Returns the {@link RightClickMenu} <br> instance contained within this
     * class. Useful for adding buttons to it.
     *
     * @return RightClickMenu
     */
    public final RightClickMenu getMenu() {
        return this.rcm;
    }
}
