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
 *
 * Using this class as an inner class would be the
 * preferred approach; bind the height and width of
 * this to the parent. This preserves the meaning of
 * classes without them all turning into Regions.
 */
public abstract class DragStartRegion<T> extends Region {
    public DragStartRegion() {
        this.setBackground(null);
        // init listener for drag starts
        this.setOnDragDetected((event) -> {
            RightClickMenu.despawnAll();
            this.startFullDrag();
            DragDropOverlay.spawnOverlay(this);
            onDragStart();
        });
        // init listener for drag stops
        this.setOnMouseReleased((event) -> {
            if (DragDropOverlay.isActive()) {
                DragDropOverlay.killOverlay();
                cleanupOnDragEnd();
            }
        });
    }

    /**
     * This will be called when a drag action starts. This method does not need to
     * do anything.
     */
    protected abstract void onDragStart();

    /**
     * This will be called be called whenever a drag
     * action ends. Note that this could mean a successful drag
     * or an unsuccessful drag. This method should be used to
     * revert any temporary stylings made to the UI componenet(s)
     * that were being dragged.
     */
    protected abstract void cleanupOnDragEnd();

    /**
     * Gets the data that is being transferred.
     */
    protected abstract T getData();

    /**
     * This is the preview that will be shown when this {@link DragStarter}
     * is being dragged across the screen. While null can be returned,
     * it is discouraged as users like to see what they are dragging.
     *
     * @return a Region which is a visual representation of the component
     * that is being dragged
     */
    protected abstract Region getRepresentation();
}
