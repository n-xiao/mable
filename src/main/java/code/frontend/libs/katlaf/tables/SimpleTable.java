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

import code.frontend.libs.katlaf.ricing.RiceHandler;
import java.util.ArrayList;
import javafx.geometry.Pos;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;

public class SimpleTable extends FlowPane {
    private final ArrayList<SimpleTableMember> members;
    public SimpleTable() {
        this.members = new ArrayList<SimpleTableMember>();
        this.setMaxHeight(Double.MAX_VALUE);
        this.setMaxWidth(Double.MAX_VALUE);
        this.setAlignment(Pos.TOP_CENTER);
        this.setBackground(RiceHandler.createBG(RiceHandler.getColour("night"), 0, 0));
        this.setOnMousePressed(event -> {
            deselectAllMembers();
            event.consume();
        });
    }

    /*


     PRIVATE API
    -------------------------------------------------------------------------------------*/

    /**
     * Deselects all members of this table.
     */
    private void deselectAllMembers() {
        members.forEach(member -> member.setSelected(false));
    }

    private SimpleTableMember lastSelectedMember;
    /**
     * Called when user selects a {@link SimpleTableMember member} while
     * holding their shift key. This selects all members in between the
     * last selected member and the (caller) member that was just shift-clicked on.
     */
    private void shiftSelectMember(final SimpleTableMember tableMember) {
        // error & edge case handlings
        if (members.isEmpty()) {
            throw new IllegalStateException("shift selected on an empty table?!");
        } else if (lastSelectedMember == null || !lastSelectedMember.isSelected()
            || tableMember.equals(lastSelectedMember) && lastSelectedMember.isSelected()) {
            deselectAllMembers();
            selectMember(tableMember);
            return;
        }

        selectMember(tableMember);
        boolean selecting = false;
        for (SimpleTableMember member : members) {
            if (member.equals(tableMember) || member.equals(lastSelectedMember)) {
                selecting = !selecting;
            } else if (selecting) {
                selectMember(member);
            }
        }
    }

    /**
     * Sets a given member as selected and stores a reference to it
     * as the last selected member.
     */
    private void selectMember(final SimpleTableMember tableMember) {
        tableMember.setSelected(true);
        lastSelectedMember = tableMember;
    }

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
    void requestAlign() {
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


     PROTECTED API
    -------------------------------------------------------------------------------------*/

    protected ArrayList<SimpleTableMember> getSelectedMembers() {
        final ArrayList<SimpleTableMember> selected = new ArrayList<SimpleTableMember>();
        members.forEach(member -> {
            if (member.isSelected())
                selected.add(member);
        });
        return selected;
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    /**
     * Adds a {@link SimpleTableMember member} to this table,
     * and attaches mouse listeners to it.
     *
     * This method will realign the members by
     * calling `requestAlign()`
     */
    public void addMember(final SimpleTableMember tableMember) {
        members.add(tableMember);

        tableMember.setOnMousePressed((event) -> {
            if (event.getButton().equals(MouseButton.SECONDARY)) {
                if (!tableMember.isSelected()) {
                    deselectAllMembers();
                    selectMember(tableMember);
                }
                tableMember.onRightClicked(getSelectedMembers());
            } else if (event.isShiftDown()) {
                shiftSelectMember(tableMember);
            } else if (event.isMetaDown()) {
                selectMember(tableMember);
            } else {
                deselectAllMembers();
                selectMember(tableMember);
            }
            event.consume();
        });

        members.sort(null);

        this.getChildren().add(tableMember);
        this.requestAlign();
    }

    /**
     * Removes the provided member from this table.
     * This method will realign the members by
     * calling `requestAlign()`
     */
    public void removeMember(SimpleTableMember tableMember) {
        members.remove(tableMember);
        this.getChildren().remove(tableMember);
        this.requestAlign();
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
