package code.frontend.foundation;

import code.frontend.misc.Vals;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

// dont use setPreferredSize() with layout null
public class CustomBox extends ResizableCanvas {
    // CORNER_OFFSET > CORNER_DEVIATION
    final private double DEVIATION;
    final private double CORNER_DEVIATION;
    final private double CORNER_OFFSET;

    private int thickness;
    private Coordinate[] cornerCoords = null;
    private Coordinate[] fixedCoords = null;

    public CustomBox(int thickness) {
        this.thickness = thickness;
        DEVIATION = Vals.GraphicalUI.DEVIATION;
        CORNER_DEVIATION = Vals.GraphicalUI.CORNER_DEVIATION;
        CORNER_OFFSET = Vals.GraphicalUI.CORNER_OFFSET;
    }

    public CustomBox(int thickness, double dev, double cornerDev, double cornerOffset) {
        this.thickness = thickness;
        DEVIATION = (dev >= 0) ? dev : Vals.GraphicalUI.DEVIATION;
        CORNER_DEVIATION = (cornerDev >= 0) ? cornerDev : Vals.GraphicalUI.CORNER_DEVIATION;
        CORNER_OFFSET = (cornerOffset >= 0) ? cornerOffset : Vals.GraphicalUI.CORNER_OFFSET;
        assert CORNER_OFFSET > CORNER_DEVIATION;
    }

    @Override
    protected void draw(GraphicsContext gc) {
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(this.thickness);
        gc.setLineCap(StrokeLineCap.ROUND);
        gc.setLineJoin(StrokeLineJoin.ROUND);
        assembleBoxPath(gc);
        assembleBoxPath(gc);
    }

    private void assembleBoxPath(GraphicsContext gc) {
        Coordinate[] fixed = getFixedCoords();
        Coordinate[] ctrl = getControlCoords();
        gc.beginPath();
        gc.moveTo(fixed[0].x, fixed[0].y);
        for (int i = 0; i < ctrl.length; i++) {
            int fixedIndex = (i == ctrl.length - 1) ? 0 : i + 1; // allows wrap-around
            gc.quadraticCurveTo(ctrl[i].x, ctrl[i].y, fixed[fixedIndex].x, fixed[fixedIndex].y);
            // System.out.println("x: " + fixed[fixedIndex].x + "y: " + fixed[fixedIndex].y);
        }
        gc.stroke();
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
        this.fixedCoords = new Coordinate[8];
        double width = getPaddedWidth();
        double height = getPaddedHeight();
        double offset = (Math.min(width, height) * CORNER_OFFSET);
        // ew ugly code... but it's readable so sike
        this.fixedCoords[0] = new Coordinate(corners[0].x, corners[0].y + offset);
        this.fixedCoords[1] = new Coordinate(corners[0].x + offset, corners[0].y);

        this.fixedCoords[2] = new Coordinate(corners[1].x - offset, corners[1].y);
        this.fixedCoords[3] = new Coordinate(corners[1].x, corners[1].y + offset);

        this.fixedCoords[4] = new Coordinate(corners[2].x, corners[2].y - offset);
        this.fixedCoords[5] = new Coordinate(corners[2].x - offset, corners[2].y);

        this.fixedCoords[6] = new Coordinate(corners[3].x + offset, corners[3].y);
        this.fixedCoords[7] = new Coordinate(corners[3].x, corners[3].y - offset);

        return this.fixedCoords;
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
            coord.setDeviations((Math.min(width, height) + thickness) * CORNER_DEVIATION);
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
        for (int i = 1; i < controlCoords.length; i+=2) {
            boolean evenCount = count % 2 == 0;
            double xDeviation = ((((evenCount) ? width : height) + thickness) * DEVIATION);
            double yDeviation = ((((evenCount) ? height : width) + thickness) * DEVIATION);
            controlCoords[i].setDeviations(xDeviation,yDeviation);
            controlCoords[i].x = controlCoords[i].getVarX();
            controlCoords[i].y = controlCoords[i].getVarY();
            count++;
        }

        return controlCoords;
    }

    private double getHorizPadding() {
        return 0.5 * (this.getHeight() - getPaddedHeight());
    }

    private double getVertiPadding() {
        return 0.5 * (this.getWidth() - getPaddedWidth());
    }

    private double getPaddedHeight() {
        double height = this.getHeight();
        double bigDev = height * Math.max(DEVIATION, CORNER_DEVIATION);
        return Math.floor(height - bigDev) - this.thickness;
    }

    private double getPaddedWidth() {
        double width = this.getWidth();
        double bigDev = width * Math.max(DEVIATION, CORNER_DEVIATION);
        return Math.floor(width - bigDev) - this.thickness;
    }
}

