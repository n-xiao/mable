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
import code.frontend.capabilities.concurrency.Updatable;
import code.frontend.libs.katlaf.FontHandler;
import code.frontend.libs.katlaf.FontHandler.DedicatedFont;
import code.frontend.libs.katlaf.FormatHandler;
import code.frontend.libs.katlaf.buttons.ButtonFoundation;
import code.frontend.libs.katlaf.graphics.MableBorder;
import code.frontend.libs.katlaf.lists.SimpleListMember;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import java.time.LocalDate;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public final class CountdownListMember extends SimpleListMember implements Updatable {
    private final Countdown countdown;
    private final CountdownList list;

    public CountdownListMember(final Countdown countdown, final CountdownList list) {
        super(list.getSelector());
        this.countdown = countdown;
        this.list = list;

        final var container = new HBox();
        final var spacer = new Pane();
        spacer.setVisible(false);
        HBox.setHgrow(spacer, Priority.ALWAYS);
        container.setPadding(new Insets(0, 5, 0, 5));
        /*
         * add the children now
         */
        var completeButton = new CompleteButton();
        var nameplate = new Nameplate();
        HBox.setMargin(nameplate, new Insets(0, 0, 0, 5));
        var dateplate = new Dateplate();
        HBox.setMargin(dateplate, new Insets(0, 5, 0, 0));
        var counter = new Counter();
        this.getChildren().addAll(completeButton, nameplate, spacer, dateplate, counter);
    }

    /*


     PRIVATE API
    -------------------------------------------------------------------------------------*/

    private void style() {
        if (this.isToggled()) {
            // TODO
        }
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    /**
     * Increments (and/or changes) the number of days which remains for this instance
     * as time passes.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public void update() {
        this.getChildren().forEach(child -> {
            if (child instanceof Updatable updatable) {
                updatable.update();
            }
        });
    }

    @Override
    public void setToggle(boolean toggled) {
        super.setToggle(toggled);
        this.style();
    }

    @Override
    public void onMouseEntered(MouseEvent event) {
        // do nothin
    }

    @Override
    public void onMouseExited(MouseEvent event) {
        // do nothin
    }

    @Override
    public void onMouseReleased(MouseEvent event) {
        // do nothin
    }

    /*


     COMPOSITIONS
    -------------------------------------------------------------------------------------*/

    private class Nameplate extends Label implements Updatable {
        Nameplate() {
            this.setAlignment(Pos.CENTER_LEFT);
            this.setTextFill(RiceHandler.getColour("white"));
            this.setFont(FontHandler.getDedicated(DedicatedFont.COUNTDOWN_NAME));
            this.setMaxHeight(Double.MAX_VALUE);
            update();
        }

        @Override
        public void update() {
            this.setText(countdown.getName());
        }
    }

    private class Dateplate extends Label {
        Dateplate() {
            this.setAlignment(Pos.CENTER);
            this.setTextFill(RiceHandler.getColour("grey"));
            this.setFont(FontHandler.getSubtitle());
            this.setMaxHeight(Double.MAX_VALUE);
        }
    }

    /**
     * This is the display for the number of days remaining, overdue, since deletion or
     * since completion.
     */
    private class Counter extends StackPane implements Updatable {
        final Label label;
        Counter() {
            this.setMouseTransparent(true);
            this.setBackground(null);
            final MableBorder border = new MableBorder(1, 0.2, 0.25);
            border.bindSize(this.widthProperty(), this.heightProperty());
            this.getChildren().add(border);
            /*
             * set up the text thingy now
             */
            this.label = new Label();
            this.label.setFont(FontHandler.getNormal());
            this.label.setTextFill(RiceHandler.getColour("white"));
            this.label.setBackground(null);
            this.label.setAlignment(Pos.CENTER);
            this.label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            update();
            this.getChildren().addLast(label);
        }

        @Override
        public void update() {
            String num;
            String post;
            final Countdown countdown = CountdownListMember.this.countdown;
            final LocalDate now = LocalDate.now();

            int days = Math.abs(countdown.getDaysUntilDue(now));
            num = days > 9999 ? FormatHandler.intToString(days) : Integer.toString(days);
            post = "days left";

            if (countdown.isDone() || countdown.isDeleted()) {
                days = Math.abs(countdown.getDaysUntilCompletion(now));
                num = FormatHandler.intToString(days);
                post = "days ago";
            } else if (countdown.isOverdue(now)) {
                post = "days overdue";
            }

            this.label.setText(num + " " + post);
        }
    }

    /**
     * The circular button which resembles a bullet point. It is empty if the countdown
     * is incomplete, filled otherwise.
     */
    private class CompleteButton extends ButtonFoundation {
        private final MableBorder border;
        private final Region fill;
        CompleteButton() {
            /*
             * set the border up first
             */
            this.border = new MableBorder(1, 0.2, 1);
            this.border.bindSize(this.widthProperty(), this.heightProperty());
            /*
             * set the fill up now
             */
            this.fill = new Region();
            this.fill.prefWidthProperty().bind(this.widthProperty());
            this.fill.prefHeightProperty().bind(this.heightProperty());
            // TODO figure out how to create a circular bg fill
            final var backgroundFill = new BackgroundFill(
                RiceHandler.getColour("blue"), new CornerRadii(5), new Insets(1.5));
            this.fill.setBackground(new Background(backgroundFill));
            this.fill.setOpacity(0);
            /*
             * lastly, take the daily dose of boilerplate
             */
            this.setBackground(null);
            this.getChildren().addAll(this.border, this.fill);
        }

        /**
         * Marks the Countdown of this CountdownListMember as done (completed) if it is
         * not done. Marks the Countdown of this CountdownListMember as undone if it is
         * done.
         * <p>
         * Logically speaking, this call should always remove the CountdownListMember
         * from its current CountdownList because the list for completed Countdowns and
         * the list for incomplete Countdowns are different, and one one of such list(s)
         * should be displayed at any given moment.
         * <p>
         * Lastly, this method consumes the event so that is does not propagate and trigger
         * an unnecessary selection.
         *
         * @see Countdown
         * @see CountdownList
         *
         * @throws IllegalCallerException      although technically possible, this method should
         *                                     never be called on a Countdown that has already been
         *                                     deleted. It doesn't make sense.
         */
        @Override
        public void onMousePressed(MouseEvent event) {
            if (countdown.isDeleted())
                throw new IllegalCallerException(
                    "a deleted Countdown shouldn't be set as done or not done. what r u doing?");

            list.getSelector().deselectAll();
            if (countdown.isDone()) {
                this.fill.setOpacity(0);
            } else {
                this.fill.setOpacity(1);
                countdown.updateCompletionDateTime();
            }
            countdown.setDone(!countdown.isDone());
            list.removeMember(CountdownListMember.this);

            event.consume();
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
