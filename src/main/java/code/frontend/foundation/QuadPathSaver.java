/*
   Copyright (C) 2026  Nicholas Siow <nxiao.dev@gmail.com>
*/

package code.frontend.foundation;

import java.util.LinkedList;
import javafx.scene.canvas.GraphicsContext;

public class QuadPathSaver {
    private record QuadPath(Coordinate control, Coordinate end) {}

    private Coordinate start;
    private LinkedList<QuadPath> path;

    public QuadPathSaver(Coordinate start) {
        this.start = start;
        this.path = new LinkedList<QuadPath>();
    }

    public void logQuadPath(Coordinate control, Coordinate end) {
        this.path.add(new QuadPath(control, end));
    }

    public void restorePath(GraphicsContext gc) {
        gc.beginPath();
        gc.moveTo(start.x, start.y);
        for (QuadPath quadPath : this.path) {
            Coordinate control = quadPath.control();
            Coordinate end = quadPath.end();
            gc.quadraticCurveTo(control.x, control.y, end.x, end.y);
        }
    }

    public boolean restoreReady() {
        return !this.path.isEmpty();
    }
}
