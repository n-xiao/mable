package code.frontend.foundation;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;

import javax.swing.JPanel;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

public class CustomLine extends ResizableCanvas {

    private Coordinate start;
    private Coordinate end;
    private int thickness;
    private final double DEVIATION; // determined using thickness

    public CustomLine(Coordinate start, Coordinate end, int thickness) {
        assert start != null && end != null;
        this.start = start;
        this.end = end;
        this.thickness = thickness;
        DEVIATION = 0.5;
    }

    public CustomLine(Coordinate start, Coordinate end, int deviation, int thickness) {
        assert start != null && end != null && deviation >= 0;
        this.start = start;
        this.end = end;
        this.thickness = thickness;
        DEVIATION = deviation;
    }

    @Override
    protected void draw(GraphicsContext gc) {
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(this.thickness);
        gc.setLineCap(StrokeLineCap.ROUND);
        gc.setLineJoin(StrokeLineJoin.ROUND);

        double midX = (this.start.x + this.end.x) / 2;
        double midY = (this.start.y + this.end.y) / 2;
        Coordinate midCoord = new Coordinate(midX, midY);
        midCoord.setDeviations(DEVIATION * thickness);
        gc.beginPath();
        // straight line first
        gc.moveTo(start.x, start.y);
        gc.lineTo(end.x, end.y);
        // then the messy line
        gc.quadraticCurveTo(midCoord.getVarX(), midCoord.getVarY(), start.x, start.y);
        gc.stroke();
    }

    // protected void paintComponent(Graphics g) {
    //     Graphics2D g2d = (Graphics2D) g.create();
    //     g2d.setPaint(Color.WHITE); // for testing only, use variable later
    //     g2d.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    //     g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    //     double midX = (this.start.x + this.end.x) / 2;
    //     double midY = (this.start.y + this.end.y) / 2;
    //     Coordinate midCoord = new Coordinate(midX, midY);
    //     GeneralPath line1 = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 3);
    //     line1.moveTo(start.x, start.y);
    //     line1.quadTo(midCoord.x, midCoord.y, end.x, end.y);
    //     midCoord.setDeviations(0, DEVIATION * thickness);
    //     GeneralPath line2 = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 3);
    //     line2.moveTo(start.x, start.y);
    //     line2.quadTo(midCoord.getVarX(), midCoord.getVarY(), end.x, end.y);
    //     g2d.draw(line1);
    //     g2d.draw(line2);
    // }
}
