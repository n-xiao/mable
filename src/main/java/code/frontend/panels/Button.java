/*
   Copyright (C) 2026  Nicholas Siow

   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU Affero General Public License as
   published by the Free Software Foundation, either version 3 of the
   License, or (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU Affero General Public License for more details.

   You should have received a copy of the GNU Affero General Public License
   along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package code.frontend.panels;

import code.frontend.foundation.CustomBox;
import code.frontend.misc.Vals;
import code.frontend.misc.Vals.Colour;
import javafx.animation.FadeTransition;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

public abstract class Button extends Pane {
    private final double HOVER_OPACITY = 0.2;

    private Font labelFont;
    private Color feedbackColour;
    private Color colour;
    private double borderThickness;

    private Pane animPane = new Pane();
    private FadeTransition ft = new FadeTransition();
    private boolean animationsEnabled;

    private Label label;
    private CustomBox border;

    private boolean enabled;
    private boolean consumeEvent;

    public Button(String text) {
        this.consumeEvent = false;
        this.labelFont = Vals.FontTools.getButtonFont();
        this.feedbackColour = Vals.Colour.FEEDBACK;
        this.colour = Color.WHITE;
        this.borderThickness = Vals.GraphicalUI.BTTN_THICKNESS;
        this.enabled = true;
        this.animationsEnabled = true;

        this.border = new CustomBox(borderThickness);
        CustomBox.applyCustomBorder(this, border);

        this.label = new Label(text);
        this.label.setTextFill(colour);
        this.label.setAlignment(Pos.CENTER);
        this.label.setFont(labelFont);
        this.label.prefWidthProperty().bind(this.widthProperty());
        this.label.prefHeightProperty().bind(this.heightProperty());
        this.label.relocate(0, 0);
        this.label.setViewOrder(0);

        double vertiInset = border.getVertiPadding() + 3;
        double horizInset = border.getHorizPadding() + 4;
        Insets animPaneInsets = new Insets(vertiInset, horizInset, vertiInset, horizInset);
        BackgroundFill bgFill =
            new BackgroundFill(feedbackColour, new CornerRadii(12), animPaneInsets);
        this.animPane.setBackground(new Background(bgFill));
        this.animPane.prefWidthProperty().bind(this.widthProperty());
        this.animPane.prefHeightProperty().bind(this.heightProperty());
        this.animPane.relocate(0, 0);
        this.animPane.setOpacity(0);
        this.animPane.setViewOrder(1);
        this.ft.setNode(animPane);

        this.getChildren().addAll(label, animPane);

        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (consumeEvent)
                    event.consume();
                if (enabled) {
                    executeOnClick();
                    playClickAnim();
                }
            }
        });
        this.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (enabled) {
                    playMouseEnterAnim();
                }
            }
        });
        this.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (enabled) {
                    playMouseExitAnim();
                }
            }
        });
    }

    public abstract void executeOnClick();

    protected void playClickAnim() {
        if (!animationsEnabled)
            return;
        this.ft.stop();
        this.ft.setDuration(Duration.millis(200));
        this.ft.setFromValue(0.8);
        this.ft.setToValue(0);
        this.ft.playFromStart();
    }

    private void playMouseEnterAnim() {
        if (!animationsEnabled) {
            this.animPane.setOpacity(1);
            return;
        }
        this.ft.stop();
        this.ft.setDuration(Duration.millis(300));
        this.ft.setFromValue(0);
        this.ft.setToValue(HOVER_OPACITY);
        this.ft.playFromStart();
    }

    private void playMouseExitAnim() {
        if (!animationsEnabled) {
            this.animPane.setOpacity(0);
            return;
        }
        this.ft.stop();
        this.ft.setDuration(Duration.millis(300));
        this.ft.setFromValue(HOVER_OPACITY);
        this.ft.setToValue(0);
        this.ft.playFromStart();
    }

    public Label getLabel() {
        return label;
    }

    public CustomBox getCustomBorder() {
        return border;
    }

    public void setFeedbackColour(Color feedbackColour) {
        this.feedbackColour = feedbackColour;
    }

    public void setFeedbackBackground(Background bg) {
        this.animPane.setBackground(bg);
    }

    protected Pane getFeedbackBackground() {
        return this.animPane;
    }

    public void setColour(Color colour) {
        this.colour = colour;
        this.border.setStrokeColour(colour);
    }

    public void setBorderThickness(int borderThickness) {
        this.borderThickness = borderThickness;
    }

    public void setTextLabel(String text) {
        this.label.setText(text);
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            this.border.setStrokeColour(colour);
            this.label.setTextFill(colour);
        } else {
            this.border.setStrokeColour(Colour.DISABLED);
            this.label.setTextFill(Colour.DISABLED);
        }
        this.enabled = enabled;
    }

    public void setConsumeEvent(boolean consumeEvent) {
        this.consumeEvent = consumeEvent;
    }

    public void setAnimationsEnabled(boolean animationsEnabled) {
        this.animationsEnabled = animationsEnabled;
    }
}
