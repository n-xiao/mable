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

import code.frontend.libs.katlaf.collections.SelectionCollection;
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
    private final SelectionCollection<SimpleListMember> selcol;
    private double spacing;

    /**
     * Creates a new instance of an empty SimpleList.
     */
    public SimpleList() {
        this.setBackground(null);
        this.setFillWidth(true);
        /*
         * a list shall always be ordered based on a listable's priority
         * (priority being an abstract concept). lower priority comes first.
         * easier to implement when counting down days
         */
        this.members = new ArrayList<SimpleListMember>();
        this.selcol = new SelectionCollection<SimpleListMember>(this.members);
        this.spacing = 0;
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
        if (member instanceof Comparable) {
            this.members.sort(null);
        }
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

    /**
     * When creating SimpleListMember instances for this list, the SelectionCollection (parent)
     * needs to be obtained.
     *
     * @returns the SelectionCollection contained within this SimpleList
     * @see SelectionCollection
     */
    public SelectionCollection<SimpleListMember> getSelector() {
        return selcol;
    }
}
