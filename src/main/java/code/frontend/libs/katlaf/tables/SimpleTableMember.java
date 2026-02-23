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

package code.frontend.libs.katlaf.tables;

import code.frontend.libs.katlaf.collections.SelectionChild;
import code.frontend.libs.katlaf.collections.SelectionCollection;
import javafx.scene.layout.StackPane;

/**
 * A SimpleTableMember has a fixed width and height. This class provides selection implementations
 * by extending SelectionChild.
 *
 * @see SelectionChild
 * @since v3.0.0-beta
 */
public abstract class SimpleTableMember
    extends SelectionChild implements Comparable<SimpleTableMember> {
    /**
     * Creates a new instance of this class. This constructor calls the hideFace() method
     * of the superclass, so as to provide implementations of this class the equivalent of
     * an empty StackPane.
     *
     * @param parent    the SelectionCollection that this SelectionChild instance belongs to
     * @param width     the width of this instance
     * @param height    the height of this instance
     * @see SelectionCollection
     * @see StackPane
     */
    public SimpleTableMember(final SelectionCollection<? extends SelectionChild> parent,
        final double width, final double height) {
        super(parent);
        this.setBackground(null);
        this.setMinWidth(width);
        this.setMaxWidth(width);
        this.setMinHeight(height);
        this.setMaxHeight(height);
    }
}
