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
 * This is the {@link ResizableCanvas} which implements custom border
 * graphics. A MableBorder is comprised, internally, of
 * four quadratic bezier curves that make up each corner and four
 * cubic bezier curves that make up its four sides.
 * <p>
 * There is a more in-depth explanation in the developer documentation.
 * Please read it.
 *
 * @since v2.1.0-beta
 */
public final class MableBorder extends ResizableCanvas {
    private static final int EDGES = 4;
    private static final int PASSES = 3;

    private final Corner[][] corners;
    private final CubicBezierData[][] connections;
    private double thickness;
    private double messiness;
    private double cornerRadii;

    /**
     * Creates a new MableBorder.
     *
     * @param thickness     specifies the thickness of the border
     * @param messiness     specifies how messy the border is.
     *
     */
    public MableBorder(final double thickness, final double messiness, final double cornerRadii) {
        this.corners = new Corner[PASSES][EDGES];
        this.connections = new CubicBezierData[PASSES][EDGES];
        this.thickness = thickness;
        this.messiness = messiness;
        this.cornerRadii = cornerRadii;
        this.setMouseTransparent(true);
    }

    /*


     PROTECTED
    -------------------------------------------------------------------------------------*/

    /**
     * Draws the border using the provided canvas. The previous calculation is stored in
     * an array during runtime so that, when recompute is false, the border can be redrawn
     * without any recalculations. This means that the user should not see any changes to
     * the shape of the path. Useful when changing anything besides the size of this
     * MableBorder, like its colour.
     */
    @Override
    protected void draw(final GraphicsContext gc, final boolean recompute) {
        for (int p = 0; p < PASSES; p++) {
            if (recompute || !notNull((Object[]) this.corners)
                || !notNull((Object[]) this.connections)) {
                this.corners[p][0] = new Corner(this, p, 0);
                this.corners[p][0].recompute();

                for (int i = 1; i < EDGES; i++) {
                    this.corners[p][i] = new Corner(this, p, i);
                    this.corners[p][i].recompute();

                    Corner prev = this.corners[p][i - 1];
                    Coordinate[] ctrls = joinCorners(prev, this.corners[p][i]);
                    this.connections[p][i - 1] = new CubicBezierData(ctrls[0], ctrls[1]);
                }

                Coordinate[] ctrls = joinCorners(this.corners[p][EDGES - 1], this.corners[p][0]);
                this.connections[p][EDGES - 1] = new CubicBezierData(ctrls[0], ctrls[1]);
            }

            gc.setLineWidth(this.thickness);
            gc.setLineCap(StrokeLineCap.ROUND);
            gc.setLineJoin(StrokeLineJoin.ROUND);

            gc.beginPath();

            for (int i = 0; i < EDGES - 1; i++) {
                this.corners[p][i].draw(gc);
                CubicBezierData cbd = this.connections[p][i];
                if (p < 2)
                    gc.bezierCurveTo(cbd.c1().x, cbd.c1().y, cbd.c2().x, cbd.c2().y,
                        this.corners[p][i + 1].start.x, this.corners[p][i + 1].start.y);
            }

            this.corners[p][EDGES - 1].draw(gc);
            CubicBezierData cbd = this.connections[p][EDGES - 1];
            if (p < 2)
                gc.bezierCurveTo(cbd.c1().x, cbd.c1().y, cbd.c2().x, cbd.c2().y,
                    this.corners[p][0].start.x, this.corners[p][0].start.y);

            gc.stroke();
            gc.closePath();
        }
    }

    /*


     PRIVATE
    -------------------------------------------------------------------------------------*/

    /**
     * Connects one Corner with another Corner by computing and drawing a cubic
     * bezier curve from the end (or start) of one corner to the start (or end)
     * of the other corner. The cubic bezier curve takes the messiness of each
     * corner into account.
     * <p>
     * This ensures that the shape of the MableBorder remains consistent, as
     * the sides are directly affected by the corners.
     *
     * @param corner1       the corner that should be drawn from
     * @param corner2       the corner that should be drawn to
     */
    private Coordinate[] joinCorners(final Corner corner1, final Corner corner2) {
        final boolean H_MATCH = !(corner1.isBottom() ^ corner2.isBottom());
        final boolean V_MATCH = !(corner1.isRight() ^ corner2.isRight());
        if (!(H_MATCH ^ V_MATCH))
            throw new IllegalArgumentException("Attempted to create a weird rectangle.");

        double cx1, cy1, cx2, cy2;
        if (H_MATCH) {
            cx1 = getRandom(corner1.end.x, extrapolateCornerDeltaX(corner1, corner1.end));
            cy1 = corner1.ctrl.getXmodel().getYfromX(cx1);
            cx2 = getRandom(corner2.start.x, extrapolateCornerDeltaX(corner2, corner2.start));
            cy2 = corner2.ctrl.getXmodel().getYfromX(cx2);
        } else {
            cy1 = getRandom(corner1.end.y, extrapolateCornerDeltaY(corner1, corner1.end));
            cx1 = corner1.ctrl.getYmodel().getXfromY(cy1);
            cy2 = getRandom(corner2.start.y, extrapolateCornerDeltaY(corner2, corner2.start));
            cx2 = corner2.ctrl.getYmodel().getXfromY(cy2);
        }

        Coordinate[] coords = {new Coordinate(cx1, cy1), new Coordinate(cx2, cy2)};
        return coords;
    }

    private double getPaddedHeight() {
        return this.getHeight() - 2 * getPaddingDist();
    }

    private double getPaddedWidth() {
        return this.getWidth() - 2 * getPaddingDist();
    }

    private static boolean notNull(Object... objects) {
        for (Object object : objects) {
            if (object == null)
                return false;
        }
        return true;
    }

    private static double getRandom(double d1, double d2) {
        double diff = d2 - d1;
        return d1 + diff * Math.random();
    }

    private static double plusMinus(double d, boolean b) {
        return b ? d : -d;
    }

    private static double extrapolateCornerDeltaX(Corner corner, Coordinate coord) {
        return coord.x - plusMinus(corner.getCornerDelta(), corner.isRight());
    }

    private static double extrapolateCornerDeltaY(Corner corner, Coordinate coord) {
        return coord.y - plusMinus(corner.getCornerDelta(), corner.isBottom());
    }

    /*


     PUBLIC
    -------------------------------------------------------------------------------------*/

    /**
     * Sets the messiness of this MableBorder. Note that the messiness
     * is multiplied by the stroke width of this MableBorder to calculate how
     * much a corner should deviate by.
     * <p>
     * This method will redraw and recompute this MableBorder.
     */
    public void setMessiness(double messiness) {
        this.messiness = messiness;
        super.resizeAndDraw(true);
    }

    public void setThickness(double thickness) {
        this.thickness = thickness;
        super.resizeAndDraw(true);
    }

    public double getThickness() {
        return thickness;
    }

    /**
     * Determines how much a MableBorder's corners are curved. This method will
     * redraw and recompute this MableBorder.
     *
     * @param cornerRadii   a value from 0.0 to 1.0, inclusively. Any value
     *                      greater than 1.0 will be ignored. Any value less
     *                      than 0.0 will produce undefined behaviour.
     */
    public void setCornerRadii(double cornerRadii) {
        this.cornerRadii = cornerRadii;
        super.resizeAndDraw(true);
    }

    public double getCornerRadii() {
        return cornerRadii;
    }

    public double getPaddingDist() {
        return this.thickness * (this.messiness + 1);
    }

    /*


     COMPOSITIONS
    -------------------------------------------------------------------------------------*/

    private static class Corner {
        final MableBorder border;
        final boolean isDecor;
        final int index; // used for bitmask

        Coordinate origin;
        Coordinate start;
        Coordinate end;
        ControlCoordinate ctrl;

        Corner(final MableBorder BORDER, int pass, int index) {
            this.border = BORDER;
            this.isDecor = Math.random() < 0.5 && pass > 1;
            this.index = index;
        }

        void draw(GraphicsContext gc) {
            if (!notNull(this.origin, this.start, this.end, this.ctrl))
                recompute();
            gc.moveTo(this.start.x, this.start.y);
            gc.quadraticCurveTo(this.ctrl.x, this.ctrl.y, this.end.x, this.end.y);
        }

        void recompute() {
            this.origin = this.newOrigin();
            this.start = this.newStart();
            this.end = this.newEnd();
            this.ctrl = this.newControlCoord();
        }

        private Coordinate newOrigin() {
            double ox, oy;
            ox = this.isRight() ? border.getWidth() - border.getPaddingDist()
                                : border.getPaddingDist();
            oy = this.isBottom() ? border.getHeight() - border.getPaddingDist()
                                 : border.getPaddingDist();

            return new Coordinate(ox, oy);
        }

        private Coordinate newStart() {
            double delta = this.getCornerDelta();

            delta *= this.isRight() ? -1 : 1; // delta is negative if origin is on rhs

            double sx, sy;
            sx = this.isRight() ^ this.isBottom() ? this.origin.x + delta : this.origin.x;
            sy = !(this.isRight() ^ this.isBottom()) ? this.origin.y + delta : this.origin.y;

            return new Coordinate(sx, sy);
        }

        private Coordinate newEnd() {
            double delta = this.getCornerDelta();

            delta *= this.isBottom() ? -1 : 1; // delta is negative if origin is at bottom

            double ex, ey;
            ex = !(this.isRight() ^ this.isBottom()) ? this.origin.x + delta : this.origin.x;
            ey = this.isRight() ^ this.isBottom() ? this.origin.y + delta : this.origin.y;

            return new Coordinate(ex, ey);
        }
        private ControlCoordinate newControlCoord() {
            double delta = this.getCtrlDelta();

            double x = this.origin.x - getRandom(0, plusMinus(delta, this.isRight()));
            double y = this.origin.y - getRandom(plusMinus(delta, this.isBottom()), 0);

            Coordinate xCoord = this.isRight() ^ this.isBottom() ? this.start : this.end;
            Coordinate yCoord = !(this.isRight() ^ this.isBottom()) ? this.start : this.end;

            return new ControlCoordinate(x, y, xCoord, yCoord);
        }

        private boolean isRight() {
            return Integer.bitCount(this.index) == 1;
        }

        private boolean isBottom() {
            return Integer.highestOneBit(this.index) == 2;
        }

        private double getCornerDelta() {
            double len = Math.min(this.border.getPaddedWidth(), this.border.getPaddedHeight());
            return Math.min(0.5 * len, 0.5 * this.getCornerRadii() * len);
        }

        private double getCtrlDelta() {
            return this.getMessiness() * (this.border.thickness + this.getCornerDelta());
        }

        private double getMessiness() {
            return border.messiness;
        }

        private double getCornerRadii() {
            return this.isDecor ? this.border.cornerRadii * getRandom(1.05, 1.2)
                                : this.border.cornerRadii;
        }
    }

    private static record CubicBezierData(Coordinate c1, Coordinate c2) {}
}
