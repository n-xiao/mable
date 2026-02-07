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

package code.frontend.libs.katlaf.buttons;

import code.frontend.libs.katlaf.FontHandler;
import code.frontend.libs.katlaf.graphics.MableBorder;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

public abstract class Button extends TemplateButton {
    private Font labelFont;
    private Color colour;

    private Pane bg = new Pane();
    private FadeTransition ft = new FadeTransition();

    private Label label;
    private MableBorder border;

    public Button(String text) {
        this.labelFont = FontHandler.getNormal();
        this.colour = RiceHandler.getColour();

        this.border = new MableBorder(1.5, 0.3, 0.35);
        MableBorder.applyToPane(this, border);

        this.label = new Label(text);
        this.label.setTextFill(RiceHandler.getColour());
        this.label.setAlignment(Pos.CENTER);
        this.label.setFont(labelFont);
        this.label.prefWidthProperty().bind(this.widthProperty());
        this.label.prefHeightProperty().bind(this.heightProperty());
        this.label.relocate(0, 0);

        Insets animPaneInsets = new Insets(border.getPaddingDist());
        BackgroundFill bgFill = new BackgroundFill(colour, new CornerRadii(7), animPaneInsets);
        this.bg.setBackground(new Background(bgFill));
        this.bg.prefWidthProperty().bind(this.widthProperty());
        this.bg.prefHeightProperty().bind(this.heightProperty());
        this.bg.relocate(0, 0);
        this.bg.setOpacity(0);
        this.ft.setNode(bg);

        this.getChildren().addAll(label, bg);
        this.label.setViewOrder(0);
        this.bg.setViewOrder(1);

        this.setCursor(Cursor.HAND);
    }

    @Override
    protected void onMouseEnter(MouseEvent event) {
        playMouseEnterAnim();
    }

    @Override
    protected void onMouseLeave(MouseEvent event) {
        playMouseExitAnim();
    }

    @Override
    protected void onMousePress(MouseEvent event) {
        executeOnClick(event);
    }

    public abstract void executeOnClick(MouseEvent event);

    protected void playMouseEnterAnim() {
        this.ft.stop();
        this.ft.setDuration(Duration.millis(150));
        this.ft.setFromValue(0);
        this.ft.setToValue(0.1);
        this.ft.playFromStart();
    }

    protected void playMouseExitAnim() {
        this.ft.stop();
        this.ft.setDuration(Duration.millis(300));
        this.ft.setFromValue(0.1);
        this.ft.setToValue(0);
        this.ft.playFromStart();
    }

    public Label getLabel() {
        return label;
    }

    public void setCustomBackground(Background bg) {
        this.bg.setBackground(bg);
    }

    protected Pane getCustomBackground() {
        return this.bg;
    }

    public void setTextLabel(String text) {
        this.label.setText(text);
    }

    public MableBorder getCustomBorder() {
        return this.border;
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled) {
            this.border.setStrokeColour(colour);
            this.label.setTextFill(colour);
        } else {
            this.border.setStrokeColour(RiceHandler.getColour("darkgrey"));
            this.label.setTextFill(RiceHandler.getColour("darkgrey"));
        }
        super.setEnabled(enabled);
    }
}
