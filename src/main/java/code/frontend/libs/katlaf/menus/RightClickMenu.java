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
import code.frontend.libs.katlaf.FontHandler;
import code.frontend.libs.katlaf.FontHandler.DedicatedFont;
import code.frontend.libs.katlaf.buttons.FilledButton;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * A UI component which appears when a supported UI component is right-clicked on.
 * It has a fixed width and height. Internally, this class uses a VBox of FilledButton
 * instances to implement the buttons.
 *
 * @since v3.0.0-beta
 *
 * @see VBox
 * @see FilledButton
 */
public final class RightClickMenu extends Region {
    private static final double BUTTON_HEIGHT = 25;
    private static final double WIDTH = 90;
    private static final double PADDING = 5;

    private final List<FilledButton> buttons;

    /**
     * Creates a new instance of this class. Use the addButton() method to
     * add buttons.
     */
    public RightClickMenu() {
        this.buttons = new ArrayList<FilledButton>();
    }

    /*


     PRIVATE API
    -------------------------------------------------------------------------------------*/

    private void despawn() {
        this.setVisible(false);
        final MainContainer mc = MainContainer.getInstance();
        mc.getChildren().remove(this);
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    /**
     * Initialises this instance at a specified position, relative to the window bounds.
     *
     * @param x     the x coordinate at which this instance should position its top
     *              left corner
     * @param y     the y coordinate at which this instance should position its top
     *              left corner
     */
    public void init(double x, double y) {
        final MainContainer mc = MainContainer.getInstance();

        this.setManaged(false);
        this.resize(mc.getScene().getWidth(), mc.getScene().getHeight());

        final VBox container = new VBox();
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(PADDING, PADDING, 0, PADDING));
        container.setBackground(RiceHandler.createBG(RiceHandler.getColour("darkgrey"), 8, 0));
        container.setViewOrder(-200);
        this.buttons.forEach(container.getChildren()::add);

        this.getChildren().add(container);

        double width = WIDTH + 2 * PADDING;
        if (x + width > mc.getWidth())
            x -= (x + width) - mc.getWidth();

        double height = BUTTON_HEIGHT * this.buttons.size() + 2 * PADDING;
        if (y + height > mc.getHeight())
            y -= (y + height) - mc.getHeight();

        container.resizeRelocate(x, y, width, height);

        mc.getChildren().add(this);

        this.setOnMouseClicked(event -> { this.despawn(); });
    }

    /**
     * Adds a button to this instance.
     * <p>
     * This method supports method chaining.
     *
     * @param label     the text this button should display
     * @param runnable  the procedure this button should run when clicked
     *
     * @return RightClickMenu the current instance
     */
    public RightClickMenu addButton(final String label, final Runnable runnable) {
        final FilledButton button =
            new FilledButton(Color.color(0, 0, 0, 0), RiceHandler.getColour("royalblue")) {
                @Override
                public void onMouseClicked(MouseEvent event) {
                    runnable.run();
                    despawn();
                    event.consume();
                }
            };
        VBox.setMargin(button, new Insets(0, 0, PADDING, 0));

        this.buttons.add(button);

        final Label text = new Label("  " + label);
        text.setAlignment(Pos.CENTER_LEFT);
        text.setTextFill(RiceHandler.getColour("white"));
        text.setFont(FontHandler.getDedicated(DedicatedFont.RIGHT_CLICK));
        text.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        button.setMinSize(WIDTH, BUTTON_HEIGHT);
        button.setMaxSize(WIDTH, BUTTON_HEIGHT);
        button.setTransitionDuration(50);
        button.getChildren().addAll(text);
        button.setViewOrder(-1);

        return this;
    }
}
