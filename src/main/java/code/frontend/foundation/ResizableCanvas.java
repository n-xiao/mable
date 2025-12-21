package code.frontend.foundation;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public abstract class ResizableCanvas extends Canvas {

    public ResizableCanvas() {
        widthProperty().addListener(event -> resizeAndDraw());
        heightProperty().addListener(event -> resizeAndDraw());
    }

    private void resizeAndDraw() {
        double width = this.getWidth();
        double height = this.getHeight();
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, width, height);
        draw(gc);
    }

    protected abstract void draw(GraphicsContext gc);

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
