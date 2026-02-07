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

import code.frontend.libs.katlaf.inputfields.InputField;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import java.util.ArrayList;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;

public class SimpleTable extends Region {
    private final ArrayList<SimpleTableMember> members;

    public SimpleTable() {
        this.members = new ArrayList<SimpleTableMember>();
        this.table = new Table();
        this.table.prefWidthProperty().bind(this.widthProperty());
        this.table.prefHeightProperty().bind(this.heightProperty());
        this.getChildren().add(this.table);
    }

    /*


     BEHAVIOUR
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

    /*


     PROTECTED API
    -------------------------------------------------------------------------------------*/
    private final Table table;

    protected void setHgap(final double hgap) {
        table.contents.setHgap(hgap);
        table.requestAlign();
    }

    protected void setVgap(final double vgap) {
        table.contents.setVgap(vgap);
        table.requestAlign();
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
    public void addMember(final SimpleTableMember tableMember) {
        members.add(tableMember);

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
            event.consume();
        });

        members.sort(null);

        table.contents.getChildren().add(tableMember);
        table.requestAlign();
    }

    public void removeMember(SimpleTableMember tableMember) {
        members.remove(tableMember);
        table.contents.getChildren().remove(tableMember);
        table.requestAlign();
    }

    /*


     COMPOSITIONS
    -------------------------------------------------------------------------------------*/

    private final class Table extends ScrollPane {
        final Contents contents;
        Table() {
            this.contents = new Contents();
            this.setContent(this.contents);
            this.setBackground(null);
            this.setStyle("-fx-background: transparent;");
            this.setFitToWidth(true);
            this.setHbarPolicy(ScrollBarPolicy.NEVER);
            this.setVbarPolicy(ScrollBarPolicy.NEVER); // TODO: implement custom scrollbar later
            this.widthProperty().addListener((observable, oldValue, newValue) -> requestAlign());
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
            final int widths = (int) SimpleTable.this.getMembers().getFirst().getWidth();
            final int numMembers = SimpleTable.this.getMembers().size();
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


         SUB-COMPOSITIONS
        -------------------------------------------------------------------------------------*/

        class Contents extends FlowPane {
            Contents() {
                this.prefWrapLengthProperty().bind(Table.this.widthProperty());
                this.minHeightProperty().bind(Table.this.heightProperty().add(-2));
                this.setMaxHeight(Double.MAX_VALUE);
                this.setMaxWidth(Double.MAX_VALUE);
                this.setAlignment(Pos.TOP_CENTER);
                this.setBackground(RiceHandler.createBG(RiceHandler.getColour("night"), 0, 0));
                this.setOnMousePressed(event -> {
                    SimpleTable.this.deselectAllMembers();
                    InputField.escapeAllInputs(); // fixes stupid inputs trapping cursors
                    event.consume();
                });
            }
        }

        class FakeMember extends Region {
            FakeMember(final double width) {
                this.setMinSize(width, 1);
                this.setMaxSize(width, 1);
                this.setVisible(false);
            }
        }
    }
}
