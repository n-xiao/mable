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

import java.util.LinkedHashSet;
import java.util.Stack;
import javafx.scene.layout.VBox;

/**
 * An implementation of a list. The appearance of members are unknown to the list,
 * since the list only handles add and remove operations as well as some animations.
 *
 * @since v3.0.0-beta
 * @see SimpleListMember
 */
public class SimpleList extends VBox {
    private final LinkedHashSet<SimpleListMember> members;
    private final Stack<SimpleListMember> selected;
    private double spacing; // vertical spacing in between each member

    /**
     * Creates a new instance of an empty SimpleList.
     */
    public SimpleList() {
        this.setBackground(null);
        this.setMouseTransparent(true);
        this.members = new LinkedHashSet<SimpleListMember>();
        this.selected = new Stack<SimpleListMember>();
        this.spacing = 0;
    }

    /*


     PRIVATE API
    -------------------------------------------------------------------------------------*/

    /**
     * A SimpleListMember should call this method when it detects
     * a shift select on it. If there are no other members that are currently
     * selected, then this method will just forward the call to the selected() method.
     * <p>
     * Otherwise, the last member that was selected before the calling of this method
     * will be used as a "pivot". All members inclusively between the pivot and the member which
     * issued this method call will be selected. All other members will be deselected.
     *
     * @param member    the member that this method call originated from
     */

    final void shiftSelected(final SimpleListMember member) {
        if (this.selected.isEmpty()) {
            selected(member);
            return;
        }

        final SimpleListMember pivot = this.selected.peek();
        boolean selecting = false;
        for (SimpleListMember mem : members) {
            if (mem.equals(member) || mem.equals(pivot))
                selecting = !selecting;

            if (selecting && !selected.contains(mem)) {
                mem.setToggle(true);
                selected.push(mem);
            } else if (!selecting) {
                mem.setToggle(false);
                selected.remove(mem);
            }
        }
    }

    /**
     * A SimpleListMember should call this method when it detects
     * a meta select on itself. It will be pushed to the stack of selected
     * SimpleListMember instances of this SimpleList instance.
     *
     * @param member    the SimpleListMember which should be pushed to the stack.
     */
    final void metaSelected(final SimpleListMember member) {
        // TODO
    }

    /**
     * A SimpleListMember should call this method when it detects
     * a click on itself. The selected stack will be emptied before the member is added
     * to the stack.
     *
     * @param member    the member of this SimpleList instance which will become the only element
     *                  in the stack of this SimpleList instance
     */
    final void selected(final SimpleListMember member) {
        // TODO
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/
}
