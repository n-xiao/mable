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

package code.frontend.capabilities.countdown.components;

import code.backend.data.Countdown;
import code.frontend.libs.katlaf.dragndrop.DragStartRegion;
import code.frontend.libs.katlaf.tables.SimpleTableMember;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;

public class CountdownControl extends ScrollPane {
    public CountdownControl() {
        this.setStyle("-fx-background: transparent;");
    }

    /*


     BEHAVIOUR
    -------------------------------------------------------------------------------------*/

    /*


     COMPOSITIONS
    -------------------------------------------------------------------------------------*/

    private class Member extends SimpleTableMember {
        final Countdown countdown;
        Member(final Countdown countdown) {
            super(CountdownPane.WIDTH, CountdownPane.HEIGHT);
            this.countdown = countdown;
        }

        @Override
        public int compareTo(SimpleTableMember o) {
            if (o instanceof Member) {
                final LocalDate now = LocalDate.now();
                final Member other = (Member) o;
                return this.countdown.daysUntilDue(now) - other.countdown.daysUntilDue(now);
            }
            throw new IllegalArgumentException(
                "Cannot compare something that isn't a CountdownControl.Member");
        }

        @Override
        protected void onRightClicked(ArrayList<SimpleTableMember> selectedMembers) {
            // TODO Auto-generated method stub
        }
    }

    private class DragDetector extends DragStartRegion<Countdown> {
        final CountdownPane parent;

        DragDetector(final CountdownPane parent) {
            this.parent = parent;
            this.prefWidthProperty().bind(parent.widthProperty());
            this.prefHeightProperty().bind(parent.heightProperty());
            this.setViewOrder(this.parent.getViewOrder() - 1);
            parent.getChildren().addLast(this);
        }

        @Override
        protected Countdown getData() {
            return this.parent.getCountdown();
        }

        @Override
        protected Region getRepresentation() {
            // TODO this thing properly later
            return this.parent;
        }

        @Override
        protected void cleanupOnDragEnd() {
            this.parent.setOpacity(1);
            this.parent.setMouseTransparent(false);
        }

        @Override
        protected void onDragStart() {
            this.parent.setOpacity(0.6);
            this.parent.setMouseTransparent(true);
        }
    }
}
