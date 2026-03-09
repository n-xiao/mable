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

import code.frontend.libs.katlaf.collections.SelectionCollection;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;

public class SimpleTable extends FlowPane {
    private static final double V_GAP = 8;
    private static final double H_GAP = 5;
    private final ArrayList<SimpleTableMember> members;
    private final SelectionCollection<SimpleTableMember> selcol;

    public SimpleTable() {
        this.members = new ArrayList<SimpleTableMember>();
        this.selcol = new SelectionCollection<SimpleTableMember>(this.members);
        this.setVgap(V_GAP);
        this.setHgap(H_GAP);
        this.setMaxHeight(Double.MAX_VALUE);
        this.setMaxWidth(Double.MAX_VALUE);
        this.setAlignment(Pos.TOP_CENTER);
        this.setBackground(RiceHandler.createBG(RiceHandler.getColour("night"), 0, 0));
        this.setOnMousePressed(event -> { // click anywhere to deselect
            this.selcol.deselectAll();
            event.consume();
        });
    }

    /*


     PROTECTED API
    -------------------------------------------------------------------------------------*/

    /**
     * This allows for left to right listing of members; by
     * adding invisible Regions that act as "spacers" or "paddings", an incomplete
     * row of a FlowPane will be aligned to the left when all children of the
     * FlowPane are centered. It is attached to a listener for when the window
     * is resized.
     *
     * At the time of writing, I can't seem to figure out how to reliably get
     * the extra number of paddings needed. Hence, one extra padding is added
     * and the height of the paddings are locked to 1 pixel, so it should not
     * affect the scrolling experience of the user. (as in, the invisible
     * paddings will probably not let the user scroll down to nothing)
     */
    protected void requestAlign() {
        this.getChildren().removeIf(child -> child instanceof FakeMember);

        final int totalWidth = (int) this.getBoundsInParent().getWidth();
        final int widths = (int) members.getFirst().getWidth();
        final int numMembers = members.size();
        final int columns = (int) (totalWidth / widths);

        if (columns == 0)
            return;

        final int numLastRow = (int) (numMembers % columns); // finds num of members on last row
        final int remainder = columns - numLastRow + 1;

        for (int i = 0; i < remainder; i++) {
            final FakeMember fakeMember = new FakeMember(widths);
            this.getChildren().addLast(fakeMember);
        }
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    /**
     * Adds a {@link SimpleTableMember member} to this table,
     * and attaches mouse listeners to it.
     */
    public void addMember(final SimpleTableMember tableMember) {
        this.selcol.deselectAll();
        this.members.add(tableMember);
        this.members.sort(null);
        this.getChildren().add(this.members.indexOf(tableMember), tableMember);
    }

    public void addMembers(final List<SimpleTableMember> tableMembers) {
        this.selcol.deselectAll();
        for (SimpleTableMember simpleTableMember : tableMembers) {
            this.members.add(simpleTableMember);
        }
        this.members.sort(null);
        this.getChildren().clear();
        this.getChildren().addAll(this.members);
    }

    /**
     * Removes the provided member from this table.
     */
    public void removeMember(final SimpleTableMember tableMember) {
        this.members.remove(tableMember);
        this.getChildren().remove(tableMember);
    }

    public void removeMembers(final List<SimpleTableMember> tableMembers) {
        this.members.removeAll(tableMembers);
        this.getChildren().clear();
        this.getChildren().addAll(this.members);
    }

    public SelectionCollection<SimpleTableMember> getSelector() {
        return this.selcol;
    }

    /*


     COMPOSITIONS
    -------------------------------------------------------------------------------------*/

    class FakeMember extends Region {
        FakeMember(final double width) {
            this.setMinSize(width, 1);
            this.setMaxSize(width, 1);
            this.setVisible(false);
        }
    }
}
