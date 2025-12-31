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

package code.frontend.foundation;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

public class CustomLine extends ResizableCanvas {
    public enum Type { VERTICAL_TYPE, HORIZONTAL_TYPE }

    private Coordinate start;
    private Coordinate end;
    private Coordinate mid;
    private Coordinate midVar;
    private double thickness;
    private Type lineType;
    private double startPadding;
    private double endPadding;
    private final double DEVIATION; // determined using thickness

    public CustomLine(double thickness, Type type) {
        this.DEVIATION = 0.5;
        this.thickness = thickness;
        this.lineType = type;
        this.startPadding = 0;
        this.endPadding = 0;
    }

    @Override
    protected void draw(GraphicsContext gc, boolean recompute) {
        gc.setLineWidth(this.thickness);
        gc.setLineCap(StrokeLineCap.ROUND);
        gc.setLineJoin(StrokeLineJoin.ROUND);

        double width = this.getWidth();
        double height = this.getHeight();

        switch (this.lineType) {
            case VERTICAL_TYPE:
                this.start = new Coordinate(width / 2, startPadding);
                this.end = new Coordinate(width / 2, height - endPadding);
                break;
            case HORIZONTAL_TYPE:
                this.start = new Coordinate(startPadding, height / 2);
                this.end = new Coordinate(width - endPadding, height / 2);
                break;
            default:
                throw new IllegalArgumentException("Invalid CustomLine Type was encountered.");
        }

        double midX = (this.start.x + this.end.x) / 2;
        double midY = (this.start.y + this.end.y) / 2;
        this.mid = new Coordinate(midX, midY);
        mid.setDeviations(DEVIATION * thickness);
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

    public void setStartPadding(double startPadding) {
        this.startPadding = startPadding;
        resizeAndDraw(true);
    }

    public void setEndPadding(double endPadding) {
        this.endPadding = endPadding;
        resizeAndDraw(true);
    }

    /*
     * Convenience method
     */
    public void setPadding(double startEndPadding) {
        this.startPadding = startEndPadding;
        this.endPadding = startEndPadding;
        resizeAndDraw(true);
    }
}
