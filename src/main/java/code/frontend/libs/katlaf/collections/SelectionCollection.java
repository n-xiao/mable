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

import code.frontend.libs.katlaf.buttons.SelectionButton;
import java.util.List;

/**
 * This class is a wrapper for a  collection of SelectionButton instances which support
 * actions like shift-click, meta-click, deselect all, select all and
 * click to select.
 * <p>
 * It acts as a command and control "server" for its children, allowing the children
 * to interact with other children of the same collection.
 *
 * @see SelectionButton
 * @since v3.0.0-beta
 */
public class SelectionCollection<E extends SelectionChild> {
    private final List<E> children;
    private SelectionChild pivot;

    /**
     * Creates a new SelectionCollection instance, backed by the provided
     * List.
     *
     * @param children      the list that backs this instance
     */
    public SelectionCollection(final List<E> children) {
        this.children = children;
        this.pivot = null;
    }

    /*


     PRIVATE API
    -------------------------------------------------------------------------------------*/

    /**
     * A child should call this method when it detects
     * a shift select on it. If there are no other children that are currently
     * selected, then this method will just forward the call to the selected() method.
     * <p>
     * Otherwise, the last child that was selected before the calling of this method
     * will be used as a "pivot". All children inclusively between the pivot and the child which
     * issued this method call will be selected. All other children will be deselected.
     *
     * @param caller    the child that this method call originated from
     */

    final void shiftSelected(final SelectionChild caller) {
        /*
         * can't do this with null pivot
         * OR can't do this if pivot is not even toggled
         * OR can't do this if user is dum dum and shift selects THE SAME DARN THING??
         */
        if (this.pivot == null || !this.pivot.isToggled() || caller.equals(this.pivot)) {
            selected(caller);
            return;
        }

        boolean selecting = false;
        for (SelectionChild child : this.children) {
            if (child.equals(caller) || child.equals(this.pivot)) {
                selecting = !selecting;
                child.setToggle(true);
            } else if (selecting && !child.isToggled()) {
                child.setToggle(true);
            } else {
                child.setToggle(false);
            }
        }
    }

    /**
     * A child should call this method when it detects
     * a meta select on itself.
     *
     * @param caller    the child which incurred the user click
     */
    final void metaSelected(final SelectionChild caller) {
        if (caller.isToggled()) {
            caller.setToggle(false);
        } else {
            this.pivot = caller;
            caller.setToggle(true);
        }
    }

    /**
     * Given that the child that has been clicked on (and selected) has already
     * been toggled (selected), all this method does is deselect all other children.
     *
     * @param caller    the child that is to be selected
     */
    final void selected(final SelectionChild caller) {
        deselectAllExcept(caller);
        this.pivot = caller;
        caller.setToggle(true);
    }

    /**
     * Deselects every single child of this collection except for the child provided in the
     * parameter. That is ignored. If the child is not toggled already, then this method
     * will leave the collection with all children deselected (untoggled).
     *
     * @param child    the child that should not be untoggled
     */
    private void deselectAllExcept(final SelectionChild child) {
        this.children.forEach(c -> {
            if (!c.equals(child))
                c.setToggle(false);
        });
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    public void deselectAll() {
        this.children.forEach(child -> child.setToggle(false));
        this.pivot = null;
    }

    public int getNumberOfSelected() {
        int count = 0;
        for (SelectionChild child : this.children) {
            if (child.isToggled())
                count++;
        }
        return count;
    }
}
