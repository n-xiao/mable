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
import code.frontend.libs.katlaf.FontHandler;
import code.frontend.libs.katlaf.FontHandler.DedicatedFont;
import code.frontend.libs.katlaf.FormatHandler;
import code.frontend.libs.katlaf.graphics.CustomLine;
import code.frontend.libs.katlaf.graphics.CustomLine.Type;
import code.frontend.libs.katlaf.graphics.MableBorder;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import java.time.LocalDate;
import javafx.animation.Animation.Status;
import javafx.animation.FadeTransition;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

class CountdownPane extends VBox {
    public static final double WIDTH = 255;
    public static final double HEIGHT = 110;
    public static final double NAME_WIDTH = 150;
    public static final double DIV_WIDTH = 8;
    public static final double CONTENT_HEIGHT = 90;

    private final Label descriptorLabel;
    private final Label daysLabel;
    private final Label statusLabel; // for displaying the status on mouse hover
    private final Label endDateLabel; // for displaying the due date on mouse hover
    private final Label nameLabel;

    private final CustomLine divider;
    private final HBox hoverContainer; // container
    private final HBox content; // container
    private final FadeTransition fadeTransition; // for ui hover animation
    private final MableBorder mableBorder; // for selection ui indication

    private Countdown countdown; // points to the backend object
    private boolean selected; // for selection detection
    private Color borderColour;

    public CountdownPane(Countdown cd, LocalDate now) {
        this.hoverContainer = new HBox();
        this.content = new HBox();
        this.divider = new CustomLine(2, Type.VERTICAL_TYPE);
        this.descriptorLabel = new Label();
        this.daysLabel = new Label();
        this.statusLabel = new Label();
        this.endDateLabel = new Label();
        this.nameLabel = new Label();
        this.mableBorder = new MableBorder(2.2, 0.19, 0.42);
        this.fadeTransition = new FadeTransition(Duration.millis(300), hoverContainer);
        this.countdown = cd;
        this.selected = false;
        this.setAlignment(Pos.CENTER);
        initContentHBox(now);
        initHoverHBox();
        this.getChildren().addAll(this.hoverContainer, this.content);
    }

    /*


     BEHAVIOUR
    -------------------------------------------------------------------------------------*/

    private void initHoverHBox() {
        int leftRightPadding = 16;
        double height = HEIGHT - CONTENT_HEIGHT;
        this.hoverContainer.setPrefSize(WIDTH, height);
        Font font = FontHandler.getSubtitle();
        this.statusLabel.setAlignment(Pos.BOTTOM_LEFT);
        this.statusLabel.setFont(font);
        this.statusLabel.setTextFill(RiceHandler.getColour("lightgrey"));
        this.statusLabel.setMaxSize(WIDTH / 2, height);
        HBox.setMargin(this.statusLabel, new Insets(0, 0, 0, leftRightPadding));
        Pane spacer = new Pane();
        spacer.setMaxSize(WIDTH, height);
        HBox.setHgrow(spacer, Priority.ALWAYS);
        this.endDateLabel.setAlignment(Pos.BOTTOM_RIGHT);
        this.endDateLabel.setFont(font);
        this.endDateLabel.setTextFill(RiceHandler.getColour("lightgrey"));
        this.endDateLabel.setMaxSize(WIDTH / 2, height);
        HBox.setMargin(this.endDateLabel, new Insets(0, leftRightPadding, 0, 0));
        this.hoverContainer.setFillHeight(true);
        this.hoverContainer.getChildren().addAll(this.statusLabel, spacer, this.endDateLabel);
        this.hoverContainer.setOpacity(0);

        content.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (selected)
                    return; // do nothing if selected
                fadeTransition.stop();
                setMouseEnterAnim(fadeTransition);
                fadeTransition.playFromStart();
            }
        });

        content.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                final boolean ALREADY_RUNNING =
                    fadeTransition.getStatus() == Status.RUNNING && fadeTransition.getToValue() == 0
                    || hoverContainer.getOpacity() == 0;
                if (selected || ALREADY_RUNNING)
                    return; // do nothing if selected
                fadeTransition.stop();
                setMouseExitAnim(fadeTransition);
                fadeTransition.playFromStart();
            }
        });
    }

    private void setMouseEnterAnim(FadeTransition anim) {
        anim.setFromValue(0);
        anim.setToValue(1);
    }

    private void setMouseExitAnim(FadeTransition anim) {
        anim.setFromValue(1);
        anim.setToValue(0);
    }

    private void initContentHBox(LocalDate now) {
        content.setPrefSize(WIDTH, CONTENT_HEIGHT);
        content.setFillHeight(true);
        // adds the border
        this.borderColour = RiceHandler.getColour("white"); // TODO: implement custom colour
        mableBorder.setStrokeColour(this.borderColour);
        MableBorder.applyToPane(content, mableBorder);
        // adds the name display
        content.getChildren().add(createNameLabel(countdown));
        // adds the divider
        content.getChildren().add(createVerticalDivider());
        // adds the day countdown pane
        content.getChildren().add(createCountdownDisplay(countdown, now));
    }

    private Label createNameLabel(Countdown cd) {
        String name = cd.getName();
        nameLabel.setText(name);
        Font nameFont = FontHandler.getDedicated(DedicatedFont.COUNTDOWN_NAME);
        nameLabel.setAlignment(Pos.CENTER);
        nameLabel.setTextAlignment(TextAlignment.CENTER);
        nameLabel.setWrapText(true);
        nameLabel.setFont(nameFont);
        nameLabel.setTextFill(RiceHandler.getColour("white"));
        nameLabel.setPrefWidth(NAME_WIDTH);
        nameLabel.prefHeightProperty().bind(this.heightProperty());

        HBox.setMargin(nameLabel, new Insets(10, 2, 10, 14));
        HBox.setHgrow(nameLabel, Priority.ALWAYS);
        return nameLabel;
    }

    private Pane createVerticalDivider() {
        Pane pane = new Pane();
        pane.setPrefSize(DIV_WIDTH, HEIGHT);
        divider.setStrokeColour(RiceHandler.getColour("white"));
        divider.setOpacity(0.4);
        divider.setPadding(20);
        CustomLine.applyToPane(pane, divider);
        return pane;
    }

    private VBox createCountdownDisplay(Countdown cd, LocalDate now) {
        VBox display = new VBox();

        Font numFont = FontHandler.getDedicated(DedicatedFont.COUNTDOWN_NUM);
        daysLabel.setAlignment(Pos.CENTER);
        daysLabel.setTextAlignment(TextAlignment.CENTER);
        daysLabel.setFont(numFont);
        daysLabel.setTextFill(RiceHandler.getColour("white"));
        daysLabel.prefWidthProperty().bind(display.widthProperty());

        Font textFont = FontHandler.getDedicated(DedicatedFont.COUNTDOWN_INFO);
        descriptorLabel.setAlignment(Pos.CENTER);
        descriptorLabel.setTextAlignment(TextAlignment.CENTER);
        descriptorLabel.setFont(textFont);
        descriptorLabel.setTextFill(RiceHandler.getColour("white"));
        descriptorLabel.prefWidthProperty().bind(display.widthProperty());

        configureCountdownLabelsText(countdown, now);

        display.getChildren().addAll(daysLabel, descriptorLabel);
        HBox.setMargin(display, new Insets(10, 5, 10, 0));
        HBox.setHgrow(display, Priority.ALWAYS);
        return display;
    }

    private void configureStatus(Countdown countdown, LocalDate now) {
        final String statusString = getStatusString(now);
        final String dateString = getStringDueDate(now);
        this.statusLabel.setText(statusString);
        this.endDateLabel.setText(dateString);
    }

    private void configureCountdownLabelsText(Countdown countdown, LocalDate now) {
        int daysLeft = Math.abs(countdown.getDaysUntilDue(now));
        daysLabel.setText(getDaysLeftString(now));
        String textNoun = getTextNoun(daysLeft);
        String textAdverb = getTextAdverb(countdown.isOverdue(now));
        descriptorLabel.setText(textNoun + "\n" + textAdverb);
    }

    /*


     PROTECTED API
    -------------------------------------------------------------------------------------*/

    protected final void refreshContent() {
        LocalDate now = LocalDate.now();
        nameLabel.setText(this.countdown.getName());
        configureCountdownLabelsText(this.countdown, now);
        configureStatus(this.countdown, now);
    }

    protected String getTextNoun(int daysLeft) {
        return daysLeft != 1 ? "DAYS" : "DAY";
    }

    protected String getTextAdverb(boolean isOverdue) {
        return isOverdue ? "OVERDUE" : "LEFT";
    }

    protected String getStatusString(final LocalDate now) {
        return countdown.getStatus(now);
    }

    /**
     * Returns a string representation of the due date in
     * the user's local date and time.
     */
    protected String getStringDueDate(LocalDate now) {
        final LocalDate localDue = countdown.getLocalDueDate(now);
        final String day = Integer.toString(localDue.getDayOfMonth());
        final String month = Integer.toString(localDue.getMonthValue());
        final String year = Integer.toString(localDue.getYear());
        return "Due: " + day + "/" + month + "/" + year; // uses the correct format
    }

    protected String getDaysLeftString(final LocalDate now) {
        final int days = Math.abs(countdown.getDaysUntilDue(now));
        return FormatHandler.intToString(days);
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    public void applySelectStyle() {
        this.fadeTransition.stop();
        this.hoverContainer.setOpacity(1);
        setColour(RiceHandler.getColour("blue"));
        this.statusLabel.setTextFill(RiceHandler.getColour("blue"));
        this.endDateLabel.setTextFill(RiceHandler.getColour("blue"));
    }

    public void applyDeselectStyle(boolean withFadeOut) {
        if (withFadeOut) {
            this.fadeTransition.stop();
            this.fadeTransition.setToValue(0);
            this.fadeTransition.setFromValue(this.hoverContainer.getOpacity());
            this.fadeTransition.playFromStart();
        }
        this.setColour(RiceHandler.getColour("night"));
        this.statusLabel.setTextFill(RiceHandler.getColour("lightgrey"));
        this.endDateLabel.setTextFill(RiceHandler.getColour("lightgrey"));
    }

    public void setCountdown(Countdown countdown) {
        this.countdown = countdown;
    }

    public Countdown getCountdown() {
        return countdown;
    }

    public void setColour(Color colour) {
        content.setBackground(
            RiceHandler.createBG(RiceHandler.adjustOpacity(colour, 0.2), 14, 3.5));
    }

    public Color getBorderColour() {
        return this.borderColour;
    }
}
