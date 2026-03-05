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
import code.frontend.libs.katlaf.collections.SelectionCollection;
import code.frontend.libs.katlaf.graphics.LabelledBorderedRegion;
import code.frontend.libs.katlaf.graphics.MableBorder;
import code.frontend.libs.katlaf.popup.Popup;
import code.frontend.libs.katlaf.ricing.Colour;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import code.frontend.libs.katlaf.tables.SimpleTable;
import code.frontend.libs.katlaf.tables.SimpleTableMember;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public final class LegendTable extends StackPane {
    private final SimpleTable table;
    private final ArrayList<LegendTableMember> members;
    private final Uncategorised uncategorised;
    private final CountdownList list;
    private boolean populated;

    public LegendTable(final CountdownList countdownList) {
        this.table = new SimpleTable();
        StackPane.setMargin(this.table, new Insets(4, 10, 4, 10));

        final LabelledBorderedRegion region = new LabelledBorderedRegion(
            new MableBorder(1.5, 0.2, 0.2), "Legend", RiceHandler.getColour("night"));
        region.setColour(RiceHandler.getColour("lightgrey"));
        region.setMouseTransparent(true);

        this.getChildren().addAll(region, this.table);

        this.members = new ArrayList<LegendTableMember>();
        this.uncategorised = new Uncategorised();
        this.list = countdownList;
        this.populated = false;
    }

    /*


     PRIVATE API
    -------------------------------------------------------------------------------------*/

    SelectionCollection<SimpleTableMember> getSelector() {
        return this.table.getSelector();
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    public void populate(final List<Legend> legends, final List<Countdown> countdowns) {
        if (!this.populated) {
            legends.forEach(this::addMember);
            this.table.addMember(new LegendCreateButton());
            this.table.addMember(this.uncategorised);
            this.organiseCountdowns(countdowns);
        }

        this.populated = true;
    }

    public void organiseCountdowns(final List<Countdown> countdowns) {
        countdowns.forEach(countdown -> {
            if (!countdown.isInLegend()) {
                countdown.moveToLegend(this.uncategorised.getLegend());
            }
        });
        this.list.colourCountdowns();
    }

    public void addMember(final Legend legend) {
        final LegendTableMember member = new LegendTableMember(legend, this);
        this.members.add(member);
        this.table.addMember(member);
        this.list.colourCountdowns();
    }

    public void removeMember(final Legend legend) {
        LegendTableMember memberToDelete = null;
        for (LegendTableMember member : members) {
            if (member.getLegend().equals(legend))
                memberToDelete = member;
        }
        this.members.remove(memberToDelete);
        this.table.removeMember(memberToDelete);
    }

    /*


     COMPOSITIONS
    -------------------------------------------------------------------------------------*/

    private class Uncategorised extends LegendTableMember {
        Uncategorised() {
            final Legend legend = new Legend("Uncategorised");
            legend.setColour(new Colour(0, 0, 0, 1));
            super(legend, LegendTable.this);
            getDeletePane().setVisible(false);
        }

        @Override
        public int compareTo(LegendTableMember o) {
            return -1;
        }
    }

    private class LegendCreateButton
        extends SimpleTableMember implements Comparable<LegendTableMember> {
        private static final double WIDTH = 10;

        LegendCreateButton() {
            super(table.getSelector());
            this.setMaxSize(WIDTH, LegendTableMember.HEIGHT);
            this.setMinSize(WIDTH, LegendTableMember.HEIGHT);

            final Label label = new Label("+");
            label.setFont(FontHandler.getDedicated(DedicatedFont.SYMBOL));
            label.setTextFill(RiceHandler.getColour("white"));
            label.setMouseTransparent(true);

            final MableBorder border = new MableBorder(1.5, 0, 0.8);

            this.getChildren().addAll(label, border);
        }

        /**
         * This acts as a click detector.
         * <p>
         * {@inheritDoc}
         */
        @Override
        public void setToggle(boolean toggled) {
            if (toggled) {
                /*
                 * must be set false to not interfere with methods such as
                 * deselectAll()
                 */
                this.setToggle(false);
                Popup.spawn(new LegendCreatorPopup());
            }
        }

        @Override
        public int compareTo(LegendTableMember o) {
            return 1;
        }
    }
}
