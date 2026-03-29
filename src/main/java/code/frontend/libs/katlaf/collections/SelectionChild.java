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

package code.frontend.libs.katlaf.collections;

import code.frontend.libs.katlaf.buttons.ButtonFoundation;
import javafx.scene.input.MouseEvent;

public abstract class SelectionChild extends ButtonFoundation {
    private final SelectionCollection<? extends SelectionChild> parent;
    public SelectionChild(final SelectionCollection<? extends SelectionChild> parent) {
        this.parent = parent;
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    /**
     * Since a child cannot access other children in the same list, calls which affect
     * other children must be forwarded to the parent. The MouseEvent is consumed
     * at the end of this method.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public final void onMousePressed(final MouseEvent event) {
        if (event.isShiftDown()) {
            parent.shiftSelected(this);
        } else if (event.isMetaDown()) {
            parent.metaSelected(this);
        } else if (parent.getNumberOfSelected() == 0 || !this.isToggled()) {
            parent.selected(this);
        }
        this.requestFocus();
        event.consume();
    }
}
