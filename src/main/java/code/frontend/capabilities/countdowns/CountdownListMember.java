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

package code.frontend.capabilities.countdowns;

import code.backend.data.Countdown;
import code.frontend.libs.katlaf.FontHandler;
import code.frontend.libs.katlaf.FormatHandler;
import code.frontend.libs.katlaf.buttons.ButtonFoundation;
import code.frontend.libs.katlaf.graphics.MableBorder;
import code.frontend.libs.katlaf.lists.SimpleListMember;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import java.time.LocalDate;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class CountdownListMember extends SimpleListMember {
    private final Countdown countdown;

    public CountdownListMember(final Countdown countdown, final CountdownList parent) {
        super(parent.getSelcol());
        this.countdown = countdown;
    }

    /**
     * This is the display for the number of days remaining, overdue, since deletion or
     * since completion.
     */
    private class Counter extends StackPane {
        Counter() {
            this.setMouseTransparent(true);
            this.setBackground(null);
            final MableBorder border = new MableBorder(1, 0.2, 0.25);
            border.bindSize(this.widthProperty(), this.heightProperty());
            this.getChildren().add(border);
            /*
             * set up the text thingy now
             */
            String num;
            String post;
            final Countdown countdown = CountdownListMember.this.countdown;
            final LocalDate now = LocalDate.now();

            int days = Math.abs(countdown.getDaysUntilDue(now));
            num = days > 9999 ? FormatHandler.intToString(days) : Integer.toString(days);
            post = "days left";

            if (countdown.isDone() || countdown.getMarkedForDeletion()) {
                days = Math.abs(countdown.getDaysUntilCompletion(now));
                num = FormatHandler.intToString(days);
                post = "days ago";
            } else if (countdown.isOverdue(now)) {
                post = "days overdue";
            }

            final String result = num + " " + post;
            final Label label = new Label(result);
            label.setFont(FontHandler.getNormal());
            label.setTextFill(RiceHandler.getColour("white"));
            label.setBackground(null);
            label.setAlignment(Pos.CENTER);
            label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            this.getChildren().addLast(label);
        }
    }

    private class CompleteButton extends ButtonFoundation {
        private final MableBorder border;
        CompleteButton() {
            this.setBackground(null);
            this.border = new MableBorder(1, 0.2, 1);
            this.border.bindSize(this.widthProperty(), this.heightProperty());
            this.getChildren().addLast(this.border);
        }

        @Override
        public void onMousePressed(MouseEvent event) {
            // TODO Auto-generated method stub
        }

        @Override
        public final void onMouseEntered(MouseEvent event) {
            this.border.setStrokeColour(RiceHandler.getColour("lightblue"));
        }

        @Override
        public final void onMouseExited(MouseEvent event) {
            this.border.setStrokeColour(RiceHandler.getColour("white"));
        }

        @Override
        public final void onMouseReleased(MouseEvent event) {
            // does nothing
        }
    }
}
