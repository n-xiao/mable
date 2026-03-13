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

package code.frontend.capabilities.legends;

import code.backend.data.Countdown;
import code.backend.data.Legend;
import code.frontend.capabilities.countdowns.CountdownList;
import code.frontend.libs.katlaf.FontHandler;
import code.frontend.libs.katlaf.FontHandler.DedicatedFont;
import code.frontend.libs.katlaf.buttons.ButtonFoundation;
import code.frontend.libs.katlaf.faces.BorderLabelFace;
import code.frontend.libs.katlaf.graphics.LabelledBorderedRegion;
import code.frontend.libs.katlaf.graphics.MableBorder;
import code.frontend.libs.katlaf.popup.Popup;
import code.frontend.libs.katlaf.ricing.Colour;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import code.frontend.libs.katlaf.tables.SimpleTable;
import java.util.ArrayList;
import java.util.Set;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public final class LegendTable extends StackPane {
    private final SimpleTable table;
    private final ArrayList<LegendTableMember> members;
    private final Uncategorised uncategorised;
    private final CountdownList list;
    private boolean populated;

    public LegendTable(final CountdownList countdownList) {
        this.table = new SimpleTable();
        this.table.setAlignment(Pos.CENTER);
        StackPane.setMargin(this.table, new Insets(4, 10, 4, 10));

        final LabelledBorderedRegion region = new LabelledBorderedRegion(
            new MableBorder(1.5, 0.2, 0.2), "Legend", RiceHandler.getColour("night"));
        region.setColour(RiceHandler.getColour("lightgrey"));
        region.setViewOrder(-1);
        region.setOpacity(0.5);
        region.setMouseTransparent(true);

        StackPane.setMargin(this.table, new Insets(8));
        this.getChildren().addAll(region, this.table);

        this.members = new ArrayList<LegendTableMember>();
        this.uncategorised = new Uncategorised();
        this.list = countdownList;
        this.populated = false;
    }

    /*


     PRIVATE API
    -------------------------------------------------------------------------------------*/

    private void refreshIndexes() {
        this.members.forEach(member -> member.getLegend().setIndex(this.members.indexOf(member)));
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    public void populate(final Set<Legend> legends, final Set<Countdown> countdowns) {
        if (!this.populated) {
            final CountdownToLegendDragStopper stopper =
                new CountdownToLegendDragStopper(this.uncategorised);
            stopper.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            stopper.setOpacity(0);
            this.uncategorised.getChildren().addLast(stopper);
            this.table.getChildren().addFirst(this.uncategorised);
            this.table.getChildren().addLast(new LegendCreateButton());

            this.populated = true;

            legends.forEach(this::addMember);
            this.organiseCountdowns(countdowns);
        }
    }

    public void organiseCountdowns(final Set<Countdown> countdowns) {
        countdowns.forEach(countdown -> {
            if (!countdown.isInLegend()) {
                countdown.moveToLegend(this.uncategorised.getLegend());
            }
        });
        this.list.colourCountdowns();
    }

    public void addMember(final Legend legend) {
        if (!this.populated)
            throw new IllegalStateException("You must populate the LegendTable first!");

        final LegendTableMember member = new LegendTableMember(legend, this, this.list);
        this.members.add(member);
        this.table.getChildren().add(this.table.getChildren().size() - 1, member);
        this.list.colourCountdowns();
        /*
         * drag drop stuff
         */
        final CountdownToLegendDragStopper stopper = new CountdownToLegendDragStopper(member);
        stopper.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        stopper.setOpacity(0);
        member.getChildren().addLast(stopper);
    }

    public void removeMember(final Legend legend) {
        LegendTableMember memberToDelete = null;
        for (LegendTableMember member : members) {
            if (member.getLegend().equals(legend))
                memberToDelete = member;
        }
        this.members.remove(memberToDelete);
        this.table.getChildren().remove(memberToDelete);
        this.refreshIndexes();
    }

    public int getNextIndex() {
        return this.members.size();
    }

    /*


     COMPOSITIONS
    -------------------------------------------------------------------------------------*/

    private class Uncategorised extends LegendTableMember {
        Uncategorised() {
            final Legend legend = new Legend("Uncategorised");
            legend.setColour(new Colour("white"));
            super(legend, LegendTable.this, LegendTable.this.list);
            getDeletePane().setVisible(false);
        }
    }

    private class LegendCreateButton extends ButtonFoundation {
        LegendCreateButton() {
            this.setMinSize(LegendTableMember.HEIGHT, LegendTableMember.HEIGHT);
            this.setMaxSize(LegendTableMember.HEIGHT, LegendTableMember.HEIGHT);
            this.setCursor(Cursor.HAND);
            final BorderLabelFace face = new BorderLabelFace(new MableBorder(1.5, 0.1, 0.5));
            face.setMouseTransparent(true);
            face.setFont(FontHandler.getDedicated(DedicatedFont.SYMBOL));
            face.setText("+");
            this.getChildren().add(face);
        }

        @Override
        public void onMousePressed(MouseEvent event) {
            Popup.spawn(new LegendCreatorPopup(LegendTable.this));
        }
    }
}
