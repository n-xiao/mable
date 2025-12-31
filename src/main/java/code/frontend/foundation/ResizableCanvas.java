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

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * Allows drawings on a Canvas to be resized if necessary.
 * However, resizing should be kept to a minimum because
 * it causes many re-drawings to occur, causing UI lag.
 */
public abstract class ResizableCanvas extends Canvas {
    private Color strokeColour = Color.WHITE;

    public ResizableCanvas() {
        widthProperty().addListener(event -> resizeAndDraw(true));
        heightProperty().addListener(event -> resizeAndDraw(true));
    }

    protected void resizeAndDraw(boolean recompute) {
        double width = this.getWidth();
        double height = this.getHeight();
        if (width > 0 && height > 0) {
            GraphicsContext gc = getGraphicsContext2D();
            gc.clearRect(0, 0, width, height);
            gc.setStroke(this.strokeColour);
            draw(gc, recompute);
        }
        this.setManaged(false); // this stops layouts from messing with it
    }

    protected abstract void draw(GraphicsContext gc, boolean recompute);

    public static Pane applyCustomBorder(Pane p, ResizableCanvas canvas) {
        p.getChildren().add(canvas);
        canvas.widthProperty().bind(p.widthProperty());
        canvas.heightProperty().bind(p.heightProperty());
        return p;
    }

    public void setStrokeColour(Color strokeColour) {
        this.strokeColour = strokeColour;
        resizeAndDraw(false);
    }

    public Color getStrokeColour() {
        return strokeColour;
    }

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
