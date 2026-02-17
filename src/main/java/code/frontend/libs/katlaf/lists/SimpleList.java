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

import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

/**
 * An implementation of a list. The appearance of members are unknown to the list,
 * since the list only handles add and remove operations as well as some animations.
 *
 * @since v3.0.0-beta
 * @see SimpleListMember
 */
public class SimpleList extends VBox {
    private final ArrayList<SimpleListMember> members;
    private double spacing;
    private SimpleListMember pivot; // this is the last selected member

    /**
     * Creates a new instance of an empty SimpleList.
     */
    public SimpleList() {
        this.setBackground(null);
        this.setMouseTransparent(true);
        this.setFillWidth(true);
        /*
         * a list shall always be ordered based on a listable's priority
         * (priority being an abstract concept). lower priority comes first.
         * easier to implement when counting down days
         */
        this.members = new ArrayList<SimpleListMember>();
        this.spacing = 0;
        this.pivot = null;
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
        /*
         * can't do this with null pivot
         * OR can't do this if pivot is not even toggled
         * OR can't do this if user is dum dum and shift selects THE SAME DARN THING??
         */
        if (this.pivot == null || !this.pivot.isToggled() || member.equals(this.pivot)) {
            selected(member);
            return;
        }

        boolean selecting = false;
        for (SimpleListMember mem : members) {
            if (mem.equals(member) || mem.equals(this.pivot))
                selecting = !selecting;
            if (selecting && !mem.isToggled()) {
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
        this.pivot = member;
    }

    /**
     * Given that the member that has been clicked on (and selected) has already
     * been toggled (selected), all this method does is deselect all other members.
     *
     * @param member    the member that is to be selected
     */
    final void selected(final SimpleListMember member) {
        deselectAllExcept(member);
        this.pivot = member;
    }

    /**
     * Deselects every single member of this list except for the member provided in the
     * parameter. That is ignored. If the member is not toggled already, then this method
     * will leave the list with all members deselected (untoggled).
     *
     * @param member    the member that should not be untoggled
     */
    private void deselectAllExcept(final SimpleListMember member) {
        members.forEach(m -> {
            if (!m.equals(member))
                m.setToggle(false);
        });
    }

    /*


     PROTECTED API
    -------------------------------------------------------------------------------------*/

    protected final ArrayList<SimpleListMember> getMembers() {
        return this.members;
    }

    protected final void syncMembers() {
        this.getChildren().clear();
        this.members.forEach(member -> { this.getChildren().add(member); });
    }

    protected void setVspacing(final double spacing) {
        final ObservableList<Node> children = this.getChildren();
        if (children.isEmpty())
            return;
        for (Node node : children) {
            if (!children.getFirst().equals(node)) {
                VBox.setMargin(node, new Insets(spacing, 0, 0, 0));
            }
        }
        this.spacing = spacing;
    }

    protected void setVspacing() {
        setVspacing(this.spacing);
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    /**
     * This method is called during runtime, usually when a user adds a new member to this
     * list.
     */
    public void addMember(final SimpleListMember member) {
        this.members.add(member);
        this.members.sort(null);
        if (this.members.getLast().equals(member)) {
            this.getChildren().addLast(member);
        } else {
            this.getChildren().add(this.members.indexOf(member), member);
        }
        setVspacing();
    }

    public void addMembers(final List<SimpleListMember> membersToAdd) {
        for (SimpleListMember simpleListMember : membersToAdd) {
            addMember(simpleListMember);
        }
    }

    public void removeMember(final SimpleListMember member) {
        this.members.remove(member);
        this.getChildren().removeIf(
            m -> (m instanceof SimpleListMember currentMember && currentMember.equals(member)));
    }

    public void removeMembers(final List<SimpleListMember> membersToRemove) {
        for (SimpleListMember simpleListMember : membersToRemove) {
            removeMember(simpleListMember);
        }
    }
}
