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
import code.backend.utils.CountdownHandler;
import code.frontend.libs.katlaf.dividers.HorizontalDivider;
import code.frontend.libs.katlaf.dragndrop.DragStartRegion;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import code.frontend.libs.katlaf.tables.SimpleTable;
import code.frontend.libs.katlaf.tables.SimpleTableMember;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class CountdownViewer extends ScrollPane {
    private final VBox container;
    private final SimpleTable table;
    private final SimpleTable completedTable;

    public CountdownViewer() {
        this.setStyle("-fx-background: transparent;");
        this.setFitToWidth(true);
        this.container = new VBox();
        this.container.setBackground(RiceHandler.createBG(RiceHandler.getColour("night"), 12, 0));

        final LocalDate now = LocalDate.now();

        this.table = new SimpleTable();
        CountdownHandler.getIncomplete().forEach(
            countdown -> { this.table.addMember(new Member(new CountdownPane(countdown, now))); });

        this.completedTable = new SimpleTable();
        CountdownHandler.getComplete().forEach(countdown -> {
            this.completedTable.addMember(new Member(new CompletedPane(countdown, now)));
        });

        final HorizontalDivider divider =
            new HorizontalDivider(2, "Completed", RiceHandler.getColour("dullgrey"));
        VBox.setMargin(divider, new Insets(5, 0, 5, 0));

        this.container.getChildren().addAll(this.table, divider, this.completedTable);
        this.getChildren().add(this.container);
    }

    /*


     COMPOSITIONS
    -------------------------------------------------------------------------------------*/

    private class Member extends SimpleTableMember {
        final CountdownPane countdownPane;
        Member(final CountdownPane countdownPane) {
            super(CountdownPane.WIDTH, CountdownPane.HEIGHT);
            this.countdownPane = countdownPane;
            this.getChildren().addAll(
                this.countdownPane, new DragDetector(this, countdownPane.getCountdown()));
        }

        @Override
        protected void onSelected() {
            this.countdownPane.applySelectStyle();
        }

        @Override
        protected void onDeselected() {
            this.countdownPane.applyDeselectStyle(true);
        }

        @Override
        public int compareTo(SimpleTableMember o) {
            if (o instanceof Member other) {
                final LocalDate now = LocalDate.now();
                return this.countdownPane.getCountdown().getDaysUntilDue(now)
                    - other.countdownPane.getCountdown().getDaysUntilDue(now);
            } else {
                throw new IllegalArgumentException(
                    "Cannot compare something that isn't a CountdownControl.Member");
            }
        }

        @Override
        protected void onRightClicked(ArrayList<SimpleTableMember> selectedMembers) {
            // TODO Auto-generated method stub
        }
    }

    private class DragDetector extends DragStartRegion<Countdown> {
        final Member parent;
        final Countdown countdown;

        DragDetector(final Member parent, final Countdown countdown) {
            this.parent = parent;
            this.countdown = countdown;
            this.prefWidthProperty().bind(parent.widthProperty());
            this.prefHeightProperty().bind(parent.heightProperty());
            this.setViewOrder(this.parent.getViewOrder() - 1);
        }

        @Override
        protected Countdown getData() {
            return this.countdown;
        }

        @Override
        protected Region getRepresentation() {
            // TODO THIS THING LATER
            return new Region();
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
