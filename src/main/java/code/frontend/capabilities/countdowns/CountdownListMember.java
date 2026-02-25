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
import code.frontend.libs.katlaf.graphics.CustomLine;
import code.frontend.libs.katlaf.graphics.CustomLine.Type;
import code.frontend.libs.katlaf.graphics.MableBorder;
import code.frontend.libs.katlaf.interfaces.Colourable;
import code.frontend.libs.katlaf.lists.SimpleListMember;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import code.frontend.libs.katlaf.transitions.Transitioner;
import java.time.LocalDate;
import javafx.animation.FadeTransition;
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

final class CountdownListMember
    extends SimpleListMember implements Updatable, Colourable, Comparable<CountdownListMember> {
    private final Countdown countdown;
    private final CountdownList list;
    private final HBox content;
    private final CompleteButton completeButton;
    private final Nameplate nameplate;
    private final Dateplate dateplate;
    private final Counter counter;
    private final VBox container;
    /*
     * transition fields for complete action
     */
    private final Transitioner compTransitioner;
    private final FadeTransition nameFade;
    private final FadeTransition dateFade;
    private final FadeTransition counterFade;
    private final FadeTransition compFade;
    /*
     * transition fields for delete action
     */
    private final Transitioner deleteTransitioner;
    private final FadeTransition globalFade;

    CountdownListMember(final Countdown countdown, final CountdownList list) {
        super(list.getSelector());
        this.countdown = countdown;
        this.list = list;
        /*
         * init the content
         */
        this.content = new HBox();
        this.content.setBackground(null);
        final var spacer = new Pane();
        spacer.setVisible(false);
        HBox.setHgrow(spacer, Priority.ALWAYS);
        this.content.setFillHeight(false);
        this.content.setAlignment(Pos.CENTER);
        this.content.setMinHeight(35);

        /*
         * init the children
         */
        this.completeButton = new CompleteButton();
        HBox.setMargin(completeButton, new Insets(3, 2, 3, 2));
        this.nameplate = new Nameplate();
        HBox.setMargin(nameplate, new Insets(0, 0, 0, 8));
        this.dateplate = new Dateplate();
        HBox.setMargin(dateplate, new Insets(0, 8, 0, 0));
        this.counter = new Counter();
        this.counter.setPrefHeight(25);

        this.content.getChildren().addAll(completeButton, nameplate, spacer, dateplate, counter);
        /*
         * configure the vbox container
         */
        this.container = new VBox();
        this.container.setFillWidth(true);
        this.container.prefWidthProperty().bind(this.widthProperty());
        this.container.prefHeightProperty().bind(this.heightProperty());
        this.container.setBackground(null);
        final var divider = new CustomLine(0.5, Type.HORIZONTAL);
        divider.widthProperty().bind(this.container.widthProperty());
        divider.setHeight(1);
        divider.setColour("grey");
        this.container.getChildren().addAll(this.content, divider);
        this.getChildren().add(this.container);
        /*
         * set up transitions
         */
        final int millis = 400;
        this.nameFade = new FadeTransition(Duration.millis(millis), this.nameplate);
        this.dateFade = new FadeTransition(Duration.millis(millis), this.dateplate);
        this.counterFade = new FadeTransition(Duration.millis(millis), this.counter);
        this.compFade = new FadeTransition(Duration.millis(200), this.completeButton.fill);
        this.compTransitioner =
            new Transitioner()
                .prepare()
                .playParallel(this.nameFade, this.dateFade, this.counterFade, this.compFade)
                .hold(Duration.millis(1200));

        this.globalFade = new FadeTransition(Duration.millis(millis), this.content);
        this.deleteTransitioner = new Transitioner().prepare().play(this.globalFade);
    }

    /*


     PROTECTED API
    -------------------------------------------------------------------------------------*/

    /**
     * Useful when trying to implement a custom counter, such as for a "relative"
     * countdown mode.
     */
    protected final void removeCounter() {
        this.content.getChildren().remove(this.counter);
    }

    protected final Countdown getCountdown() {
        return this.countdown;
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
        this.content.getChildren().forEach(child -> {
            if (child instanceof Updatable updatable) {
                updatable.update();
            }
        });
    }

    @Override
    public void setColour(Color colour) {
        this.content.getChildren().forEach(child -> {
            if (child instanceof Colourable colourable) {
                colourable.setColour(colour);
            }
        });
    }

    @Override
    public void resetColour() {
        this.content.getChildren().forEach(child -> {
            if (child instanceof Colourable colourable) {
                colourable.resetColour();
            }
        });
    }

    @Override
    public void setToggle(boolean toggled) {
        super.setToggle(toggled);
        if (this.isToggled()) {
            this.content.setBackground(
                RiceHandler.createBG(RiceHandler.getColour("dullgrey"), 0, 0));
        } else {
            this.content.setBackground(null);
        }
    }

    @Override
    public int compareTo(CountdownListMember o) {
        return this.countdown.compareTo(o.countdown);
    }

    /*


     COMPOSITIONS
    -------------------------------------------------------------------------------------*/

    private class Nameplate extends Label implements Updatable, Colourable {
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

        @Override
        public void setColour(Color colour) {
            this.setTextFill(colour);
        }

        @Override
        public void resetColour() {
            this.setTextFill(countdown.getColour());
        }
    }

    private class Dateplate extends Label {
        Dateplate() {
            this.setAlignment(Pos.CENTER);
            this.setTextFill(RiceHandler.getColour("lightgrey"));
            this.setFont(FontHandler.getSubtitle());
            this.setMaxHeight(Double.MAX_VALUE);
            final LocalDate now = LocalDate.now();
            final LocalDate localDate = countdown.isDone() ? countdown.getLocalCompletionDate(now)
                                                           : countdown.getLocalDueDate(now);
            final String pretext = countdown.isDone() ? "Completed: " : "Due: ";
            final String day = Integer.toString(localDate.getDayOfMonth());
            final String month = Integer.toString(localDate.getMonthValue());
            final String year = Integer.toString(localDate.getYear());

            final String text = pretext + day + "/" + month + "/" + year;
            this.setText(text);
        }
    }

    /**
     * This is the display for the number of days remaining, overdue, since deletion or
     * since completion.
     */
    private class Counter extends StackPane implements Updatable, Colourable {
        final Label label;
        final MableBorder border;
        Counter() {
            this.setMouseTransparent(true);
            this.setBackground(null);
            this.border = new MableBorder(1, 0.2, 0.4);
            this.border.bindSize(this.widthProperty(), this.heightProperty());
            this.getChildren().add(border);
            /*
             * set up the text thingy now
             */
            this.label = new Label();
            this.label.setFont(FontHandler.getDedicated(DedicatedFont.COUNTDOWN_NUM));
            this.label.setTextFill(RiceHandler.getColour("white"));
            this.label.setBackground(null);
            this.label.setAlignment(Pos.CENTER);
            StackPane.setMargin(this.label, new Insets(0, 6, 0, 6));
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

        @Override
        public void setColour(Color colour) {
            this.border.setColour(colour);
            this.label.setTextFill(colour);
        }

        @Override
        public void resetColour() {
            setColour(countdown.getColour());
        }
    }

    /**
     * The circular button which resembles a bullet point. It is empty if the countdown
     * is incomplete, filled otherwise.
     */
    private class CompleteButton extends ButtonFoundation implements Colourable {
        private final MableBorder border;
        private final Region fill;
        private boolean tempIsDone;
        CompleteButton() {
            /*
             * set the border up first
             */
            this.border = new MableBorder(1, 0.05, 1);
            this.border.bindSize(this.widthProperty(), this.heightProperty());
            /*
             * set the fill up now
             */
            this.fill = new Region();
            this.fill.prefWidthProperty().bind(this.widthProperty());
            this.fill.prefHeightProperty().bind(this.heightProperty());
            final var backgroundFill =
                new BackgroundFill(countdown.getColour(), new CornerRadii(12), new Insets(4));
            this.fill.setBackground(new Background(backgroundFill));
            this.fill.setOpacity(0);
            this.setBackground(null);
            this.getChildren().addAll(this.border, this.fill);
            this.resize(18, 18);

            this.tempIsDone = countdown.isDone();
        }

        @Override
        public void setColour(Color colour) {
            final var backgroundFill =
                new BackgroundFill(colour, new CornerRadii(5), new Insets(1.5));
            this.fill.setBackground(new Background(backgroundFill));
            this.border.setColour(colour);
        }

        @Override
        public void resetColour() {
            setColour(countdown.getColour());
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
            if (!this.isEnabled())
                return;

            list.getSelector().deselectAll();

            if (this.tempIsDone) {
                compTransitioner.setFadeToValues(1);
                compFade.setToValue(0);
            } else {
                compTransitioner.setFadeToValues(0.7);
                compFade.setToValue(1);
                countdown.updateCompletionDateTime();
            }
            this.tempIsDone = !this.tempIsDone;
            compTransitioner.getTransition().setOnFinished(e -> {
                if (this.tempIsDone == countdown.isDone())
                    return;

                countdown.setDone(tempIsDone);
                /*
                 * this action shall not un-delete deleted countdowns
                 */
                this.setEnabled(false);
                if (!countdown.isDeleted()) {
                    list.removeMember(CountdownListMember.this);
                }
            });
            compTransitioner.getTransition().playFromStart();

            event.consume();
        }

        @Override
        public boolean isResizable() {
            return false;
        }
    }
}
