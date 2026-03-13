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

package code.frontend.libs.katlaf.lists;

import code.frontend.libs.katlaf.buttons.IconButton;
import java.util.ArrayList;
import javafx.scene.layout.VBox;

/**
 * A VBox containing some instances of IconButton, presented in a vertical format.
 * Ideal for creating a sidebar.
 *
 * @since v3.0.0-beta
 * @see IconButton
 */
public class IconButtonList extends VBox {
    private final ArrayList<IconButton> buttons;

    public IconButtonList() {
        this.buttons = new ArrayList<IconButton>();
        this.setFillWidth(true);
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    public void add(final IconButton button) {
        this.buttons.add(button);
        this.getChildren().add(button);
    }

    public void add(final IconButton... buttons) {
        for (IconButton iconButton : buttons) {
            add(iconButton);
        }
    }

    public void select(final IconButton button) {
        this.deselectAll();
        button.useSelectedStyle();
    }

    public void select(final int index) {
        select(this.buttons.get(index));
    }

    public void deselectAll() {
        this.buttons.forEach(button -> button.useDeselectedStyle());
    }
}
