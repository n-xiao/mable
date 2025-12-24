package code.frontend.foundation;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * Allows drawings on a Canvas to be resized if necessary.
 * However, resizing should be kept to a minimum because
 * it causes many re-drawings to occur, causing UI lag.
 */
public abstract class ResizableCanvas extends Canvas
{

    private Color strokeColour = Color.WHITE;

    public ResizableCanvas()
    {
        widthProperty().addListener(event -> resizeAndDraw());
        heightProperty().addListener(event -> resizeAndDraw());
    }

    protected void resizeAndDraw()
    {
        double width = this.getWidth();
        double height = this.getHeight();
        if (width > 0 && height > 0)
            {
                GraphicsContext gc = getGraphicsContext2D();
                gc.clearRect(0, 0, width, height);
                gc.setStroke(this.strokeColour);
                draw(gc);
            }
        this.setManaged(false); // this stops layouts from messing with it
    }

    protected abstract void draw(GraphicsContext gc);

    public static Pane applyToPane(Pane p, ResizableCanvas canvas)
    {
        p.getChildren().add(canvas);
        canvas.widthProperty().bind(p.widthProperty());
        canvas.heightProperty().bind(p.heightProperty());
        return p;
    }

    public void setStrokeColour(Color strokeColour)
    {
        this.strokeColour = strokeColour;
        resizeAndDraw();
    }

    @Override
    public boolean isResizable()
    {
        return true;
    }

    @Override
    public double prefWidth(double height)
    {
        return getWidth();
    }

    @Override
    public double prefHeight(double width)
    {
        return getHeight();
    }

}
