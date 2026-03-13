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

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

/**
 * This is the {@link ResizableCanvas} that implements a messy, horizontal or
 * vertical line. A CustomLine is made up of one straight line and one
 * quadratic bezier curve. The quadratic bezier curve is responsible for
 * producing the appearance of a line with varying thickness.
 * <p>
 * When a CustomLine has more canvas space than what its line width requires,
 * it will either be vertically centered or horizontally centered.
 *
 * @since v1.0.1
 */
public final class CustomLine extends ResizableCanvas {
    public enum Type { VERTICAL, HORIZONTAL }

    private Coordinate start;
    private Coordinate end;
    private Coordinate mid;
    private Coordinate midVar;
    private double thickness;
    private final Type lineType;
    private double startPadding;
    private double endPadding;
    private final double messiness; // determined using thickness

    public CustomLine(double thickness, Type type) {
        this.messiness = 0.5;
        this.thickness = thickness;
        this.lineType = type;
        this.startPadding = 0;
        this.endPadding = 0;
    }

    /**
     * Draws a horizontal or vertical line. The messiness of the line is 0.5
     * by default. This particular implementation of custom graphics does not
     * implement recomputations.
     *
     * @see ResizableCanvas
     */
    @Override
    protected void draw(GraphicsContext gc, boolean recompute) {
        gc.setLineWidth(this.thickness);
        gc.setLineCap(StrokeLineCap.ROUND);
        gc.setLineJoin(StrokeLineJoin.ROUND);

        double width = this.getWidth();
        double height = this.getHeight();

        switch (this.lineType) {
            case VERTICAL:
                this.start = new Coordinate(width / 2, startPadding);
                this.end = new Coordinate(width / 2, height - endPadding);
                break;
            case HORIZONTAL:
                this.start = new Coordinate(startPadding, height / 2);
                this.end = new Coordinate(width - endPadding, height / 2);
                break;
            default:
                throw new IllegalArgumentException("Invalid CustomLine Type was encountered.");
        }

        double midX = (this.start.x + this.end.x) / 2;
        double midY = (this.start.y + this.end.y) / 2;
        this.mid = new Coordinate(midX, midY);
        mid.setDeviations(messiness * thickness);
        gc.beginPath();
        // adds straight line first
        gc.moveTo(start.x, start.y);
        gc.lineTo(end.x, end.y);
        // adds the messy line
        double midVarX = mid.getVarX();
        double midVarY = mid.getVarY();
        if (recompute || this.midVar == null)
            this.midVar = new Coordinate(midVarX, midVarY);
        gc.quadraticCurveTo(midVar.x, midVar.y, start.x, start.y);
        gc.stroke();
    }

    /**
     * Sets how much empty space there should be before the beginning of the line
     * and the actual start of the canvas. The default value is 0.
     *
     * @param startPadding  the amount of empty space before the start of this line.
     */
    public void setStartPadding(double startPadding) {
        this.startPadding = startPadding;
        resizeAndDraw(true);
    }

    /**
     * Sets how much empty space there should be after the end of the line and
     * the actual end of the canvas. The default value is 0.
     *
     * @param startPadding  the amount of empty space after the end of this line.
     */
    public void setEndPadding(double endPadding) {
        this.endPadding = endPadding;
        resizeAndDraw(true);
    }

    /*
     * Convenience method which adds the same padding to both the start and end
     * of this line.
     */
    public void setPadding(double startEndPadding) {
        this.startPadding = startEndPadding;
        this.endPadding = startEndPadding;
        resizeAndDraw(true);
    }
}
