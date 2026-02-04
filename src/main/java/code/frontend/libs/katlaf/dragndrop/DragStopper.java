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

import code.frontend.libs.katlaf.lists.Listable;
import javafx.scene.input.MouseDragEvent;

public interface DragStopper {
    /**
     * This method is called when the mouse is released
     * whilst hovering over this {@link DragStopper},
     * finishing the drag process. This method is
     * a convenience method, and could be implemented
     * to do nothing.
     */
    void onDragStop(MouseDragEvent event);

    /**
     * This method is called whenever the mouse, while
     * dragging with an active {@link DragDropOverlay},
     * enters this {@link DragStopper}. This is a
     * convenience method which can be used to
     * implement special highlighting or styling to
     * provide visual indicators.
     *
     * The MouseDragEvent is passed to this method via
     * its parameter.
     *
     * Tip: you an use the isAccepting() method to check
     * if the current drag and drop process is transferring
     * data of relevance to this {@link DragStopper}.
     */
    void onDragRegionEnter(MouseDragEvent event);

    /**
     * This method is called whenver the the mouse,
     * while dragging with an active {@link DragDropOverlay},
     * exits this {@link DragStopper}. This is a convenience
     * method which can be used to implement
     * special highlighting or styling to provide
     * visual indicators.
     *
     * The MouseDragEvent is passed to this method via
     * its parameter.
     */
    void onDragRegionExit(MouseDragEvent event);

    /**
     * It is the implementor's responsibility to specify
     * the expected type of this {@link DragStopper}.
     * Do so by implementing this method.
     * This should never return null. Never, ever, ever.
     *
     * @return the expected Class
     */
    Class<? extends Listable> getExpectedType();

    /**
     * A check to see if this {@link DragStopper} can accept
     * the data that is currently being dragged.
     *
     * @return true if the data that is being dragged is of the
     * expected type of this {@link DragStopper}, false otherwise.
     */
    default boolean isAccepting() {
        return DragDropOverlay.checkMatchingTypes(getExpectedType());
    }
}
