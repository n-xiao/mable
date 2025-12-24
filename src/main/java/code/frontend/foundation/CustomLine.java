package code.frontend.foundation;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

public class CustomLine extends ResizableCanvas
{
    public enum Type
    {
        VERTICAL_TYPE,
        HORIZONTAL_TYPE
    }

    private Coordinate start;
    private Coordinate end;
    private int thickness;
    private Type lineType;
    private double startPadding;
    private double endPadding;
    private final double DEVIATION; // determined using thickness

    public CustomLine(int thickness, Type type)
    {
        this.DEVIATION = 0.5;
        this.thickness = thickness;
        this.lineType = type;
        this.startPadding = 0;
        this.endPadding = 0;
    }

    @Override
    protected void draw(GraphicsContext gc)
    {
        gc.setLineWidth(this.thickness);
        gc.setLineCap(StrokeLineCap.ROUND);
        gc.setLineJoin(StrokeLineJoin.ROUND);

        double width = this.getWidth();
        double height = this.getHeight();

        switch (this.lineType)
            {
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
        Coordinate midCoord = new Coordinate(midX, midY);
        midCoord.setDeviations(DEVIATION * thickness);
        gc.beginPath();
        // adds straight line first
        gc.moveTo(start.x, start.y);
        gc.lineTo(end.x, end.y);
        // adds the messy line
        gc.quadraticCurveTo(midCoord.getVarX(), midCoord.getVarY(), start.x, start.y);
        gc.stroke();
    }

    public void setStartPadding(double startPadding)
    {
        this.startPadding = startPadding;
        resizeAndDraw();
    }

    public void setEndPadding(double endPadding)
    {
        this.endPadding = endPadding;
        resizeAndDraw();
    }

    /*
     * Convenience method
     */
    public void setPadding(double startEndPadding)
    {
        this.startPadding = startEndPadding;
        this.endPadding = startEndPadding;
        resizeAndDraw();
    }
}
