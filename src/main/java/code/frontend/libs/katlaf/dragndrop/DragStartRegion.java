/*
    Copyright (C) 2026 Nicholas Siow <nxiao.dev@gmail.com>
    DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package code.frontend.libs.katlaf.dragndrop;

import code.frontend.libs.katlaf.menus.RightClickMenu;
import javafx.scene.layout.Region;

/**
 * A wrapper class which provides a given Region
 * with drag (starting) functionality. The generic
 * type should be the backend component that this
 * drag and drop operation is "transferring".
 */
public abstract class DragStartRegion<T> extends Region implements DragStarter<T> {
    public DragStartRegion() {
        this.setBackground(null);
        this.setOnDragDetected((event) -> {
            RightClickMenu.despawnAll();
            this.startFullDrag();
            DragDropOverlay.spawnOverlay(this);
            onDragStart();
        });
    }
}
