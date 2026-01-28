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

package code.frontend.libs.katlaf.menus;

import code.frontend.MainContainer;
import code.frontend.capabilities.countdown.components.CountdownRCM;
import code.frontend.capabilities.countdown.components.FolderRCM;
import code.frontend.libs.katlaf.buttons.BasicButton;
import code.frontend.libs.katlaf.buttons.Button;
import code.frontend.libs.katlaf.graphics.CustomLine;
import code.frontend.libs.katlaf.graphics.CustomLine.Type;
import code.frontend.libs.katlaf.graphics.MableBorder;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public abstract class RightClickMenu extends VBox {
    private static final double BG_RADIUS = 12;
    private static final double BG_INSETS = 3.5;

    private static final double BORDER_THICKNESS = 2;

    private static final int RIGHTLEFT_INSET = 15;

    private static final double BUTTON_HEIGHT = 35;

    private final double WIDTH;
    private final double HEIGHT;
    private final BasicButton[] BUTTONS;
    private final Color[] COLOURS;

    private MableBorder border;

    public RightClickMenu(double width, double height, BasicButton[] buttons, Color[] colours) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.BUTTONS = buttons;
        this.COLOURS = colours;
        initStyling();
        initButtonStylings();
    }

    public static void despawnAll() {
        CountdownRCM.despawn();
        FolderRCM.despawn();
    }

    public void openAt(double x, double y) {
        final MainContainer MC = MainContainer.getInstance();
        MC.getChildren().removeIf(child -> child instanceof RightClickMenu);

        if (x + this.WIDTH > MC.getWidth())
            x -= (x + this.WIDTH) - MC.getWidth();
        if (y + this.HEIGHT > MC.getHeight())
            y -= (y + this.HEIGHT) - MC.getHeight();

        this.setMode();
        this.relocate(x, y);
        this.setViewOrder(-100);
        MC.getChildren().add(this);
    }

    protected void close() {
        final MainContainer MC = MainContainer.getInstance();
        MC.getChildren().remove(this);
    }

    private void initButtonStylings() {
        final CornerRadii CORNER_RADII = new CornerRadii(8);
        final Insets INSETS = new Insets(5, -5, 5, -5);

        assert this.COLOURS.length == this.BUTTONS.length;
        for (int i = 0; i < this.BUTTONS.length; i++) {
            Button button = this.BUTTONS[i];
            if (button != null) {
                button.setPrefSize(WIDTH, BUTTON_HEIGHT);
                button.setMaxSize(WIDTH, BUTTON_HEIGHT);
                button.getLabel().setAlignment(Pos.CENTER_LEFT);
                VBox.setMargin(button, new Insets(0, RIGHTLEFT_INSET, 0, RIGHTLEFT_INSET));
                // colouring
                BackgroundFill fill = new BackgroundFill(this.COLOURS[i], CORNER_RADII, INSETS);
                button.setCustomBackground(new Background(fill));
                this.getChildren().add(button);
            } else {
                this.getChildren().add(createDivider());
            }
        }
    }

    private void initStyling() {
        this.border = new MableBorder(BORDER_THICKNESS, 0.2, 0.2);
        MableBorder.applyToPane(this, border);
        this.setManaged(false);
        this.setBackground(
            RiceHandler.createBG(RiceHandler.getColour("background1"), BG_RADIUS, BG_INSETS));
        this.setFillWidth(true);
        this.resize(WIDTH, HEIGHT);
        this.setVisible(true);
        this.setAlignment(Pos.CENTER);
    }

    protected Region createDivider() {
        final double DIVIDER_HEIGHT = 5;
        Pane divider = new Pane();
        divider.setPrefSize(WIDTH, DIVIDER_HEIGHT);
        divider.setMaxSize(WIDTH, DIVIDER_HEIGHT);
        divider.setBackground(null);
        divider.setOpacity(0.3);
        CustomLine line = new CustomLine(BORDER_THICKNESS, Type.HORIZONTAL_TYPE);
        line.setPadding(RIGHTLEFT_INSET);
        line.setStrokeColour(RiceHandler.getColour());
        CustomLine.applyToPane(divider, line);

        return divider;
    }

    public MableBorder getCustomBorder() {
        return border;
    }

    public abstract void setMode();
}
