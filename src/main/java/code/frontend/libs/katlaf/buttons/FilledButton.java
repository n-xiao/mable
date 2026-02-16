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

import code.frontend.libs.katlaf.faces.LabelFace;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import javafx.animation.FadeTransition;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * A rounded button without a MableBorder, which extends the LabelledButtonFace.
 * This button implements a mouse hover effect, changing the button's
 * background colour using specified colours when the user hovers over
 * the button.
 *
 * @since v3.0.0-beta
 * @see LabelFace
 */
public abstract class FilledButton extends ButtonFoundation {
    private static final double RADIUS = 14;
    private final LabelFace face;
    private final Color normal;
    private final Color hover;
    private final Region hoverRegion;
    private final FadeTransition transition;

    /**
     * Creates a new FilledButton instance with the two provided colours.
     *
     * @param normal    the colour of the button when the user is not
     *                  hovering their mouse over it
     * @param hover     the colour of the button when the user is
     *                  hovering their mouse over it
     */
    public FilledButton(final Color normal, final Color hover) {
        this.face = new LabelFace();
        this.face.prefWidthProperty().bind(this.widthProperty());
        this.face.prefHeightProperty().bind(this.heightProperty());
        this.getChildren().addFirst(this.face);

        this.normal = normal;
        this.hover = hover;

        this.hoverRegion = new FillRegion(this.hover);
        this.hoverRegion.setOpacity(0);
        this.hoverRegion.prefWidthProperty().bind(this.widthProperty());
        this.hoverRegion.prefHeightProperty().bind(this.heightProperty());
        this.getChildren().addLast(this.hoverRegion); // stackpane child must be below the label

        this.transition = new FadeTransition();
        this.transition.setDuration(Duration.millis(200));
        this.transition.setNode(this.hoverRegion);
        this.setBackground(RiceHandler.createBG(normal, RADIUS, 0));
    }

    @Override
    public final void onMouseEntered(MouseEvent event) {
        if (this.isEnabled()) {
            this.transition.setFromValue(0);
            this.transition.setToValue(1);
            this.transition.playFromStart();
        }
    }

    @Override
    public final void onMouseExited(MouseEvent event) {
        if (this.isEnabled()) {
            this.transition.setFromValue(1);
            this.transition.setToValue(0);
            this.transition.playFromStart();
        }
    }

    private static final Color DISABLED_TXT = RiceHandler.getColour("grey");
    private static final Color DISABLED = RiceHandler.getColour("dullgrey");

    @Override
    public void setEnabled(final boolean enabled) {
        this.transition.stop();
        if (!enabled) {
            this.setBackground(RiceHandler.createBG(DISABLED, RADIUS, 0));
            this.face.setTextFill(DISABLED_TXT);
        } else {
            this.setBackground(RiceHandler.createBG(normal, RADIUS, 0));
            this.face.setTextFill(RiceHandler.getColour("white"));
        }
        super.setEnabled(enabled);
    }

    /**
     * A region that is responsible for displaying the colour fills.
     * They need to be a separate class in order for the FadeTransition
     * to be applied to them. It is not good to use FadeTransition on
     * the entire button... that's just lazy.
     */
    private class FillRegion extends Region {
        FillRegion(final Color colour) {
            this.setMouseTransparent(true);
            this.setBackground(RiceHandler.createBG(colour, RADIUS, 0));
        }
    }
}
