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

import code.frontend.libs.katlaf.buttons.SelectionButton;
import code.frontend.libs.katlaf.graphics.MableBorder;
import javafx.scene.input.MouseEvent;

public abstract class SimpleListMember extends SelectionButton implements Listable {
    private final SimpleList parent; // keeps reference of parent

    /*


     CONSTRUCTORS
    -------------------------------------------------------------------------------------*/

    /**
     * Creates a new instance of a SimpleListMember without a border.
     * Technically, a border is still created and added but it is invisible.
     *
     * @param parent    the SimpleList that this instance is part of. A reference
     *                  needs to be held by each SimpleListMember instance in order
     *                  to call methods from it.
     */
    public SimpleListMember(final SimpleList parent) {
        final MableBorder dummy = new MableBorder(1, 0.1, 0.1);
        dummy.setVisible(false);
        this(dummy, parent); // just to satisfy the super class haha
    }

    /**
     * Creates a new instance of a SimpleListMember with a border.
     *
     * @param border    the MableBorder to use
     * @param parent    the SimpleList that this instance is part of. A reference
     *                  needs to be held by each SimpleListMember instance in order
     *                  to call methods from it.
     * @see MableBorder
     * @see SelectionButton
     */
    public SimpleListMember(final MableBorder border, final SimpleList parent) {
        super(border);
        this.parent = parent;
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    /**
     * Since a member cannot access other members in the same list, calls which affect
     * other members must be forwarded to the parent.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public final void onMousePressed(final MouseEvent event) {
        if (event.isShiftDown()) {
            parent.shiftSelected(this);
        } else if (event.isMetaDown()) {
            parent.metaSelected(this);
        } else {
            parent.selected(this);
        }
        super.onMousePressed(event);
    }
}
