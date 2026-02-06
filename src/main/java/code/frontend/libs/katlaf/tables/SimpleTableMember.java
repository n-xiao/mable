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

import java.util.ArrayList;
import javafx.scene.layout.StackPane;

public abstract class SimpleTableMember extends StackPane implements Comparable<SimpleTableMember> {
    private boolean selected;

    public SimpleTableMember(final double width, final double height) {
        this.setBackground(null);
        this.setMinWidth(width);
        this.setMaxWidth(width);
        this.setMinHeight(height);
        this.setMaxHeight(height);
        this.selected = false;
    }

    /*


     BEHAVIOUR
    -------------------------------------------------------------------------------------*/

    void setSelected(boolean selected) {
        this.selected = selected;
        if (selected) {
            onSelected();
        } else {
            onDeselected();
        }
    }

    /*


     PROTECTED API
    -------------------------------------------------------------------------------------*/

    /**
     * This method is called when this {@link SimpleTableMember}
     * instance is selected. Its default implementation does nothing.
     */
    protected void onSelected() {}

    /**
     * This method is called when this {@link SimpleTableMember}
     * instance is deselected. Its default implementation does nothing.
     */
    protected void onDeselected() {}

    /**
     * This method is called when this instance is clicked on
     * with a right mouse button.
     *
     * An ArrayList of selected members from the {@link SimpleTable}
     * are provided through the parameter.
     */
    protected abstract void onRightClicked(final ArrayList<SimpleTableMember> selectedMembers);

    /**
     * This method is called when this instance receives a drag
     * start. This method may not need to do anything. An ArrayList
     * of selected members from the {@link SimpleTable} are provided
     * through the parameter.
     */
    protected abstract void onDragStart(final ArrayList<SimpleTableMember> selectedMembers);

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    public boolean isSelected() {
        return this.selected;
    }
}
