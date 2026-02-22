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
import code.frontend.libs.katlaf.collections.SelectionChild;
import code.frontend.libs.katlaf.collections.SelectionCollection;
import code.frontend.libs.katlaf.graphics.MableBorder;

public abstract class SimpleListMember extends SelectionChild {
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
    public SimpleListMember(final SelectionCollection<? extends SelectionChild> parent) {
        super(parent);
        this.setMaxWidth(Double.MAX_VALUE);
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
    public SimpleListMember(
        final MableBorder border, final SelectionCollection<? extends SelectionChild> parent) {
        this(parent);
        border.bindSize(this.widthProperty(), this.heightProperty());
        this.getChildren().add(border);
    }
}
