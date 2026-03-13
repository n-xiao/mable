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

import code.backend.data.wrappers.CountdownPacket;
import code.frontend.libs.katlaf.dragndrop.DragStopRegion;
import javafx.scene.input.MouseDragEvent;

final class CountdownToLegendDragStopper extends DragStopRegion<CountdownPacket> {
    private final LegendTableMember member;

    CountdownToLegendDragStopper(final LegendTableMember member) {
        this.member = member;
    }

    /*


     PROTECTED API
    -------------------------------------------------------------------------------------*/

    @Override
    protected void onDragStop(MouseDragEvent event) {
        /*
         * remember that deleted countdowns shouldn't be draggable
         * we will assume no countdown is deleted here
         *
         * the change the colours of the CountdownListMember is handled
         * via update
         */

        final CountdownPacket packet = this.retrieveData();
        if (packet == null)
            return;

        packet.getCountdowns().forEach(
            countdown -> countdown.moveToLegend(this.member.getLegend()));
        this.member.onDragExited();
    }

    @Override
    protected void onDragRegionEnter(MouseDragEvent event) {
        if (this.isExpecting())
            this.member.onDragEntered();
    }

    @Override
    protected void onDragRegionExit(MouseDragEvent event) {
        if (this.isExpecting())
            this.member.onDragExited();
    }

    @Override
    protected Class<CountdownPacket> getExpectedType() {
        return CountdownPacket.class;
    }
}
