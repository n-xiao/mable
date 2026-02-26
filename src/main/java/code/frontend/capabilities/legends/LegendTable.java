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

import code.backend.data.Legend;
import code.frontend.libs.katlaf.graphics.LabelledBorderedRegion;
import code.frontend.libs.katlaf.graphics.MableBorder;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import code.frontend.libs.katlaf.tables.SimpleTable;
import code.frontend.libs.katlaf.tables.SimpleTableMember;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;

public final class LegendTable extends StackPane {
    private final SimpleTable table;
    private final ArrayList<LegendTableMember> members;
    private boolean populated;

    public LegendTable() {
        this.table = new SimpleTable();
        StackPane.setMargin(this.table, new Insets(4, 10, 4, 10));

        final LabelledBorderedRegion region = new LabelledBorderedRegion(
            new MableBorder(1.5, 0.2, 0.2), "Legend", RiceHandler.getColour("night"));
        region.setColour(RiceHandler.getColour("lightgrey"));
        region.setMouseTransparent(true);

        this.getChildren().addAll(region, this.table);

        this.populated = false;
        this.members = new ArrayList<LegendTableMember>();
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    public void populate(final List<Legend> legends) {
        if (!this.populated) {
            legends.forEach(this::addMember);
        }

        this.populated = true;
    }

    public void addMember(final Legend legend) {
        final LegendTableMember member = new LegendTableMember(legend);
        this.members.add(member);
        this.table.addMember(member);
    }

    public void removeMember(final Legend legend) {
        LegendTableMember memberToDelete = null;
        for (LegendTableMember member : members) {
            if (member.legend.equals(legend)) {
                memberToDelete = member;
                this.members.remove(member);
            }
        }
        this.table.removeMember(memberToDelete);
    }

    /*


     COMPOSITIONS
    -------------------------------------------------------------------------------------*/

    private class LegendTableMember
        extends SimpleTableMember implements Comparable<LegendTableMember> {
        private final Legend legend;

        LegendTableMember(final Legend legend) {
            super(table.getSelector());
            this.legend = legend;
        }

        @Override
        public int compareTo(LegendTableMember o) {
            return this.legend.compareTo(o.legend);
        }
    }
}
