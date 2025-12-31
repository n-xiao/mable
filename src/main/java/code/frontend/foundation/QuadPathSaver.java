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
