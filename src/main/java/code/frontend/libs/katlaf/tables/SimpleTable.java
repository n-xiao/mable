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
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;

public class SimpleTable extends Region {
    private ArrayList<SimpleTableMember> members;

    public SimpleTable() {
        // TODO
        this.members = new ArrayList<SimpleTableMember>();
    }

    /*


     BEHAVIOUR
    -------------------------------------------------------------------------------------*/

    private void deselectAllMembers() {
        members.forEach(member -> member.setSelected(false));
    }

    private SimpleTableMember lastSelectedMember;
    /**
     * Called when user selects a {@link SimpleTableMember member} while
     * holding their shift key.
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

    private void selectMember(final SimpleTableMember tableMember) {
        tableMember.setSelected(true);
        lastSelectedMember = tableMember;
    }

    /*


     PROTECTED API
    -------------------------------------------------------------------------------------*/

    protected void setHgap(double hgap) {
        // TODO
    }

    protected void setVgap(double vgap) {
        // TODO
    }

    protected ArrayList<SimpleTableMember> getMembers() {
        return this.members;
    }

    protected ArrayList<SimpleTableMember> getSelectedMembers() {
        final ArrayList<SimpleTableMember> selectedMembers = new ArrayList<SimpleTableMember>();
        this.members.forEach(member -> {
            if (member.isSelected())
                selectedMembers.add(member);
        });
        return selectedMembers;
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    /**
     * Adds a {@link SimpleTableMember member} to this table,
     * and attaches mouse listeners to it.
     */
    public void addMember(SimpleTableMember tableMember) {
        members.add(tableMember);

        // TODO add to table UI

        tableMember.setOnMousePressed((event) -> {
            if (event.getButton().equals(MouseButton.SECONDARY)) {
                selectMember(tableMember);
                tableMember.onRightClicked(getSelectedMembers());
            } else if (event.isShiftDown()) {
                shiftSelectMember(tableMember);
            } else if (event.isMetaDown()) {
                selectMember(tableMember);
            } else {
                deselectAllMembers();
                selectMember(tableMember);
            }
        });

        tableMember.setOnDragDetected(event -> tableMember.onDragStart(getSelectedMembers()));

        members.sort(null);
    }

    public void removeMember(SimpleTableMember tableMember) {
        members.remove(tableMember);

        // TODO remove from table UI
    }

    /*


     COMPOSITIONS
    -------------------------------------------------------------------------------------*/

    private class Table extends ScrollPane {
        final Contents contents;
        Table() {
            this.contents = new Contents();
            this.setContent(this.contents);
            this.setBackground(null);
            this.setStyle("-fx-background: transparent;");
            this.setFitToWidth(true);
            this.setHbarPolicy(ScrollBarPolicy.NEVER);
            this.setVbarPolicy(ScrollBarPolicy.NEVER); // TODO: implement custom scrollbar later
        }

        private class Contents extends FlowPane {
            Contents() {
                this.prefWrapLengthProperty().bind(Table.this.widthProperty());
                this.minHeightProperty().bind(Table.this.heightProperty().add(-2));
                this.setMaxHeight(Double.MAX_VALUE);
            }
        }
    }
}
