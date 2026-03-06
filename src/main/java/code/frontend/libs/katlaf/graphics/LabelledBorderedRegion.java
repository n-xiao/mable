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

package code.frontend.libs.katlaf.graphics;

import code.frontend.libs.katlaf.FontHandler;
import code.frontend.libs.katlaf.FontHandler.DedicatedFont;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

/**
 * This is a labelled version of a BorderedRegion which is composed of a MableBorder.
 * The Label can either be located at the top left corner of the Region, or at the
 * bottom right of the Region, depending on how the component should be presented
 * to the user. Note that all elements of this region share the same colour.
 *
 * @see MableBorder
 * @since v3.0.0-beta
 */
public class LabelledBorderedRegion extends BorderedRegion {
    private final Label label;
    private Loc location;

    /**
     * Creates a new instance of this class.
     *
     * @param mableBorder   the MableBorder that should be used for this LabelledBorderedRegion
     * @param text          the text that should be displayed by the Label
     * @param bg            the background colour of the label. Non-null values are highly
     *                      encouraged, and should match the background colour of its parent
     *                      node's background, since the background hides part of the MableBorder's
     *                      stroke.
     */
    public LabelledBorderedRegion(
        final MableBorder mableBorder, final String text, final Color bg) {
        super(mableBorder);
        final BackgroundFill fill = new BackgroundFill(bg, null, new Insets(0, -3, 0, -3));
        this.label = new Label(text);
        this.label.setBackground(new Background(fill));
        this.label.setTextFill(RiceHandler.getColour("white"));
        this.label.setFont(FontHandler.getDedicated(DedicatedFont.BORDER));
        this.label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        this.label.setAlignment(Pos.TOP_CENTER);
        this.label.setViewOrder(-0.1); // above MableBorders
        // add listeners
        this.widthProperty().addListener((observable, oldValue, newValue) -> positionLabel());
        this.heightProperty().addListener((observable, oldValue, newValue) -> positionLabel());

        this.getChildren().add(this.label);

        this.location = Loc.TOP_LEFT;
    }

    /*


     PRIVATE API
    -------------------------------------------------------------------------------------*/

    private void positionLabel() {
        final double width = this.label.prefWidth(-1);
        final double height = this.label.prefHeight(-1);
        if (width <= 0 || height <= 0)
            return;

        double x;
        double y;
        switch (this.location) {
            case TOP_LEFT:
                x = 15;
                y = this.getCustomBorder().getPaddingDist()
                    - 0.5 * (Math.abs(height - this.getCustomBorder().getThickness()));
                this.label.relocate(x, y);
                break;
            default:
                x = this.getCustomBorder().getWidth() - this.getCustomBorder().getPaddingDist()
                    - width - 15;
                y = this.getCustomBorder().getHeight() - this.getCustomBorder().getPaddingDist()
                    - 0.5 * (Math.abs(height - this.getCustomBorder().getThickness()));
                this.label.relocate(x, 0);
                this.label.setTranslateY(y);
                break;
        }
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    /**
     * Used to specify the location of the label for this LabelledBorderedRegion
     */
    public enum Loc { TOP_LEFT, BOTTOM_RIGHT }

    /**
     * Sets the colour of the MableBorder and Label of this LabelledBorderedRegion.
     * The default colour is white (specified by the theme in use).
     *
     * @param colour    the colour that this component should use
     */
    public final void setColour(final Color colour) {
        this.getCustomBorder().setColour(colour);
        this.label.setTextFill(colour);
    }

    public void setLocation(Loc location) {
        this.location = location;
        this.positionLabel();
    }
}
