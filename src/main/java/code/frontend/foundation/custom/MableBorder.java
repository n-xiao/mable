/*
 * Copyright (C) 2026  Nicholas Siow <nxiao.dev@gmail.com>
 */

package code.frontend.foundation.custom;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

public class MableBorder extends ResizableCanvas {
    private static final int EDGES = 4;
    private static final int PASSES = 3;

    private final Corner[][] CORNERS;
    private final CubicBezierData[][] CONNECTIONS;
    private double thickness;
    private double messiness;
    private double cornerRadii;

    public MableBorder(double thickness, double messiness, double cornerRadii) {
        this.CORNERS = new Corner[PASSES][EDGES];
        this.CONNECTIONS = new CubicBezierData[PASSES][EDGES];
        this.thickness = thickness;
        this.messiness = messiness;
        this.cornerRadii = cornerRadii;
    }

    @Override
    protected void draw(GraphicsContext gc, boolean recompute) {
        for (int p = 0; p < PASSES; p++) {
            if (recompute || !notNull((Object[]) this.CORNERS)
                || !notNull((Object[]) this.CONNECTIONS)) {
                this.CORNERS[p][0] = new Corner(this, p, 0);
                this.CORNERS[p][0].recompute();

                for (int i = 1; i < EDGES; i++) {
                    this.CORNERS[p][i] = new Corner(this, p, i);
                    this.CORNERS[p][i].recompute();

                    Corner prev = this.CORNERS[p][i - 1];
                    Coordinate[] ctrls = joinCorners(prev, this.CORNERS[p][i]);
                    this.CONNECTIONS[p][i - 1] = new CubicBezierData(ctrls[0], ctrls[1]);
                }

                Coordinate[] ctrls = joinCorners(this.CORNERS[p][EDGES - 1], this.CORNERS[p][0]);
                this.CONNECTIONS[p][EDGES - 1] = new CubicBezierData(ctrls[0], ctrls[1]);
            }

            gc.setLineWidth(this.thickness);
            gc.setLineCap(StrokeLineCap.ROUND);
            gc.setLineJoin(StrokeLineJoin.ROUND);

            gc.beginPath();

            for (int i = 0; i < EDGES - 1; i++) {
                this.CORNERS[p][i].draw(gc);
                CubicBezierData cbd = this.CONNECTIONS[p][i];
                if (p < 2)
                    gc.bezierCurveTo(cbd.c1().x, cbd.c1().y, cbd.c2().x, cbd.c2().y,
                        this.CORNERS[p][i + 1].start.x, this.CORNERS[p][i + 1].start.y);
            }

            this.CORNERS[p][EDGES - 1].draw(gc);
            CubicBezierData cbd = this.CONNECTIONS[p][EDGES - 1];
            if (p < 2)
                gc.bezierCurveTo(cbd.c1().x, cbd.c1().y, cbd.c2().x, cbd.c2().y,
                    this.CORNERS[p][0].start.x, this.CORNERS[p][0].start.y);

            gc.stroke();
            gc.closePath();
        }
    }

    private Coordinate[] joinCorners(Corner corner1, Corner corner2) {
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

    public void setMessiness(double messiness) {
        this.messiness = messiness;
        super.resizeAndDraw(true);
    }

    public void setThickness(double thickness) {
        this.thickness = thickness;
        super.resizeAndDraw(true);
    }

    public void setCornerRadii(double cornerRadii) {
        this.cornerRadii = cornerRadii;
        super.resizeAndDraw(true);
    }

    public double getPaddingDist() {
        return this.thickness * (this.messiness + 1);
    }
    private double getPaddedHeight() {
        return this.getHeight() - 2 * getPaddingDist();
    }

    private double getPaddedWidth() {
        return this.getWidth() - 2 * getPaddingDist();
    }

    protected static boolean notNull(Object... objects) {
        for (Object object : objects) {
            if (object == null)
                return false;
        }
        return true;
    }

    protected static double getRandom(double d1, double d2) {
        double diff = d2 - d1;
        return d1 + diff * Math.random();
    }

    protected static double plusMinus(double d, boolean b) {
        return b ? d : -d;
    }

    protected static double extrapolateCornerDeltaX(Corner corner, Coordinate coord) {
        return coord.x - plusMinus(corner.getCornerDelta(), corner.isRight());
    }

    protected static double extrapolateCornerDeltaY(Corner corner, Coordinate coord) {
        return coord.y - plusMinus(corner.getCornerDelta(), corner.isBottom());
    }

    private static class Corner {
        final MableBorder BORDER;
        final boolean IS_DECOR;
        final int INDEX; // used for bitmask

        Coordinate origin;
        Coordinate start;
        Coordinate end;
        ControlCoordinate ctrl;

        Corner(final MableBorder BORDER, int pass, int index) {
            this.BORDER = BORDER;
            this.IS_DECOR = Math.random() < 0.5 && pass > 1;
            this.INDEX = index;
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
            ox = this.isRight() ? BORDER.getWidth() - BORDER.getPaddingDist()
                                : BORDER.getPaddingDist();
            oy = this.isBottom() ? BORDER.getHeight() - BORDER.getPaddingDist()
                                 : BORDER.getPaddingDist();

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
            return Integer.bitCount(this.INDEX) == 1;
        }

        private boolean isBottom() {
            return Integer.highestOneBit(this.INDEX) == 2;
        }

        private double getCornerDelta() {
            double len = Math.min(this.BORDER.getPaddedWidth(), this.BORDER.getPaddedHeight());
            return Math.min(0.5 * len, 0.5 * this.getCornerRadii() * len);
        }

        private double getCtrlDelta() {
            return this.getMessiness() * (this.BORDER.thickness + this.getCornerDelta());
        }

        private double getMessiness() {
            return BORDER.messiness;
        }

        private double getCornerRadii() {
            return this.IS_DECOR ? this.BORDER.cornerRadii * getRandom(1.18, 1.25)
                                 : this.BORDER.cornerRadii;
        }
    }

    private static record CubicBezierData(Coordinate c1, Coordinate c2) {}
}
