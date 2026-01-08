/*
   Copyright (C) 2026  Nicholas Siow <nxiao.dev@gmail.com>
*/

package code.frontend.foundation.custom;

import code.frontend.misc.Vals;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

public class CustomBox extends ResizableCanvas {
    // CORNER_OFFSET > CORNER_DEVIATION
    private double deviation = Vals.GraphicalUI.DEVIATION;
    private double cornerDeviation = Vals.GraphicalUI.CORNER_DEVIATION;
    private double cornerOffset = Vals.GraphicalUI.CORNER_OFFSET;

    private Coordinate[] cornerCoords;
    private QuadPathSaver saver;

    private double thickness;

    public CustomBox() {
        this.thickness = Vals.GraphicalUI.DRAW_THICKNESS;
    }

    public CustomBox(double thickness) {
        this.thickness = thickness;
    }

    public CustomBox(double thickness, double dev, double cornerDev, double cornerOffset) {
        this.thickness = thickness;
        this.deviation = (dev >= 0) ? dev : Vals.GraphicalUI.DEVIATION;
        this.cornerDeviation = (cornerDev >= 0) ? cornerDev : Vals.GraphicalUI.CORNER_DEVIATION;
        this.cornerOffset = (cornerOffset > 0) ? cornerOffset : Vals.GraphicalUI.CORNER_OFFSET;
        assert cornerOffset > cornerDeviation;
    }

    // TODO: FIX THIS SO THAT REDRAW WITH EXACT PREV PATH IS POSSIBLE
    // IDEA IS TO EXTEND gc class AND IMPLEMENT SOME KIND OF PATH LOGGER
    @Override
    protected void draw(GraphicsContext gc, boolean recompute) {
        gc.setLineWidth(this.thickness);
        gc.setLineCap(StrokeLineCap.ROUND);
        gc.setLineJoin(StrokeLineJoin.ROUND);
        if (!recompute && this.saver != null)
            restoreBoxPath(gc);
        else
            assembleBoxPath(gc);
        gc.stroke();
    }

    private void assembleBoxPath(GraphicsContext gc) {
        Coordinate[] fixed = getFixedCoords();
        gc.beginPath();
        gc.moveTo(fixed[0].x, fixed[0].y);
        this.saver = new QuadPathSaver(new Coordinate(fixed[0].x, fixed[0].y));
        for (int loop = 0; loop < 2; loop++) {
            Coordinate[] ctrl = getControlCoords();
            for (int i = 0; i < ctrl.length; i++) {
                int fixedIndex = (i == ctrl.length - 1) ? 0 : i + 1; // allows wrap-around
                gc.quadraticCurveTo(ctrl[i].x, ctrl[i].y, fixed[fixedIndex].x, fixed[fixedIndex].y);
                this.saver.logQuadPath(ctrl[i], fixed[fixedIndex]);
                // System.out.println("x: " + fixed[fixedIndex].x + "y: " + fixed[fixedIndex].y);
            }
        }
        gc.closePath();
    }

    private void restoreBoxPath(GraphicsContext gc) {
        this.saver.restorePath(gc);
    }

    /*
     * it's like:
     * 0---------------1
     * |               |
     * |               |
     * |               |
     * |               |
     * 3---------------2
     */
    public Coordinate[] getCornerCoords() {
        // if (this.cornerCoords != null) return this.cornerCoords;
        double width = getPaddedWidth();
        double height = getPaddedHeight();
        this.cornerCoords = new Coordinate[4];
        for (int i = 0; i < cornerCoords.length; i++) {
            // the reasoning for the code below is trivial
            // and is left as an exercise for the reader :3
            double xPos = getVertiPadding() + ((i == 1 || i == 2) ? width : 0);
            double yPos = getHorizPadding() + ((i == 2 || i == 3) ? height : 0);
            this.cornerCoords[i] = new Coordinate(xPos, yPos);
        }
        return this.cornerCoords;
    }

    /*
     * it's something like this:
     *
     *           __xy1_____
     *      o  /
     *        /
     *       /
     *      xy0
     *      |
     */
    private Coordinate[] getFixedCoords() {
        // if (this.fixedCoords != null) return this.fixedCoords;
        Coordinate[] corners = getCornerCoords();
        Coordinate[] fixedCoords = new Coordinate[8];
        double width = getPaddedWidth();
        double height = getPaddedHeight();
        double offset = (Math.min(width, height) * cornerOffset);
        // ew ugly code... but it's readable so sike... maybe ill prettify it later
        fixedCoords[0] = new Coordinate(corners[0].x, corners[0].y + offset);
        fixedCoords[1] = new Coordinate(corners[0].x + offset, corners[0].y);

        fixedCoords[2] = new Coordinate(corners[1].x - offset, corners[1].y);
        fixedCoords[3] = new Coordinate(corners[1].x, corners[1].y + offset);

        fixedCoords[4] = new Coordinate(corners[2].x, corners[2].y - offset);
        fixedCoords[5] = new Coordinate(corners[2].x - offset, corners[2].y);

        fixedCoords[6] = new Coordinate(corners[3].x + offset, corners[3].y);
        fixedCoords[7] = new Coordinate(corners[3].x, corners[3].y - offset);

        return fixedCoords;
    }

    /*
     * it be like dis:
     *
     * 0-------1-------2
     * |               |
     * |               |
     * |               |
     * 7               3
     * |               |
     * |               |
     * |               |
     * 6-------5-------4
     */
    private Coordinate[] getControlCoords() {
        getCornerCoords();
        Coordinate[] controlCoords = new Coordinate[8];
        double width = getPaddedWidth();
        double height = getPaddedHeight();
        // first add the corner ctrl points
        for (int i = 0; i < 4; i++) {
            int index = i * 2;
            Coordinate coord = this.cornerCoords[i];
            coord.setDeviations((Math.min(width, height) + thickness) * cornerDeviation);
            double x = this.cornerCoords[i].getVarX();
            double y = this.cornerCoords[i].getVarY();
            controlCoords[index] = new Coordinate(x, y);
        }
        // then add the side ctrl points
        double midHeight = height / 2;
        double midWidth = width / 2;
        double minX = this.cornerCoords[0].x;
        double minY = this.cornerCoords[0].y;
        double maxX = this.cornerCoords[2].x;
        double maxY = this.cornerCoords[2].y;

        controlCoords[1] = new Coordinate(minX + midWidth, minY);
        controlCoords[3] = new Coordinate(maxX, minY + midHeight);
        controlCoords[5] = new Coordinate(minX + midWidth, maxY);
        controlCoords[7] = new Coordinate(minX, minY + midHeight);

        int count = 0;
        for (int i = 1; i < controlCoords.length; i += 2) {
            boolean evenCount = count % 2 == 0;
            double xDeviation = ((((evenCount) ? width : height) + thickness) * deviation);
            double yDeviation = ((((evenCount) ? height : width) + thickness) * deviation);
            controlCoords[i].setDeviations(xDeviation, yDeviation);
            controlCoords[i].x = controlCoords[i].getVarX();
            controlCoords[i].y = controlCoords[i].getVarY();
            count++;
        }

        return controlCoords;
    }

    public double getHorizPadding() {
        return 0.5 * (this.getHeight() - getPaddedHeight());
    }

    public double getVertiPadding() {
        return 0.5 * (this.getWidth() - getPaddedWidth());
    }

    private double getPaddedHeight() {
        double height = this.getHeight();
        double bigDev = height * Math.max(deviation, cornerDeviation);
        return Math.floor(height - bigDev) - this.thickness;
    }

    private double getPaddedWidth() {
        double width = this.getWidth();
        double bigDev = width * Math.max(deviation, cornerDeviation);
        return Math.floor(width - bigDev) - this.thickness;
    }

    public void setCornerOffset(double cornerOffset) {
        this.cornerOffset = cornerOffset;
        this.resizeAndDraw(true);
    }

    public void setThickness(double thickness) {
        this.thickness = thickness;
        this.resizeAndDraw(true);
    }

    public void setDeviation(double deviation) {
        this.deviation = deviation;
        this.resizeAndDraw(true);
    }

    public void setCornerDeviation(double cornerDeviation) {
        this.cornerDeviation = cornerDeviation;
        this.resizeAndDraw(true);
    }
}
