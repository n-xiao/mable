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

package code.frontend.libs.katlaf.ricing;

import javafx.scene.paint.Color;

/**
 * A wrapper class for JavaFX's Color class. Adds more methods and also
 * ensures some spelling consistency throughout the code.
 *
 * TODO: fully migrate all other classes from Color to Colour
 *
 * @since v3.0.0-beta
 */
public final class Colour {
    private final Color color;

    /*


     CONSTRUCTORS
    -------------------------------------------------------------------------------------*/

    public Colour(final String str) {
        this(Color.web(str));
    }

    public Colour(final int r, final int g, final int b, final int a) {
        this(Color.rgb(r, g, b, a));
    }

    public Colour(Color colour) {
        this.color = colour;
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    /**
     * Returns the Color contained within this instance.
     */
    public Color get() {
        return this.color;
    }

    /**
     * If two colours has the same red, green, blue and alpha values,
     * they are considered the same. Applying this method would return true.
     * <p>
     * Note that this method utilises the toString() method.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Colour colour) {
            return colour.toString().equals(this.toString());
        }
        return false;
    }

    /**
     * Returns a String representing the web equivalent of this colour (rgba format).
     * For example, fully opaque black would return "rgb(0, 0, 0, 1)"
     *
     * @return String       an rgba String representation
     */
    @Override
    public String toString() {
        final int red = (int) (this.color.getRed() * 255);
        final int green = (int) (this.color.getGreen() * 255);
        final int blue = (int) (this.color.getBlue() * 255);
        final double alpha = this.color.getOpacity();
        return "rgba(" + red + ", " + green + ", " + blue + ", " + alpha + ")";
    }
}
