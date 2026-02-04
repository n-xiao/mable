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

import javafx.scene.layout.Region;

/**
 * An abstract representation of a UI component that can be dragged (and dropped)
 * by the user.
 */
public interface DragStarter<T extends Comparable<?>> {
    /**
     * This will be called when a drag action starts. This method does not need to
     * do anything.
     */
    void onDragStart();

    /**
     * This will be called be called whenever a drag
     * action ends. Note that this could mean a successful drag
     * or an unsuccessful drag. This method should be used to
     * revert any temporary stylings made to the UI componenet(s)
     * that were being dragged.
     */
    void onDragEnd();

    /**
     * Gets the data that is being transferred.
     */
    T getData();

    /**
     * This is the preview that will be shown when this {@link DragStarter}
     * is being dragged across the screen. While null can be returned,
     * it is discouraged as users like to see what they are dragging.
     *
     * @return a Region which is a visual representation of the component
     * that is being dragged
     */
    Region getRepresentation();
}
