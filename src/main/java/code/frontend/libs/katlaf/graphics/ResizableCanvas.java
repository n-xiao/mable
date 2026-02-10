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

import code.frontend.libs.katlaf.ricing.RiceHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * A {@link Canvas} that is resizeable. This is the base class for creating custom
 * graphics like MableBorder. When the size of an instance is modified,
 * the graphics on the canvas will be cleared and redrawn. How the graphics is
 * determined by implementations of this class.
 * <p>
 * The parent node of a ResizableCanvas is responsible for setting its width and height.
 *
 * @since 3.0.0-beta
 */
abstract class ResizableCanvas extends Canvas {
    private Color strokeColour = RiceHandler.getColour("white"); // default colour

    public ResizableCanvas() {
        widthProperty().addListener(event -> resizeAndDraw(true));
        heightProperty().addListener(event -> resizeAndDraw(true));
    }

    /*


     BEHAVIOUR
    -------------------------------------------------------------------------------------*/

    /**
     * This method sets image smoothening to true, clears the current graphics
     * and redraws the graphics with the current stroke colour. The provided boolean
     * in the parameter is passed to the draw method.
     *
     * @see ResizableCanvas#draw(GraphicsContext, boolean)
     *
     * @since 3.0.0-beta
     */
    final void resizeAndDraw(final boolean recompute) {
        final double width = this.getWidth();
        final double height = this.getHeight();
        if (width > 0 && height > 0) {
            final GraphicsContext gc = getGraphicsContext2D();
            gc.setImageSmoothing(true);
            gc.clearRect(0, 0, width, height);
            gc.setStroke(this.strokeColour);
            draw(gc, recompute);
        }
    }

    /*


     PROTECTED API
    -------------------------------------------------------------------------------------*/

    /**
     * Defines how the graphics are drawn. The GraphicsContext is passed
     * from the super class to implementations. The recompute parameter can
     * be ignored, but implementations should utilise it when implementing
     * efficient graphics.
     *
     * @param recompute      whether the implementation should perform a recalculation
     *                       when drawing its graphics.
     * @param gc             the GraphicsContext which drawing operations should be done on
     *
     * @since 3.0.0-beta
     */
    protected abstract void draw(final GraphicsContext gc, final boolean recompute);

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    /**
     * Sets the stroke colour and attempts to redraw this ResizableCanvas without
     * requesting a recomputation of graphics.
     *
     * @param strokeColour      the colour that the graphics should be changed to
     */
    public final void setStrokeColour(final Color strokeColour) {
        this.strokeColour = strokeColour;
        resizeAndDraw(false);
    }

    /**
     * Gets the current stroke colour.
     *
     * @returns     the stroke colour.
     */
    public final Color getStrokeColour() {
        return strokeColour;
    }

    /*


     IMPLEMENTATIONS
    -------------------------------------------------------------------------------------*/

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double prefWidth(double height) {
        return getWidth();
    }

    @Override
    public double prefHeight(double width) {
        return getHeight();
    }
}
